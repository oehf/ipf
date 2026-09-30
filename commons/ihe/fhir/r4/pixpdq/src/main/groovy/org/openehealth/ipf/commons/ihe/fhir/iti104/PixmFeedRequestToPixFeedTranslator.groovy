/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.iti104

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException
import ca.uhn.hl7v2.model.Message
import org.hl7.fhir.r4.model.Address
import org.hl7.fhir.r4.model.BooleanType
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.ContactPoint
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.HumanName
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.IntegerType
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.PrimitiveType
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Utils
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmPatient
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.PIX
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static java.util.Objects.requireNonNull

/**
 * Translates a PIXm Patient Identity Feed FHIR [ITI-104] request into a HL7v2 PIX Feed [ITI-8] message,
 * as done by a Patient Identifier Cross-reference Manager that proxies ITI-104 to ITI-8:
 * <ul>
 *     <li>Add/Revise Patient (conditional update) is translated into ADT^A08 (configurable, see
 *     {@link #addReviseEventType}), as the Manager cannot tell whether the patient exists</li>
 *     <li>Resolve Duplicate Patient (conditional update with a replaced-by link) is translated into
 *     ADT^A40, with the surviving patient in PID-3 and the subsumed patient in MRG-1</li>
 *     <li>Remove Patient (conditional delete) cannot be translated, as ITI-8 has no equivalent, and is
 *     rejected</li>
 * </ul>
 * For Add/Revise Patient, the identifier of the condition goes into the first repetition of PID-3, all
 * other identifiers of the Patient into subsequent repetitions of PID-3. Identifiers whose system cannot
 * be mapped to an assigning authority are skipped, except for the identifier of the condition, which
 * causes the request to be rejected.
 * <p>
 * For Resolve Duplicate Patient, the A40 only carries minimal demographics, as it is not meant to update
 * the patient: PID-3 with the identifier of the surviving patient, and PID-5, which HL7 v2.3.1 requires.
 * The other identifiers of the Patient belong to the subsumed patient, and would otherwise be
 * cross-referenced with the surviving patient.
 * <p>
 * Coded values are translated with the {@code fhir2hl7v2-*} mappings of
 * {@code META-INF/map/fhir-hl7v2-translation.mapping.xml}, which must be available in the mapping service.
 * <p>
 * The event type of the resulting message is also put into the parameters as
 * {@link #PIX_FEED_EVENT_TYPE}, so that {@link PixFeedResponseToPixmFeedResponseTranslator} can tell
 * whether the patient has been created.
 *
 * @author Christian Ohr
 * @since 6.0
 */
class PixmFeedRequestToPixFeedTranslator implements FhirTranslator<Message> {

    private static final Logger log = LoggerFactory.getLogger(PixmFeedRequestToPixFeedTranslator)

    /**
     * Name of the parameter (Camel header) the ITI-8 event type is put into
     */
    static final String PIX_FEED_EVENT_TYPE = 'PixFeedEventType'

    static final String MERGE_EVENT_TYPE = 'A40'

    String senderDeviceName = 'unknown'
    String senderFacilityName = 'unknown'
    String receiverDeviceName = 'unknown'
    String receiverFacilityName = 'unknown'

    /**
     * ITI-8 event type for Add/Revise Patient, one of A01, A04, A05 or A08
     */
    String addReviseEventType = 'A08'

    private final UriMapper uriMapper

    /**
     * @param uriMapper mapping for translating FHIR URIs into OIDs
     */
    PixmFeedRequestToPixFeedTranslator(UriMapper uriMapper) {
        requireNonNull(uriMapper, "URI Mapper must not be null")
        this.uriMapper = uriMapper
    }

    @Override
    Message translateFhir(Object request, Map<String, Object> parameters) {
        if (!(request instanceof Patient)) {
            // Conditional delete
            throw Utils.removePatientNotSupported()
        }
        Patient patient = (Patient) request
        Identifier identifier = Iti104Utils.patientIdentifier(patient, parameters)
                .orElseThrow { new InvalidRequestException('Could not determine the patient identifier of the ITI-104 request') }
        Identifier survivor = replacedByIdentifier(patient)

        String eventType = eventType(patient, parameters)
        Message message = PIX.FeedInteractions.ITI_8_PIX.hl7v2TransactionConfiguration.request(eventType)
        message.MSH[3] = senderDeviceName
        message.MSH[4] = senderFacilityName
        message.MSH[5] = receiverDeviceName
        message.MSH[6] = receiverFacilityName
        message.MSH[7][1] = Utils.hl7Timestamp()
        message.MSH[10] = UUID.randomUUID().toString()

        message.EVN[1] = eventType
        message.EVN[2][1] = Utils.hl7Timestamp()

        if (eventType == MERGE_EVENT_TYPE) {
            if (!survivor) {
                throw new InvalidRequestException('Resolve Duplicate Patient requires a replaced-by link with the identifier of the surviving patient')
            }
            if (survivor.system != identifier.system) {
                throw new InvalidRequestException("PIX Feed can only merge patients of the same domain, but got ${survivor.system} and ${identifier.system}")
            }
            populateIdentifier(message.PATIENT.MRG[1](0), identifier)
            populateMergePid(message.PATIENT.PID, patient, survivor)
        } else {
            populatePid(message.PID, patient, identifier)
            message.PV1[2] = 'N'
        }
        parameters?.put(PIX_FEED_EVENT_TYPE, eventType)
        message
    }

