package org.openehealth.ipf.commons.ihe.fhir.support.audit.marshal;

import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Coding;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.unmarshal.dicom.DICOMAuditParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AbstractFhirAuditSerializationStrategyTest {

    @Test
    void testSerializationFromAtnaAuditMessage() throws IOException {
        final var serializationStrategy = new BalpJsonSerializationStrategy();
        final var auditMessage = this.readAuditMessageResource("/audit-message-iti55.xml");
        final var auditEvent = serializationStrategy.translate(auditMessage);

        assertNotNull(auditEvent);
        assertTrue(new Coding("http://dicom.nema.org/resources/ontology/DCM", "110112", "Query")
                       .equalsDeep(auditEvent.getType()));
        assertEquals(1, auditEvent.getSubtype().size());
        assertTrue(new Coding("urn:ihe:event-type-code", "ITI-55", "Cross Gateway Patient Discovery")
                       .equalsDeep(auditEvent.getSubtype().get(0)));
        assertEquals(AuditEvent.AuditEventAction.E, auditEvent.getAction());
        assertEquals(Date.from(Instant.ofEpochMilli(1740503022163L)), auditEvent.getRecorded());
        assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());

        assertEquals(2, auditEvent.getAgent().size());
        var agent = auditEvent.getAgent().get(0);
        assertTrue(new Coding("http://dicom.nema.org/resources/ontology/DCM", "110153", "Source Role ID")
                       .equalsDeep(agent.getType().getCodingFirstRep()));
        assertEquals("2316", agent.getAltId());
        assertTrue(agent.getRequestor());
        assertEquals("192.168.42.20", agent.getNetwork().getAddress());
        assertEquals(AuditEvent.AuditEventAgentNetworkType._2, agent.getNetwork().getType());

        agent = auditEvent.getAgent().get(1);
        assertTrue(new Coding("http://dicom.nema.org/resources/ontology/DCM", "110152", "Destination Role ID")
                       .equalsDeep(agent.getType().getCodingFirstRep()));
        assertEquals("http://localhost:62086/ipf/iti55", agent.getWho().getDisplay());
        assertFalse(agent.getRequestor());
        assertEquals("localhost", agent.getNetwork().getAddress());
        assertEquals(AuditEvent.AuditEventAgentNetworkType._1, agent.getNetwork().getType());

        assertEquals("1.2.3.99", auditEvent.getSource().getSite());
        assertEquals("IheSpringBootTest", auditEvent.getSource().getObserver().getDisplay());

        assertEquals(2, auditEvent.getEntity().size());
        var entity = auditEvent.getEntity().get(0);
        assertTrue(new Coding("http://terminology.hl7.org/CodeSystem/audit-entity-type", "2", null)
                       .equalsDeep(entity.getType()));
        assertTrue(new Coding("http://terminology.hl7.org/CodeSystem/object-role", "24", null)
                       .equalsDeep(entity.getRole()));
        assertNotNull(entity.getQuery());

        entity = auditEvent.getEntity().get(1);
        assertTrue(new Coding("http://terminology.hl7.org/CodeSystem/audit-entity-type", "4", null)
                       .equalsDeep(entity.getType()));
        assertTrue(new Coding("http://terminology.hl7.org/CodeSystem/object-role", "26", null)
                       .equalsDeep(entity.getRole()));
        assertEquals("00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01", entity.getWhat().getIdentifier().getValue());
    }

    private AuditMessage readAuditMessageResource(final String filename) throws IOException {
        final var content = getClass().getResourceAsStream(filename).readAllBytes();
        return new DICOMAuditParser().parse(new String(content, StandardCharsets.UTF_8), false);
    }
}