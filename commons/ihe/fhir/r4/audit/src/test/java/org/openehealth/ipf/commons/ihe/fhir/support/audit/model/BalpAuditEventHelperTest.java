/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.support.audit.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A participant object ID has to end up as something a reader can resolve, not as prose: BALP is about
 * records that hold a well-formed indication of the patient, and PIXm requires
 * {@code entity.what.identifier} outright.
 *
 * @author Christian Ohr
 */
public class BalpAuditEventHelperTest {

    @Test
    public void testAFhirTokenBecomesASystemAndAValue() {
        var reference = BalpAuditEventHelper.reference("urn:oid:1.2.3.4|0815");

        assertEquals("urn:oid:1.2.3.4", reference.getIdentifier().getSystem());
        assertEquals("0815", reference.getIdentifier().getValue());
    }

    @Test
    public void testACxBecomesASystemAndAValue() {
        var reference = BalpAuditEventHelper.reference("0815^^^&1.2.3.4&ISO");

        assertEquals("urn:oid:1.2.3.4", reference.getIdentifier().getSystem());
        assertEquals("0815", reference.getIdentifier().getValue());
    }

    @Test
    public void testALiteralReferenceStaysOne() {
        var reference = BalpAuditEventHelper.reference("Patient/a2");

        assertEquals("Patient/a2", reference.getReference());
        assertFalse(reference.hasIdentifier());
    }

    @Test
    public void testATokenWithoutASystemKeepsItsValue() {
        var reference = BalpAuditEventHelper.reference("|0815");

        assertEquals("0815", reference.getIdentifier().getValue());
        assertNull(reference.getIdentifier().getSystem());
    }

    /**
     * A document unique id, a SubmissionSet UUID: no system to go with it, but an identifier all the
     * same. The generic AuditMessage translation treats a bare id the same way.
     */
    @Test
    public void testABareIdBecomesAnIdentifierWithoutASystem() {
        var reference = BalpAuditEventHelper.reference("1.2.3.4.5.6.7.8.9");

        assertEquals("1.2.3.4.5.6.7.8.9", reference.getIdentifier().getValue());
        assertNull(reference.getIdentifier().getSystem());
        assertFalse(reference.hasDisplay());
    }

    @Test
    public void testASubmissionSetUuidBecomesAnIdentifier() {
        var reference = BalpAuditEventHelper.reference("urn:uuid:6b1a1b0e-1f6c-4a1e-9b0e-2c9f4b6a1c11");

        assertEquals("urn:uuid:6b1a1b0e-1f6c-4a1e-9b0e-2c9f4b6a1c11", reference.getIdentifier().getValue());
    }

    @Test
    public void testNoIdYieldsAnEmptyReference() {
        assertTrue(BalpAuditEventHelper.reference(null).isEmpty());
        assertTrue(BalpAuditEventHelper.reference("  ").isEmpty());
    }

}
