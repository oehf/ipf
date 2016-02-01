/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.iti78

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v25.segment.PID
import org.apache.commons.lang3.Validate
import org.hl7.fhir.instance.model.*
import org.hl7.fhir.instance.model.Address.AddressUse
import org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem
import org.hl7.fhir.instance.model.ContactPoint.ContactPointUse
import org.hl7.fhir.instance.model.HumanName.NameUse
import org.hl7.fhir.instance.model.valuesets.V3MaritalStatus
import org.hl7.fhir.instance.model.valuesets.V3NullFlavor
import org.openehealth.ipf.commons.ihe.fhir.Utils
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorHL7v2ToFhir
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.RSP_K21
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Translates HL7v2 PDQ Query Response message into a list of {@link Patient} resource
 * that gets converted into a Bundle by the underlying FHIR framework.
 *
 * Also cares about error responses and throws the appropriate Exceptions for the
 * FHIR framework.
 * Depending on the prefix query tag in QPD-2 (search or get) the translator decides
 * about whether to generate a bundle or a single patient resource.
 *
 * @since 3.1
 */
class PdqResponseToPdqmResponseTranslator implements TranslatorHL7v2ToFhir {

    private static final Logger LOG = LoggerFactory.getLogger(PdqResponseToPdqmResponseTranslator)
    private final UriMapper uriMapper

    String pdqSupplierResourceIdentifierUri
    private String pdqSupplierResourceIdentifierOid

    /**
     * @param uriMapper mapping for translating FHIR URIs into OIDs
     */
    PdqResponseToPdqmResponseTranslator(UriMapper uriMapper) {
        Validate.notNull(uriMapper, "URI Mapper must not be null")
        this.uriMapper = uriMapper
    }

    /**
     * @param pdqSupplierResourceIdentifierUri the URI of the resource identifier system
     */
    void setPdqSupplierResourceIdentifierUri(String pdqSupplierResourceIdentifierUri) {
        Validate.notNull(pdqSupplierResourceIdentifierUri, "Resource Identifier URI must not be null")
        this.pdqSupplierResourceIdentifierUri = pdqSupplierResourceIdentifierUri
        this.pdqSupplierResourceIdentifierOid = uriMapper.uriToOid(pdqSupplierResourceIdentifierUri)
    }

    @Override
    Object translateHL7v2ToFhir(Message message, Map<String, Object> parameters) {
        String ackCode = message.QAK[2].value
        boolean returnBundle = message.QPD[2].value.startsWith(PdqmRequestToPdqQueryTranslator.SEARCH_TAG)
        switch (ackCode) {
            case 'OK': return returnBundle ?
                    handleRegularSearchResponse(message.QUERY_RESPONSE()) :
                    handleRegularResource(message.QUERY_RESPONSE()) // Case 1
            case 'NF': return handleRegularSearchResponse(null)  // Case 2
            case 'AE': throw handleErrorResponse(message) // Cases 3-5
            default: throw new InternalErrorException("Unexpected ack code " + ackCode)
        }
    }

    /**
     * Converts a single query response into a Patient
     * @param responseCollection query response
     * @return Patient resource or null if responseCollection was empty
     */
    protected PdqPatient handleRegularResource(responseCollection) {
        List<PdqPatient> result = handleRegularSearchResponse(responseCollection)
        result.empty ? null : result.get(0)
    }