    /**
     * Determines the ITI-8 event type. Override to e.g. send an A04 when it is known that the patient is new.
     *
     * @param patient Patient of the ITI-104 request
     * @param parameters request parameters
     * @return ITI-8 event type
     */
    protected String eventType(Patient patient, Map<String, Object> parameters) {
        replacedByIdentifier(patient) ? MERGE_EVENT_TYPE : addReviseEventType
    }

    protected static Identifier replacedByIdentifier(Patient patient) {
        patient.link
                .find { it.type == Patient.LinkType.REPLACEDBY && it.other?.identifier?.hasValue() }
                ?.other?.identifier
    }

    /**
     * Populates the PID segment of an A40 message with minimal demographics: the identifier of the surviving
     * patient and the name(s) of the Patient, as required by HL7 v2.3.1.
     *
     * @param pid PID segment
     * @param patient Patient
     * @param survivor identifier of the surviving patient
     */
    protected void populateMergePid(pid, Patient patient, Identifier survivor) {
        populateIdentifier(pid[3](0), survivor)
        patient.name.eachWithIndex { HumanName name, int i -> populateName(pid[5](i), name) }
    }

    /**
     * Populates the PID segment of an A01/A04/A05/A08 message from the Patient.
     *
     * @param pid PID segment
     * @param patient Patient
     * @param identifier identifier for the first repetition of PID-3
     */
    protected void populatePid(pid, Patient patient, Identifier identifier) {
        populateIdentifier(pid[3](0), identifier)
        patient.identifier
                .findAll { other -> !isSame(other, identifier) }
                .each { other ->
                    if (other.value && isMappable(other.system)) {
                        Utils.populateIdentifier(Utils.nextRepetition(pid[3]), uriMapper, other.system, other.value)
                    } else {
                        log.warn('Skipping identifier {}|{}, as its system cannot be mapped to an assigning authority', other.system, other.value)
                    }
                }

        patient.name.eachWithIndex { HumanName name, int i -> populateName(pid[5](i), name) }
        String mothersMaidenName = mothersMaidenName(patient)
        if (mothersMaidenName) {
            pid[6](0)[1][1] = mothersMaidenName
        }
        if (patient.hasBirthDate()) {
            pid[7][1] = patient.birthDateElement.valueAsString.replace('-', '')
        }
        if (patient.hasGender()) {
            pid[8] = patient.gender.toCode().map('hl7v2fhir-patient-gender')
        }
        patient.address.eachWithIndex { Address address, int i -> populateAddress(pid[11](i), address) }

        def (work, home) = patient.telecom.split { it.use == ContactPoint.ContactPointUse.WORK }
        home.eachWithIndex { ContactPoint telecom, int i -> populateTelecom(pid[13](i), telecom) }
        work.eachWithIndex { ContactPoint telecom, int i -> populateTelecom(pid[14](i), telecom) }

        def language = patient.communication.find { it.preferred } ?: patient.communication.find()
        if (language?.language?.codingFirstRep?.code) {
            pid[15][1] = language.language.codingFirstRep.code
        }
        if (patient.maritalStatus?.codingFirstRep?.code) {
            pid[16][1] = patient.maritalStatus.codingFirstRep.code.map('fhir2hl7v2-patient-maritalStatus')
        }

        // Religion and birth place are not part of the PDQm/PIXm Patient profile, but of PdqmPatient.
        // Both are the inverse of PdqResponseToPdqmResponseTranslator.
        String religion = religionCode(patient)
        if (religion) {
            String mapped = religion.map('fhir2hl7v2-patient-religion')
            if (mapped) {
                pid[17][1] = mapped
            }
        }
        String birthPlace = birthPlace(patient)
        if (birthPlace) {
            pid[23] = birthPlace
        }

        if (patient.multipleBirth instanceof IntegerType) {
            pid[24] = 'Y'
            pid[25] = patient.multipleBirthIntegerType.valueAsString
        } else if (patient.multipleBirth instanceof BooleanType) {
            pid[24] = patient.multipleBirthBooleanType.booleanValue() ? 'Y' : 'N'
        }
        // Citizenship, inverse to PdqResponseToPdqmResponseTranslator. A code of the null flavor UNK
        // is taken over as it is, just like the other way round.
        citizenshipCodes(patient).eachWithIndex { String code, int i ->
            pid[26](i)[1] = code.mapReverse('hl7v2fhir-patient-citizenship')
        }
        if (patient.deceased instanceof DateTimeType) {
            pid[29][1] = hl7Timestamp(patient.deceasedDateTimeType)
            pid[30] = 'Y'
        } else if (patient.deceased instanceof BooleanType) {
            pid[30] = patient.deceasedBooleanType.booleanValue() ? 'Y' : 'N'
        }
    }

