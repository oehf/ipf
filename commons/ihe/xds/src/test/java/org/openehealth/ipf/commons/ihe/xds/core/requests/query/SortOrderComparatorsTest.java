/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The comparator is what a registry built on IPF applies to its result set, so it has to mean the same
 * thing as the order a consumer asked for.
 *
 * @author Christian Ohr
 */
public class SortOrderComparatorsTest {

    private static final String CREATION_TIME = "$XDSDocumentEntryCreationTime";
    private static final String ENTRY_UUID = "$XDSDocumentEntryEntryUUID";
    private static final String SIZE = "$XDSDocumentEntrySize";

    @Test
    public void testItSortsByOneKey() {
        var entries = entries("c", "a", "b");

        entries.sort(SortOrderComparators.documentEntryComparator(
            new SortOrder(SortKey.ascending(ENTRY_UUID))));

        assertEquals(List.of("a", "b", "c"), uuids(entries));
    }

    @Test
    public void testItSortsDescending() {
        var entries = entries("c", "a", "b");

        entries.sort(SortOrderComparators.documentEntryComparator(
            new SortOrder(SortKey.descending(ENTRY_UUID))));

        assertEquals(List.of("c", "b", "a"), uuids(entries));
    }

    /**
     * The later keys only decide where the earlier ones tie -- which is the whole point of appending the
     * entry UUID as a tiebreaker.
     */
    @Test
    public void testLaterKeysBreakTiesOfEarlierOnes() {
        var entries = List.of(
            entry("b", 10L), entry("a", 20L), entry("c", 10L));

        var sorted = entries.stream()
            .sorted(SortOrderComparators.documentEntryComparator(
                new SortOrder(SortKey.ascending(SIZE)).withStableTiebreaker()))
            .toList();

        assertEquals(List.of("b", "c", "a"), uuids(sorted));
    }

    /**
     * Entries lacking the attribute go last whichever way the sort runs. Reversing the comparator as a
     * whole would put them first on a descending sort, ahead of everything the requester asked to see.
     */
    @Test
    public void testMissingValuesSortLastInBothDirections() {
        var entries = List.of(entry("with", 10L), entry("without", null));

        assertEquals(List.of("with", "without"), uuids(entries.stream()
            .sorted(SortOrderComparators.documentEntryComparator(new SortOrder(SortKey.ascending(SIZE))))
            .toList()));

        assertEquals(List.of("with", "without"), uuids(entries.stream()
            .sorted(SortOrderComparators.documentEntryComparator(new SortOrder(SortKey.descending(SIZE))))
            .toList()));
    }

    @Test
    public void testItSortsByTime() {
        var early = entry("early", null);
        early.setCreationTime(Timestamp.fromHL7("20240101000000"));
        var late = entry("late", null);
        late.setCreationTime(Timestamp.fromHL7("20250101000000"));

        var sorted = List.of(late, early).stream()
            .sorted(SortOrderComparators.documentEntryComparator(
                new SortOrder(SortKey.ascending(CREATION_TIME))))
            .toList();

        assertEquals(List.of("early", "late"), uuids(sorted));
    }

    /**
     * An attribute the helper does not know is skipped, the way a registry ignores a query parameter it
     * does not understand -- the remaining keys still apply.
     */
    @Test
    public void testAnUnknownAttributeIsSkipped() {
        var entries = entries("c", "a", "b");

        entries.sort(SortOrderComparators.documentEntryComparator(new SortOrder(
            SortKey.ascending("$SomethingTheRegistryNeverHeardOf"),
            SortKey.ascending(ENTRY_UUID))));

        assertEquals(List.of("a", "b", "c"), uuids(entries));
    }

    /**
     * An order made entirely of unknown attributes leaves nothing to sort by, and saying so is more
     * useful than handing back a comparator that does nothing.
     */
    @Test
    public void testAnEntirelyUnknownOrderYieldsNoComparator() {
        assertNull(SortOrderComparators.documentEntryComparator(
            new SortOrder(SortKey.ascending("$SomethingTheRegistryNeverHeardOf"))));
        assertNull(SortOrderComparators.documentEntryComparator(null));
        assertNull(SortOrderComparators.documentEntryComparator(new SortOrder()));
    }