    /**
     * Converts a collection of query responses into a Patient resource list
     * @param responseCollection query responses
     * @return Patient resource list
     */
    protected List<PdqPatient> handleRegularSearchResponse(responseCollection) {
        List<PdqPatient> resultList = new ArrayList<>();
        if (responseCollection) {
            for (response in responseCollection) {
                PdqPatient patient = new PdqPatient()
                PID pid = response.PID

                // This assigns the resource ID. It is taken from the PID-3 identifier list where the
                // namespace matches pdqSupplierResourceIdentifierOid
                def resourcePid = pid[3]().find { pid3 -> pdqSupplierResourceIdentifierOid == pid3[4][2].value }
                if (resourcePid) {
                    patient.setId(new IdType('Patient', resourcePid[1].value))
                } else {
                    LOG.warn("No ID found with resource system URI {}", pdqSupplierResourceIdentifierUri)
                }

                convertIdentifiers(pid[3](), patient.getIdentifier())

                // Convert names
                if (!pid[5].empty) {
                    convertNames(pid[5](), patient.getName())
                }
                if (!pid[6].empty) {
                    patient.setMothersMaidenName(convertName(pid[6]))
                }
                if (pid[7]?.value) patient.setBirthDateElement(DateType.parseV3(pid[7].value))
                if (pid[8]?.value) patient.setGender(
                        Enumerations.AdministrativeGender.fromCode(pid[8].value?.map('hl7v2fhir-patient-administrativeGender')))

                // No race in the default FHIR patient resource (but in the DAF profile)

                if (!pid[11].empty) {
                    convertAddresses(pid[11](), patient.getAddress())
                }
                if (!pid[13].empty) {
                    convertTelecoms(pid[13](), patient.getTelecom(), ContactPointUse.HOME, ContactPointSystem.PHONE)
                }
                if (!pid[14].empty) {
                    convertTelecoms(pid[14](), patient.getTelecom(), ContactPointUse.WORK, ContactPointSystem.PHONE)
                }
                // TODO may be needs conversion, expectation is something like en-US or de
                if (pid[15]?.value) {
                    CodeableConcept language = new CodeableConcept()
                    language.addCoding().setCode(pid[15].value)
                    patient.getCommunication().add(
                            new Patient.PatientCommunicationComponent().setLanguage(language))
                }
                if (pid[16]?.value) {
                    CodeableConcept maritalStatus = new CodeableConcept()
                    String mapped = pid[16].value.map('hl7v2fhir-patient-maritalStatus')
                    V3MaritalStatus mappedMaritalStatus = "UNK".equals(mapped) ? V3NullFlavor.UNK : V3MaritalStatus.fromCode(mapped)
                    maritalStatus.addCoding()
                            .setCode(mapped)
                            .setSystem(mappedMaritalStatus.system)
                            .setDisplay(mappedMaritalStatus.display)
                    patient.setMaritalStatus(maritalStatus)
                }

                // No religion in the default FHIR patient resource

                // FIXME: Often, these identifiers come without any namespace information, so they must
                // be somehow enhanced
                if (pid[18].value) {
                    patient.getIdentifier().add(convertIdentifier(pid[18]))
                }
                // FIXME: Often, these identifiers come without any namespace information, so they must
                // be enhanced with static information (SSN, AHV, NHS etc.)
                if (pid[19].value) {
                    patient.getIdentifier().add(convertIdentifier(pid[19]))
                }

                // No ethnicity in the default FHIR patient resource (but in the DAF profile)

                // No birth place in the default FHIR patient resource

                // Multiple Birth
                if (pid[25].value) {
                    patient.setMultipleBirth(new IntegerType(pid[25].value))
                } else if (pid[24].value) {
                    patient.setMultipleBirth(new BooleanType(pid[24].value == 'Y'))
                }

                // Death Indicators
                if (pid[29].value) {
                    patient.setDeceased(DateTimeType.parseV3(pid[29].value))
                } else if (pid[30].value) {
                    patient.setDeceased(new BooleanType(pid[30].value == 'Y'))
                }

                resultList.add(patient)
            }
        }
        resultList
    }

    // Handle an error response from the Cross-reference manager
    protected BaseServerResponseException handleErrorResponse(RSP_K21 message) {

        // Check error locations
        int errorField = message.ERR[2][3]?.value ? Integer.parseInt(message.ERR[2][3]?.value) : 0
        if (errorField == 8) {
            throw Utils.unknownTargetDomain()
        } else {
            throw Utils.unexpectedProblem()
        }

    }

    // More generic HL7v2 to FHIR converting. Should be probably moved out of here....

    private void convertIdentifiers(cxs, List<Identifier> identifiers) {
        for (def cx : cxs) {
            identifiers.add(convertIdentifier(cx))
        }
    }

