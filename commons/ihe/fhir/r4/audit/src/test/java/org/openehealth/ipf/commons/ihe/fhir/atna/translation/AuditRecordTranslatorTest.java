/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.atna.translation;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType;
import org.openehealth.ipf.commons.audit.model.DicomObjectDescriptionType;
import org.openehealth.ipf.commons.audit.model.EventIdentificationType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.unmarshal.dicom.DICOMAuditParser;
import org.openehealth.ipf.commons.core.config.ContextFacade;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.commons.map.BidiMappingService;
import org.openehealth.ipf.commons.map.MappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Dmytro Rud
 */
class AuditRecordTranslatorTest {

    private static final Logger log = LoggerFactory.getLogger(AuditRecordTranslatorTest.class);
    private static final FhirContext FHIR_CONTEXT = FhirContext.forR4();

    static MappingService mappingService;

    @BeforeAll
    static void beforeClass() {
        mappingService = new BidiMappingService();
        ((BidiMappingService) mappingService).setMappingScript(
                AuditRecordTranslatorTest.class.getClassLoader().getResource("META-INF/map/atna2fhir.map"));

        Registry registry = EasyMock.createMock(Registry.class);
        ContextFacade.setRegistry(registry);
        EasyMock.expect(registry.bean(MappingService.class)).andReturn(mappingService).anyTimes();
        EasyMock.replay(registry);
    }

    @Test
    void testAuditRecordTranslation1() {
        var auditMessage = createAuditMessage1();
        log.debug("ATNA Record:{}", auditMessage);

        var translator = new AuditRecordTranslator(mappingService);
        var auditEvent = translator.translate(auditMessage);
        log.debug("FHIR Resource:{}",
                FHIR_CONTEXT.newXmlParser().setPrettyPrint(true).encodeResourceToString(auditEvent));

        auditEvent.getEntity().forEach(entity -> 
                assertNotNull(entity.getWhat().getIdentifier().getType()));
        
        assertEquals(47, auditEvent.getEntity().get(2).getQuery().length);
    }

    static AuditMessage createAuditMessage1() {
        var eventIdentification = new EventIdentificationType(
                EventIdCode.DICOMInstancesTransferred,
                Instant.ofEpochMilli(System.currentTimeMillis()),
                EventOutcomeIndicator.Success);
        eventIdentification.setEventActionCode(EventActionCode.Create);

        var participant1 = new ActiveParticipantType("123", false);
        participant1.setAlternativeUserID("AETITLE=AEFOO");
        participant1.setNetworkAccessPointID("192.168.1.2");
        participant1.setNetworkAccessPointTypeCode(NetworkAccessPointTypeCode.IPAddress);
        participant1.getRoleIDCodes().add(ActiveParticipantRoleIdCode.Source);

        var participant2 = new ActiveParticipantType("67562", false);
        participant2.setAlternativeUserID("AETITLE=AEPACS");
        participant2.setNetworkAccessPointID("192.168.1.5");
        participant2.setNetworkAccessPointTypeCode(NetworkAccessPointTypeCode.IPAddress);
        participant2.getRoleIDCodes().add(ActiveParticipantRoleIdCode.Destination);

        var participant3 = new ActiveParticipantType("smitty@readingroom.hospital.org", true);
        participant3.setUserName("Dr. Smith");
        participant3.setAlternativeUserID("smith@nema");
        participant3.setNetworkAccessPointID("192.168.1.2");
        participant3.setNetworkAccessPointTypeCode(NetworkAccessPointTypeCode.IPAddress);
        participant3.getRoleIDCodes().add(ActiveParticipantRoleIdCode.Source);

        var auditSource = new AuditSourceIdentificationType("ReadingRoom");
        auditSource.setAuditEnterpriseSiteID("Hospital");
        auditSource.getAuditSourceType().add(AuditSourceType.EndUserInterface);

        var object1 = new ParticipantObjectIdentificationType(
                "1.2.840.10008.2.3.4.5.6.7.78.8", 
                ParticipantObjectIdTypeCode.StudyInstanceUID);
        object1.setParticipantObjectTypeCode(ParticipantObjectTypeCode.System);
        object1.setParticipantObjectTypeCodeRole(ParticipantObjectTypeCodeRole.Report);
        object1.setParticipantObjectDataLifeCycle(ParticipantObjectDataLifeCycle.Origination);
        
        var description = new DicomObjectDescriptionType();
        description.getMPPS().add("1.2.840.10008.1.2.3.4.5");
        description.getAccession().add("12341234");
        
        var sopClass1 = new DicomObjectDescriptionType.SOPClass(1500);
        sopClass1.setUid("1.2.840.10008.5.1.4.1.1.2");
        description.getSOPClasses().add(sopClass1);
        
        var sopClass2 = new DicomObjectDescriptionType.SOPClass(3);
        sopClass2.setUid("1.2.840.10008.5.1.4.1.1.11.1");
        description.getSOPClasses().add(sopClass2);
        
        object1.getParticipantObjectDescriptions().add(description);

        var object2 = new ParticipantObjectIdentificationType(
                "ptid12345", 
                ParticipantObjectIdTypeCode.PatientNumber);
        object2.setParticipantObjectTypeCode(ParticipantObjectTypeCode.Person);
        object2.setParticipantObjectTypeCodeRole(ParticipantObjectTypeCodeRole.Patient);
        object2.setParticipantObjectName("John Doe");

        var object3 = new ParticipantObjectIdentificationType(
                "queryId", 
                ParticipantObjectIdTypeCode.SearchCriteria);
        object3.setParticipantObjectTypeCode(ParticipantObjectTypeCode.Other);
        object3.setParticipantObjectTypeCodeRole(ParticipantObjectTypeCodeRole.Query);
        object3.setParticipantObjectQuery("SELECT * FROM documents WHERE type=\"TOP SECRET\"".getBytes());

        var auditMessage = new AuditMessage();
        auditMessage.setEventIdentification(eventIdentification);
        auditMessage.getActiveParticipants().addAll(Arrays.asList(participant1, participant2, participant3));
        auditMessage.setAuditSourceIdentification(auditSource);
        auditMessage.getParticipantObjectIdentifications().addAll(Arrays.asList(object1, object2, object3));

        return auditMessage;
    }

