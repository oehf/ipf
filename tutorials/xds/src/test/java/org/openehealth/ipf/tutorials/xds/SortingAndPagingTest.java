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
package org.openehealth.ipf.tutorials.xds;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindFoldersQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortKey;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrder;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The ordering and paging extension of ITI-18. ITI-18 defines neither, so the registry of this tutorial
 * serves them as a bilaterally agreed extension: the order arrives in the $XDSSortOrder slot, the window
 * in the ebRS pagination attributes, and the registry reports back what it applied -- without that a
 * consumer could not tell an applied order from an ignored one.
 *
 * @author Christian Ohr
 */
class SortingAndPagingTest extends XdsServerTestBase {

    @Test
    void sortedQuery() {
        var patientId = registerThreeDocuments();

        var response = findDocumentsResponse(patientId,
                new SortOrder(SortKey.descending("$XDSDocumentEntryUniqueId")), null, null);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(List.of("3.3.3", "2.2.2", "1.1.1"), documentUniqueIds(response));
        assertEquals(1, response.getHonoredSortOrder().getKeys().size());
        assertEquals(SortKey.Direction.DESCENDING, response.getHonoredSortOrder().getKeys().get(0).getDirection());
    }

    /**
     * Folders and submission sets carry their own attribute names, so ordering them takes its own
     * comparator: the same concept is $XDSDocumentEntryUniqueId for a document entry and
     * $XDSFolderUniqueId for a folder.
     */
    @Test
    void sortedFolderQuery() {
        var patientId = registerThreeDocuments();
        var sortOrder = new SortOrder(SortKey.descending("$XDSFolderUniqueId"));

        var response = findFolders(patientId, sortOrder, null, null);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        var uniqueIds = response.getFolders().stream().map(Folder::getUniqueId).toList();
        assertEquals(uniqueIds.stream().sorted(Comparator.reverseOrder()).toList(), uniqueIds,
                "the folders are not in descending order");
        assertEquals(3, uniqueIds.size());
        assertEquals(sortOrder, response.getHonoredSortOrder());
    }

    /**
     * A document entry order names no folder attribute, so the folders are left as they are -- and the
     * registry must not claim to have honored anything.
     */
    @Test
    void aDocumentOrderDoesNotReorderFolders() {
        var patientId = registerThreeDocuments();

        var response = findFolders(patientId, new SortOrder(SortKey.descending("$XDSDocumentEntryUniqueId")), null, null);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(3, response.getFolders().size());
        assertNull(response.getHonoredSortOrder());
    }

    /**
     * The window is asked for with the ebRS pagination attributes and answered with totalResultCount,
     * which is what lets a consumer show a result count it never fetched.
     */
    @Test
    void pagedQuery() {
        var patientId = registerThreeDocuments();

        var response = findDocumentsResponse(patientId, ascendingByUniqueId(), 1, 1);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(List.of("2.2.2"), documentUniqueIds(response));
        assertEquals(3, response.getTotalResultCount());
        assertEquals(1, response.getStartIndex());
    }

    /**
     * A query asking for neither must behave exactly as it did before the extension existed.
     */
    @Test
    void unsortedQueryReportsNothing() {
        var patientId = registerThreeDocuments();

        var response = findDocumentsResponse(patientId, null, null, null);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(3, response.getDocumentEntries().size());
        assertNull(response.getHonoredSortOrder());
        assertNull(response.getTotalResultCount());
        assertNull(response.getStartIndex());
    }

    /**
     * A window starting past the end is refused. An empty page would be indistinguishable from a query
     * that simply matched nothing, and the two mean very different things to whoever is paging.
     */
    @Test
    void aStartIndexBeyondTheEndIsRejected() {
        var patientId = registerThreeDocuments();

        assertEquals(Status.FAILURE, findDocumentsResponse(patientId, ascendingByUniqueId(), 3, 2).getStatus());
        assertEquals(Status.FAILURE, findDocumentsResponse(patientId, ascendingByUniqueId(), 1000, 2).getStatus());

        // the last page is still fine -- one entry left at offset 2 of three
        var lastPage = findDocumentsResponse(patientId, ascendingByUniqueId(), 2, 2);
        assertEquals(Status.SUCCESS, lastPage.getStatus(), lastPage.toString());
        assertEquals(List.of("3.3.3"), documentUniqueIds(lastPage));
    }

    /**
     * maxResults comes from the consumer and may be anything up to Integer.MAX_VALUE, which as a window
     * means "everything from startIndex on" -- and must not overflow where the end of the window is
     * computed.
     */
    @Test
    void aHugeWindowReturnsTheRest() {
        var patientId = registerThreeDocuments();

        var response = findDocumentsResponse(patientId, ascendingByUniqueId(), 1, Integer.MAX_VALUE);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(List.of("2.2.2", "3.3.3"), documentUniqueIds(response));
    }

    /**
     * Offset zero is exempt: a search matching nothing is a legitimate empty answer, not a window out of
     * range, and must not be turned into an error.
     */
    @Test
    void anEmptyResultAtOffsetZeroIsNotAnError() {
        var response = findDocumentsResponse(patient(UUID.randomUUID().toString()), null, 0, 10);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(0, response.getDocumentEntries().size());
    }

    /**
     * Folders are searched for, ordered and paged like documents are -- the window applies to whichever
     * kind the query asked about, not to document entries by default.
     */
    @Test
    void aPagedFolderQuery() {
        var patientId = registerThreeDocuments();

        var response = findFolders(patientId,
                new SortOrder(SortKey.ascending("$XDSFolderUniqueId")).withStableTiebreaker(), 1, 1);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(1, response.getFolders().size());
        assertEquals(3, response.getTotalResultCount());
        assertEquals(1, response.getStartIndex());
    }

    private QueryResponse findFolders(Identifiable patientId, SortOrder sortOrder, Integer startIndex, Integer maxResults) {
        var query = new FindFoldersQuery();
        query.setPatientId(patientId);
        query.setStatus(List.of(AvailabilityStatus.APPROVED));
        query.setSortOrder(sortOrder);
        var request = leafClassRequest(query);
        request.setStartIndex(startIndex);
        request.setMaxResults(maxResults);
        return send(iti18, request, QueryResponse.class);
    }

    private static SortOrder ascendingByUniqueId() {
        return new SortOrder(SortKey.ascending("$XDSDocumentEntryUniqueId")).withStableTiebreaker();
    }

    private static List<String> documentUniqueIds(QueryResponse response) {
        return response.getDocumentEntries().stream().map(DocumentEntry::getUniqueId).toList();
    }
}