    /**
     * Converts a CX instance into an {@link Identifier} instance
     * @param cx CX composite
     * @return an {@link Identifier} instance
     */
    protected Identifier convertIdentifier(cx) {
        Identifier identifier = new Identifier()
                .setUse(Identifier.IdentifierUse.OFFICIAL)
                .setSystem(uriMapper.oidToUri(cx[4][2]?.value))
                .setValue(cx[1]?.value)
        identifier
    }

    protected void convertAddresses(xads, List<Address> addresses) {
        for (def xad : xads) {
            addresses.add(convertAddress(xad))
        }
    }

    /**
     * Converts a XAD instance into an {@link Address} instance
     * @param xad XAD composite
     * @return an {@link Address} instance
     */
    protected Address convertAddress(xad) {
        Address address = new Address()
                .setCity(xad[3]?.value)
                .setCountry(xad[6]?.value)
                .setPostalCode(xad[5]?.value)
                .setState(xad[4]?.value)
                .setDistrict(xad[9]?.value)
                .setUse(addressUse(xad[7], AddressUse.HOME))
        if (xad[1]?.value) address.getLine().add(new StringType(xad[1]?.value))
        if (xad[2]?.value) address.getLine().add(new StringType(xad[2]?.value))
        address
    }

    protected void convertNames(xpns, List<HumanName> names) {
        for (def xpn : xpns) {
            names.add(convertName(xpn))
        }
    }

    /**
     * Converts a XPN instance into an {@link HumanName} instance
     * @param xpn XPN composite
     * @return an {@link HumanName} instance
     */
    protected HumanName convertName(xpn) {
        HumanName name = new HumanName().setUse(nameUse(xpn[7], NameUse.OFFICIAL))
        if (xpn[1]?.value) name.getFamily().add(new StringType(xpn[1].value))
        if (xpn[2]?.value) name.getGiven().add(new StringType(xpn[2].value))
        if (xpn[3]?.value) name.getGiven().add(new StringType(xpn[3].value))
        if (xpn[4]?.value) name.getSuffix().add(new StringType(xpn[4].value))
        if (xpn[5]?.value) name.getPrefix().add(new StringType(xpn[5].value))
        name
    }

    private void convertTelecoms(xtns, List<ContactPoint> telecoms, ContactPointUse defaultUse, ContactPointSystem defaultSystem) {
        for (def xtn : xtns) {
            telecoms.add(convertTelecom(xtn, defaultUse, defaultSystem))
        }
    }

    /**
     * Converts a XTN instance into an {@link ContactPoint} instance
     * @param xtn XTN composite
     * @return an {@link ContactPoint} instance
     */
    protected ContactPoint convertTelecom(xtn, ContactPointUse defaultUse, ContactPointSystem defaultSystem) {
        ContactPoint telecom = new ContactPoint()
                .setUse(telecomUse(xtn[2], defaultUse))
                .setSystem(telecomSystem(xtn[3], defaultSystem))
                .setValue(xtn[1]?.value)
        if (xtn[4]?.value) {
            telecom.setSystem(ContactPointSystem.EMAIL)
                    .setValue(xtn[4]?.value)
                    .setUse(ContactPointUse.NULL)
        }
        telecom
    }

    protected ContactPointUse telecomUse(field, ContactPointUse defaultUse) {
        String fhirTelecomUse = (field?.value ?: '').map('hl7v2fhir-telecom-use')
        return fhirTelecomUse ? ContactPointUse.fromCode(fhirTelecomUse) : defaultUse
    }

    protected ContactPointSystem telecomSystem(field, ContactPointSystem defaultSystem) {
        String fhirTelecomSystem = (field?.value ?: '').map('hl7v2fhir-telecom-type')
        return fhirTelecomSystem ? ContactPointSystem.fromCode(fhirTelecomSystem) : defaultSystem
    }

    protected NameUse nameUse(field, NameUse defaultUse) {
        String fhirNameUse = (field?.value ?: '').map('hl7v2fhir-name-use')
        return fhirNameUse ? NameUse.fromCode(fhirNameUse) : defaultUse
    }

    protected AddressUse addressUse(field, AddressUse defaultUse) {
        String addressUse = (field?.value ?: '').map('hl7v2fhir-address-use')
        return addressUse ? AddressUse.fromCode(addressUse) : defaultUse
    }
}