    /**
     * A registry with extension metadata teaches the helper about it rather than reimplementing the
     * whole comparator.
     */
    @Test
    public void testAdditionalAttributesCanBeRegistered() {
        var extra = Map.of("$ourOwnAttribute", SortOrderComparators.sortableBy(
            (DocumentEntry documentEntry) -> documentEntry.getExtraMetadata().get("urn:ours").get(0)));

        var first = entry("first", null);
        first.setExtraMetadata(Map.of("urn:ours", List.of("1")));
        var second = entry("second", null);
        second.setExtraMetadata(Map.of("urn:ours", List.of("2")));

        var sorted = List.of(second, first).stream()
            .sorted(SortOrderComparators.documentEntryComparator(
                new SortOrder(SortKey.ascending("$ourOwnAttribute")), extra))
            .toList();

        assertEquals(List.of("first", "second"), uuids(sorted));
    }

    // ------------------------------------------------------------------- folders and submission sets

    @Test
    public void testItSortsFolders() {
        var folders = List.of(folder("c"), folder("a"), folder("b"));

        var sorted = folders.stream()
            .sorted(SortOrderComparators.folderComparator(
                new SortOrder(SortKey.ascending("$XDSFolderEntryUUID"))))
            .toList();

        assertEquals(List.of("a", "b", "c"), sorted.stream().map(Folder::getEntryUuid).toList());
    }

    @Test
    public void testItSortsSubmissionSets() {
        var first = submissionSet("first");
        first.setSourceId("1.1");
        var second = submissionSet("second");
        second.setSourceId("2.2");

        var sorted = List.of(second, first).stream()
            .sorted(SortOrderComparators.submissionSetComparator(
                new SortOrder(SortKey.ascending("$XDSSubmissionSetSourceId"))))
            .toList();

        assertEquals(List.of("first", "second"), sorted.stream().map(SubmissionSet::getEntryUuid).toList());
    }

    /**
     * The same concept has a different attribute name per object type, so a document entry order must
     * not accidentally apply to folders. Otherwise a registry sorting a mixed result set would order one
     * kind by the requester's intent and the other by chance.
     */
    @Test
    public void testAttributeNamesAreScopedToTheirObjectType() {
        assertNull(SortOrderComparators.folderComparator(
                new SortOrder(SortKey.ascending("$XDSDocumentEntryUniqueId"))),
            "a document entry attribute must not order folders");
        assertNull(SortOrderComparators.documentEntryComparator(
                new SortOrder(SortKey.ascending("$XDSFolderUniqueId"))),
            "a folder attribute must not order document entries");
    }

    @Test
    public void testTheSupportedAttributesAreDiscoverable() {
        var documentEntry = SortOrderComparators.supportedDocumentEntryAttributes();
        assertTrue(documentEntry.contains(CREATION_TIME));
        assertTrue(documentEntry.contains(ENTRY_UUID));
        assertTrue(documentEntry.contains("$XDSDocumentEntryAuthorPerson"));

        // the attributes every XDS object has, under each object type's own prefix
        assertTrue(SortOrderComparators.supportedFolderAttributes().contains("$XDSFolderUniqueId"));
        assertTrue(SortOrderComparators.supportedFolderAttributes().contains("$XDSFolderLastUpdateTime"));
        assertTrue(SortOrderComparators.supportedSubmissionSetAttributes().contains("$XDSSubmissionSetUniqueId"));
        assertTrue(SortOrderComparators.supportedSubmissionSetAttributes().contains("$XDSSubmissionSetSubmissionTime"));
    }

    // ------------------------------------------------------------------------------------ helpers

    private static java.util.ArrayList<DocumentEntry> entries(String... uuids) {
        var entries = new java.util.ArrayList<DocumentEntry>();
        for (var uuid : uuids) {
            entries.add(entry(uuid, null));
        }
        return entries;
    }

    private static DocumentEntry entry(String uuid, Long size) {
        var entry = new DocumentEntry();
        entry.setEntryUuid(uuid);
        entry.setSize(size);
        return entry;
    }

    private static Folder folder(String uuid) {
        var folder = new Folder();
        folder.setEntryUuid(uuid);
        return folder;
    }

    private static SubmissionSet submissionSet(String uuid) {
        var submissionSet = new SubmissionSet();
        submissionSet.setEntryUuid(uuid);
        return submissionSet;
    }

    private static List<String> uuids(List<DocumentEntry> entries) {
        return entries.stream().map(DocumentEntry::getEntryUuid).toList();
    }

}
