/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.ietf.jgss.Oid;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.URN_IETF_RFC_3986;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.model.MhdIdentifierType.ENTRY_UUID;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.model.MhdIdentifierType.UNIQUE_ID;

/**
 * @author Christian Ohr
 */
public class MhdIdentifierTypeTest {

    @Test
    public void testIdentifiersCarryUseAndType() {
        var entryUuid = new EntryUuidIdentifier(UUID.randomUUID());
        assertEquals(Identifier.IdentifierUse.OFFICIAL, entryUuid.getUse());
        assertTrue(ENTRY_UUID.matches(entryUuid));
        assertFalse(UNIQUE_ID.matches(entryUuid));

        var uniqueId = new UniqueIdIdentifier().setValue("urn:oid:1.2.3");
        assertEquals(Identifier.IdentifierUse.USUAL, uniqueId.getUse());
        assertTrue(UNIQUE_ID.matches(uniqueId));
        assertFalse(ENTRY_UUID.matches(uniqueId));
    }

    @Test
    public void testMatchesTypeWithoutUse() {
        var identifier = new Identifier().setType(ENTRY_UUID.toCodeableConcept());
        assertTrue(ENTRY_UUID.matches(identifier));
        assertFalse(UNIQUE_ID.matches(identifier));
    }

    /** MHD 4.2.3 and earlier only set the use. */
    @Test
    public void testMatchesLegacyUseWithoutType() {
        var identifier = new Identifier().setUse(Identifier.IdentifierUse.OFFICIAL);
        assertTrue(ENTRY_UUID.matches(identifier));
        assertFalse(UNIQUE_ID.matches(identifier));
    }

    /** The type of MHD 4.2.4 wins over a contradicting use. */
    @Test
    public void testTypeTakesPrecedenceOverUse() {
        var identifier = new Identifier()
            .setUse(Identifier.IdentifierUse.OFFICIAL)
            .setType(UNIQUE_ID.toCodeableConcept());
        assertTrue(UNIQUE_ID.matches(identifier));
        assertFalse(ENTRY_UUID.matches(identifier));
    }

    /** A type from an unrelated code system does not keep the use from being evaluated. */
    @Test
    public void testUnrelatedTypeFallsBackToUse() {
        var identifier = new Identifier()
            .setUse(Identifier.IdentifierUse.OFFICIAL)
            .setType(new CodeableConcept().addCoding(
                new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "ACSN", "Accession ID")));
        assertTrue(ENTRY_UUID.matches(identifier));
        assertFalse(UNIQUE_ID.matches(identifier));
    }

    @Test
    public void testFindsEntryUuidRegardlessOfPosition() throws Exception {
        var list = new ComprehensiveSubmissionSetList();
        var uuid = UUID.randomUUID();
        list.setSubmissionSetUniqueIdIdentifier(new Oid("1.2.58.92.23"))
            .setEntryUuidIdentifier(uuid);
        assertEquals(2, list.getIdentifier().size());
        assertTrue(list.getEntryUuidIdentifier().orElseThrow().getValue().contains(uuid.toString()));
        assertTrue(list.getUniqueIdIdentifier().orElseThrow().getValue().contains("1.2.58.92.23"));
    }

    /** Since MHD 4.2.4 both slices are restricted to one occurrence. */
    @Test
    public void testSettersReplaceRatherThanAppend() throws Exception {
        var list = new ComprehensiveSubmissionSetList();
        list.setSubmissionSetUniqueIdIdentifier(new Oid("1.2.58.92.23"))
            .setEntryUuidIdentifier(UUID.randomUUID())
            .setEntryUuidIdentifier(UUID.randomUUID())
            .setSubmissionSetUniqueIdIdentifier(new Oid("1.2.58.92.24"));
        assertEquals(1, count(list.getIdentifier(), ENTRY_UUID));
        assertEquals(1, count(list.getIdentifier(), UNIQUE_ID));

        var documentReference = new ComprehensiveDocumentReference();
        documentReference
            .setUniqueIdIdentifier(URN_IETF_RFC_3986, "urn:oid:129.6.58.92.88336")
            .setEntryUuidIdentifier(UUID.randomUUID())
            .setEntryUuidIdentifier(UUID.randomUUID());
        assertEquals(1, count(documentReference.getIdentifier(), ENTRY_UUID));
    }

    /**
     * Since MHD 4.2.4 the Unique Id goes into the identifier slice next to the MasterIdentifier, and
     * both have to carry the same value.
     */
    @Test
    public void testDocumentReferenceUniqueIdGoesToMasterIdentifierAndIdentifier() {
        var documentReference = new ComprehensiveDocumentReference()
            .setUniqueIdIdentifier(URN_IETF_RFC_3986, "urn:oid:129.6.58.92.88336");

        assertEquals("urn:oid:129.6.58.92.88336", documentReference.getMasterIdentifier().getValue());
        assertEquals(1, count(documentReference.getIdentifier(), UNIQUE_ID));
        assertEquals("urn:oid:129.6.58.92.88336",
            documentReference.getUniqueIdIdentifier().orElseThrow().getValue());

        // setting it again replaces rather than adds
        documentReference.setUniqueIdIdentifier(URN_IETF_RFC_3986, "urn:oid:129.6.58.92.88337");
        assertEquals(1, count(documentReference.getIdentifier(), UNIQUE_ID));
        assertEquals("urn:oid:129.6.58.92.88337", documentReference.getMasterIdentifier().getValue());
        assertEquals("urn:oid:129.6.58.92.88337",
            documentReference.getUniqueIdIdentifier().orElseThrow().getValue());
    }

    private static long count(List<Identifier> identifiers, MhdIdentifierType type) {
        return identifiers.stream().filter(type::matches).count();
    }
}
