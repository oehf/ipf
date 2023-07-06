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

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v25.datatype.XAD
import ca.uhn.hl7v2.model.v25.segment.PID
import org.hl7.fhir.r4.model.*
import org.hl7.fhir.r4.model.Address.AddressUse
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse
import org.hl7.fhir.r4.model.HumanName.NameUse
import org.hl7.fhir.r4.model.codesystems.V3MaritalStatus
import org.hl7.fhir.r4.model.codesystems.V3NullFlavor
import org.hl7.fhir.r4.model.codesystems.V3ReligiousAffiliation
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Utils
import org.openehealth.ipf.commons.ihe.fhir.translation.ToFhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.UnmappableUriException
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.RSP_K21
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static java.util.Objects.requireNonNull

/**
 * Translates HL7v2 PDQ Query Response message into a list of {@link Patient} resource
 * that gets converted into a Bundle by the underlying FHIR framework.
 *
 * Also cares about error responses and throws the appropriate Exceptions for the
 * FHIR framework.
 * Depending on the prefix query tag in QPD-2 (search or get) the translator decides
 * about whether to generate a bundle or a single patient resource.
 *
 * @author Christian Ohr
 * @since 3.6
 */
class PdqResponseToPdqmResponseTranslator implements ToFhirTranslator<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(PdqResponseToPdqmResponseTranslator)
    private final UriMapper uriMapper

    String pdqSupplierResourceIdentifierUri
    String pdqSupplierResourceIdentifierOid
    String nationalIdentifierUri

    /**
     * @param uriMapper mapping for translating FHIR URIs into OIDs
     */
    PdqResponseToPdqmResponseTranslator(UriMapper uriMapper) {
        requireNonNull(uriMapper, "URI Mapper must not be null")
        this.uriMapper = uriMapper
    }

    /**
     * @param pdqSupplierResourceIdentifierUri the URI of the resource identifier system
     */
    void setPdqSupplierResourceIdentifierUri(String pdqSupplierResourceIdentifierUri) {
        requireNonNull(pdqSupplierResourceIdentifierUri, "Resource Identifier URI must not be null")
        this.pdqSupplierResourceIdentifierUri = pdqSupplierResourceIdentifierUri
        this.pdqSupplierResourceIdentifierOid = uriMapper.uriToOid(pdqSupplierResourceIdentifierUri)
                .orElseThrow({new UnmappableUriException(pdqSupplierResourceIdentifierUri)})
    }

    void setNationalIdentifierUri(String nationalIdentifierUri) {
        this.nationalIdentifierUri = nationalIdentifierUri
    }

    @Override
    List<PdqPatient> translateToFhir(Message message, Map<String, Object> parameters) {
        String ackCode = message.QAK[2].value
        switch (ackCode) {
            case 'OK': return handleRegularSearchResponse(message.QUERY_RESPONSE()) // Case 1,2
            case 'NF': return handleRegularSearchResponse(null)     // Case 3
            case 'AE': return handleErrorResponse(message)          // Cases 4-5
            default: throw new InternalErrorException("Unexpected ack code " + ackCode)
        }
    }

    /**
     * Converts a collection of query responses into a Patient resource list
     * @param responseCollection query responses
     * @return Patient resource list
     */
    protected List<PdqPatient> handleRegularSearchResponse(responseCollection) {
        def resultList = new ArrayList<PdqPatient>()
        if (responseCollection) {
            for (response in responseCollection) {
                PdqPatient patient = pidToPatient(response)
                addSearchScore(patient, response)
                resultList.add(patient)
            }
        }
        resultList
    }

    protected static void addSearchScore(PdqPatient pdqPatient, response) {
        ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(pdqPatient, BundleEntrySearchModeEnum.MATCH)
        /*
        String searchScoreString = response.QRI[1]?.value
        if (searchScoreString != null) {
            double searchScore = Integer.valueOf(searchScoreString) / 100
            pdqPatient.setUserData(ResourceMetadataKeyEnum.ENTRY_SCORE.name(), new DecimalDt(searchScore) )
        }
        */
    }

    protected PdqPatient pidToPatient(response) {
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

        String gender = pid[8]?.value
        convertGender(gender, patient)

        // No race (10) in the default FHIR patient resource (but in the US Core profile). Conversion not implemented yet.

        if (!pid[11].empty) {
            convertAddresses(pid[11](), patient.getAddress())
        }
        if (!pid[13].empty) {
            convertTelecoms(pid[13](), patient.getTelecom(), ContactPointUse.HOME, ContactPointSystem.PHONE)
        }
        if (!pid[14].empty) {
            convertTelecoms(pid[14](), patient.getTelecom(), ContactPointUse.WORK, ContactPointSystem.PHONE)
        }

        if (pid[15]?.value) {
            patient.addCommunication().setLanguage(makeCodeableConcept(pid[15].value, "urn:ietf:bcp:47", null))
        }

        if (pid[16]?.value) {
            String mapped = pid[16].value.map('hl7v2fhir-patient-maritalStatus')
            def mappedMaritalStatus
            switch (mapped) {
                case "UNK":
                    mappedMaritalStatus = V3NullFlavor.UNK; break
                case "U":
                    mappedMaritalStatus = new Coding()
                            .setSystem('http://hl7.org/fhir/marital-status')
                            .setCode('U')
                            .setDisplay('Unmarried'); break
                default: mappedMaritalStatus = V3MaritalStatus.fromCode(mapped)
            }
            patient.setMaritalStatus(makeCodeableConcept(mapped, mappedMaritalStatus.system, mappedMaritalStatus.display))
        }

        if (pid[17].value) {
            String mapped = pid[17].value.map('hl7v2fhir-patient-religion')
            def mappedReligion
            switch (mapped) {
                case "UNK":
                    mappedReligion = V3NullFlavor.UNK; break
                default: mappedReligion = V3ReligiousAffiliation.fromCode(mapped)
            }
            patient.addReligion(makeCodeableConcept(mapped, mappedReligion.system, mappedReligion.display))


        }

        if (pid[18].value) {
            patient.addIdentifier(convertIdentifier(pid[18]))
        }

        if (pid[19].value) {
            patient.addIdentifier(new Identifier()
                    .setSystem(nationalIdentifierUri)
                    .setValue(pid[19].value))
        }

        // No ethnicity (22) in the default FHIR patient resource (but in the US Core profile). Conversion not implemented yet

        // No birth place in the default FHIR patient resource
        if (!pid[23].empty) {
            XAD xad = new XAD(null)
            xad[3] = pid[23].value
            patient.setBirthPlace(convertAddress(xad))
        }

        // Multiple Birth
        if (pid[25].value) {
            patient.setMultipleBirth(new IntegerType(pid[25].value))
        } else if (pid[24].value) {
            patient.setMultipleBirth(new BooleanType(pid[24].value == 'Y'))
        }

        // Citizenship
        if (pid[26].value) {
            String mapped = pid[26].value.map('hl7v2fhir-patient-citizenship')
            def mappedCitizenship
            switch (mapped) {
                case "UNK":
                    mappedCitizenship = V3NullFlavor.UNK; break
                default: mappedCitizenship = new Coding()
                    .setCode(mapped)
                    .setSystem("urn:iso:std:iso:3166")
                    .setDisplay(mapped)
            }
            patient.addCitizenship()
                    .setCode(makeCodeableConcept(mapped, mappedCitizenship.system, mappedCitizenship.display))
        }

        // Death Indicators
        if (pid[29].value) {
            patient.setDeceased(DateTimeType.parseV3(pid[29].value))
        } else if (pid[30].value) {
            patient.setDeceased(new BooleanType(pid[30].value == 'Y'))
        }

        patient
    }

    protected void convertGender(String gender, PdqPatient patient) {
        if (gender) {
            patient.setGender(
                    Enumerations.AdministrativeGender.fromCode(gender.map('hl7v2fhir-patient-administrativeGender').toString()))
            patient.setGenderIdentity(
                    makeCodeableConcept(
                            gender.map('hl7v2fhir-patient-genderIdentity').toString(),
                            'hl7v2fhir-patient-genderIdentity'.valueSystem(),
                            null
                    ))
        }
    }

    protected static CodeableConcept makeCodeableConcept(String code, String system, String display) {
        CodeableConcept codeableConcept = new CodeableConcept()
        codeableConcept.addCoding()
                .setCode(code)
                .setSystem(system)
                .setDisplay(display)
        codeableConcept
    }


    // Handle an error response from the Cross-reference manager
    protected static List<PdqPatient> handleErrorResponse(RSP_K21 message) {

        // Check error locations
        int errorField = message.ERR[2][3]?.value ? Integer.parseInt(message.ERR[2][3]?.value) : 0
        if (errorField == 8) {
            throw Utils.unknownTargetDomainValue()
        } else if (errorField == 3) {
            throw Utils.unknownPatientDomain()
        } else {
            throw Utils.unexpectedProblem()
        }

    }

    // More generic HL7v2 to FHIR converting. Should be probably moved out of here....

    protected void convertIdentifiers(cxs, List<Identifier> identifiers) {
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
        if (xad[1]?.value) address.addLine(xad[1]?.value)
        if (xad[2]?.value) address.addLine(xad[2]?.value)
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
        if (xpn[1]?.value) name.setFamily(xpn[1].value)
        if (xpn[2]?.value) name.addGiven(xpn[2].value)
        if (xpn[3]?.value) name.addGiven(xpn[3].value)
        if (xpn[4]?.value) name.addSuffix(xpn[4].value)
        if (xpn[5]?.value) name.addPrefix(xpn[5].value)
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
                    .setValue(xtn[4].value)
        }
        if ("CP" == xtn[3]?.value) {
            telecom.setUse(ContactPointUse.MOBILE)
        }
        telecom
    }

    protected static ContactPointUse telecomUse(field, ContactPointUse defaultUse) {
        String fhirTelecomUse = (field?.value ?: '').map('hl7v2fhir-telecom-use')
        return fhirTelecomUse ? ContactPointUse.fromCode(fhirTelecomUse) : defaultUse
    }

    protected static ContactPointSystem telecomSystem(field, ContactPointSystem defaultSystem) {
        String fhirTelecomSystem = (field?.value ?: '').map('hl7v2fhir-telecom-type')
        return fhirTelecomSystem ? ContactPointSystem.fromCode(fhirTelecomSystem) : defaultSystem
    }

    protected static NameUse nameUse(field, NameUse defaultUse) {
        String fhirNameUse = (field?.value ?: '').map('hl7v2fhir-name-use')
        return fhirNameUse ? NameUse.fromCode(fhirNameUse) : defaultUse
    }

    protected static AddressUse addressUse(field, AddressUse defaultUse) {
        String addressUse = (field?.value ?: '').map('hl7v2fhir-address-use')
        return addressUse ? AddressUse.fromCode(addressUse) : defaultUse
    }
}