    protected void populateIdentifier(cx, Identifier identifier) {
        if (!identifier.value) {
            throw new InvalidRequestException('Patient identifier must have a value')
        }
        if (!Utils.populateIdentifier(cx, uriMapper, identifier.system, identifier.value)) {
            throw Utils.unknownIdentifierDomain(identifier.system)
        }
    }

    protected static void populateName(xpn, HumanName name) {
        xpn[1][1] = name.family ?: ''
        xpn[2] = name.given ? name.given[0].value : ''
        if (name.given.size() > 1) {
            xpn[3] = name.given.drop(1)*.value.join(' ')
        }
        if (name.hasSuffix()) {
            xpn[4] = name.suffix[0].value
        }
        if (name.hasPrefix()) {
            xpn[5] = name.prefix[0].value
        }
        // ITI-8 requires the name type code, legal name if there is none to be mapped
        xpn[7] = (name.hasUse() ? name.use.toCode().map('fhir2hl7v2-name-use') : null) ?: 'L'
    }

    protected static void populateAddress(xad, Address address) {
        if (address.line.size() > 0) {
            xad[1] = address.line[0].value
        }
        if (address.line.size() > 1) {
            xad[2] = address.line.drop(1)*.value.join(' ')
        }
        if (address.hasCity()) {
            xad[3] = address.city
        }
        if (address.hasState()) {
            xad[4] = address.state
        }
        if (address.hasPostalCode()) {
            xad[5] = address.postalCode
        }
        if (address.hasCountry()) {
            xad[6] = address.country
        }
        if (address.hasUse()) {
            xad[7] = address.use.toCode().map('fhir2hl7v2-address-use')
        }
        if (address.hasDistrict()) {
            xad[9] = address.district
        }
    }

    protected static void populateTelecom(xtn, ContactPoint telecom) {
        if (telecom.hasUse()) {
            xtn[2] = telecom.use.toCode().map('fhir2hl7v2-telecom-use')
        }
        if (telecom.system == ContactPoint.ContactPointSystem.EMAIL) {
            xtn[2] = 'NET'
            xtn[3] = 'Internet'
            xtn[4] = telecom.value
        } else {
            xtn[1] = telecom.value
            if (telecom.hasSystem()) {
                xtn[3] = telecom.system.toCode().map('fhir2hl7v2-telecom-type')
            }
            if (telecom.use == ContactPoint.ContactPointUse.MOBILE) {
                xtn[3] = 'CP'
            }
        }
    }

    protected static String mothersMaidenName(Patient patient) {
        if (patient instanceof PdqmPatient) {
            return patient.mothersMaidenName
        }
        def value = patient.getExtensionByUrl(PdqmProfile.MOTHERS_MAIDEN_NAME_EXTENSION)?.value
        value instanceof PrimitiveType ? value.valueAsString : null
    }

    protected static String religionCode(Patient patient) {
        def religion = patient instanceof PdqmPatient ?
                patient.religion.find() :
                patient.getExtensionByUrl(PdqmProfile.RELIGION_EXTENSION)?.value
        religion instanceof CodeableConcept ? religion.codingFirstRep?.code : null
    }

    /**
     * PID-23 is a plain string, which PdqResponseToPdqmResponseTranslator takes as city of the birth place
     */
    protected static String birthPlace(Patient patient) {
        def address = patient instanceof PdqmPatient ?
                patient.birthPlace :
                patient.getExtensionByUrl(PdqmProfile.BIRTH_PLACE_EXTENSION)?.value
        address instanceof Address ? (address.city ?: address.text) : null
    }

    protected static List<String> citizenshipCodes(Patient patient) {
        List<CodeableConcept> codes = patient instanceof PdqmPatient ?
                patient.citizenship*.code :
                patient.getExtensionsByUrl(PdqmProfile.CITIZENSHIP_EXTENSION)
                        .collect { it.getExtensionByUrl('code')?.value }
                        .findAll { it instanceof CodeableConcept }
        codes.findResults { it?.codingFirstRep?.code }
    }

    protected static String hl7Timestamp(DateTimeType dateTime) {
        dateTime.valueAsString
            .replaceAll('[-:T]', '')
            .replaceAll('\\.\\d+', '')
            .replaceAll('(Z|[+-]\\d{4})$', '')
    }

    private boolean isMappable(String system) {
        system && (uriMapper.uriToOid(system).isPresent() || uriMapper.uriToNamespace(system).isPresent())
    }

    private static boolean isSame(Identifier a, Identifier b) {
        a.system == b.system && a.value == b.value
    }
}