    @Test
    void testAuditTranslation2() throws Exception {
        var parser = FHIR_CONTEXT.newJsonParser().setPrettyPrint(true);

        var atnaString = IOUtils.toString(Objects.requireNonNull(
            AuditRecordTranslatorTest.class.getClassLoader().getResourceAsStream("atna-record-2.xml")),
                StandardCharsets.UTF_8);
        
        var atna = new DICOMAuditParser().parse(atnaString, true);
        var fhir = new AuditRecordTranslator(mappingService).translate(atna);
        log.debug("FHIR resource:{}", parser.encodeResourceToString(fhir));

        var validator = FHIR_CONTEXT.newValidator();
        validator.registerValidatorModule(new FhirInstanceValidator(FHIR_CONTEXT));

        var validationResult = validator.validateWithResult(fhir);
        var operationOutcome = new OperationOutcome();
        validationResult.populateOperationOutcome(operationOutcome);
        log.debug("FHIR validation result:{}", parser.encodeResourceToString(operationOutcome));

        assertTrue(validationResult.isSuccessful());

        assertEquals(4, fhir.getAgent().size());

        assertEquals("110153", fhir.getAgent().get(0).getType().getCoding().get(0).getCode());
        assertEquals("https://community.epr.ch/Repository", 
                fhir.getAgent().get(0).getWho().getIdentifier().getValue());
        assertFalse(fhir.getAgent().get(0).hasRole());

        assertEquals("110152", fhir.getAgent().get(1).getType().getCoding().get(0).getCode());
        assertEquals("1234", fhir.getAgent().get(1).getWho().getIdentifier().getValue());
        assertFalse(fhir.getAgent().get(1).hasRole());

        assertFalse(fhir.getAgent().get(2).hasType());
        assertEquals("7601002860123", fhir.getAgent().get(2).getWho().getIdentifier().getValue());
        assertFalse(fhir.getAgent().get(2).hasRole());

        assertEquals("HCP", fhir.getAgent().get(3).getType().getCoding().get(0).getCode());
        assertEquals("7601002860123", fhir.getAgent().get(3).getWho().getIdentifier().getValue());
        assertEquals("223366009", fhir.getAgent().get(3).getRole().get(0).getCoding().get(0).getCode());
    }
}