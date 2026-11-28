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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindFoldersQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortKey;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrder;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.tutorials.xds.authz.CachingDecisionPoint;
import org.openehealth.ipf.tutorials.xds.authz.DemoDecisionPoint;
import org.openehealth.ipf.tutorials.xds.store.DataStore;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Enforcement of the ITI-18 result set against the decision point, through the running registry: how
 * pages are filled when entries are denied, and how much deciding that costs.
 *
 * @author Christian Ohr
 */
class AuthorizationTest extends XdsServerTestBase {

    private static int seedCount = 0;

    /**
     * The window indexes the <em>authorized</em> set, not the match set, so a page stays full even when
     * entries inside the scanned band are denied. If it indexed the match set, a consumer asking for
     * twenty would receive however many of those twenty survived enforcement -- and a short page is the
     * signal a consumer without a total uses to detect the end of the result set, so every page would
     * look like the last one.
     */
    @Test
    void aDeniedBandDoesNotShortenThePage() {
        var patientId = seedDocuments(60).get(0).getPatientId();
        denyEveryThird();

        var response = findDocumentsResponse(patientId, ascendingByUniqueId(), 0, 20);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(20, response.getDocumentEntries().size());
        assertEquals(permittedUniqueIds(patientId).subList(0, 20), documentUniqueIds(response));
    }

    /**
     * Consecutive windows are contiguous in the authorized set: the second page starts where the first
     * one ended, not twenty <em>matches</em> in. Enforcement is invisible in the shape of the answer,
     * which is what lets a consumer walk the result set at all.
     */
    @Test
    void startIndexCountsAuthorizedEntries() {
        var patientId = seedDocuments(60).get(0).getPatientId();
        denyEveryThird();
        var permitted = permittedUniqueIds(patientId);

        var first = findDocumentsResponse(patientId, ascendingByUniqueId(), 0, 10);
        var second = findDocumentsResponse(patientId, ascendingByUniqueId(), 10, 10);

        assertEquals(permitted.subList(0, 10), documentUniqueIds(first));
        assertEquals(permitted.subList(10, 20), documentUniqueIds(second));
        // an echoed start index of zero arrives as absent: ebRS defaults the attribute to 0, so the
        // two are indistinguishable on the wire, exactly as on the request side
        assertNull(first.getStartIndex());
        assertEquals(10, second.getStartIndex());
    }

    /**
     * What the lazy fill is for: the decisions a page costs are bounded by the page, not by the match
     * set. Enforcing everything and then slicing would take sixty decisions to return twenty, and take
     * them again for the next page.
     */
    @Test
    void theWindowBoundsTheDecisions() {
        var patientId = seedDocuments(240).get(0).getPatientId();
        denyEveryThird();
        var decisionPoint = decisionPoint();
        decisionPoint.reset();

        var response = findDocumentsResponse(patientId, ascendingByUniqueId(), 0, 20);

        assertEquals(20, response.getDocumentEntries().size());
        // the first prefetch is the window and comes up short of it, so the loop reaches for the next
        // rung of the ladder -- and stops there, far short of the two hundred and forty a registry that
        // enforced the whole match set would have paid, and would have paid again for the next page
        assertTrue(decisionPoint.getDecisions() <= 100,
                "decided " + decisionPoint.getDecisions() + " entries of a match set of 240 to fill a page of 20");
        assertTrue(decisionPoint.getDecisions() >= 20, "a page of twenty cannot cost fewer than twenty decisions");
        assertTrue(decisionPoint.getCalls() <= 3,
                "took " + decisionPoint.getCalls() + " round trips to the decision point");
    }

    /**
     * Permitting everything is the cheap case the block estimate starts from: one batched call, and no
     * overshoot beyond the window.
     */
    @Test
    void permittingEverythingCostsOneCall() {
        var patientId = seedDocuments(60).get(0).getPatientId();
        var decisionPoint = decisionPoint();
        decisionPoint.reset();

        var response = findDocumentsResponse(patientId, ascendingByUniqueId(), 0, 20);

        assertEquals(20, response.getDocumentEntries().size());
        assertEquals(1, decisionPoint.getCalls());
        assertEquals(20, decisionPoint.getDecisions());
    }

    /**
     * Above the threshold the total is omitted: the only honest total is the size of the authorized set,
     * and producing it means deciding the entire match set to answer one window. Reporting the size of
     * the match set instead would tell the consumer exactly how many documents are being withheld.
     */
    @Test
    void theTotalIsOmittedAboveTheThreshold() {
        var patientId = seedDocuments(60).get(0).getPatientId();
        denyEveryThird();

        var response = findDocumentsResponse(patientId, ascendingByUniqueId(), 0, 20);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertNull(response.getTotalResultCount());
        assertEquals(20, response.getDocumentEntries().size());
    }

    /**
     * Below the threshold deciding the whole match set is cheap regardless, so the total can be reported
     * -- and it is the size of the authorized set, not of the match set.
     */
    @Test
    void theTotalBelowTheThresholdCountsAuthorizedEntries() {
        var patientId = seedDocuments(6).get(0).getPatientId();
        denyEveryThird();

        var response = findDocumentsResponse(patientId, ascendingByUniqueId(), 0, 10);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(4, response.getTotalResultCount());
        assertEquals(4, response.getDocumentEntries().size());
    }

    /**
     * Above the threshold the lazy fill sizes the page by maxResults, which comes from the consumer: a
     * window of Integer.MAX_VALUE must neither allocate that much up front nor overflow with the start
     * index added.
     */
    @Test
    void aHugeWindowAboveTheThresholdReturnsTheRest() {
        var patientId = seedDocuments(60).get(0).getPatientId();
        denyEveryThird();
        var permitted = permittedUniqueIds(patientId);

        var response = findDocumentsResponse(patientId, ascendingByUniqueId(), 1, Integer.MAX_VALUE);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(permitted.subList(1, permitted.size()), documentUniqueIds(response));
    }

    /**
     * Folders are enforced like documents are: a folder the requestor may not see is missing from the
     * result, and the page is filled from what is left.
     */
    @Test
    void foldersAreEnforced() {
        var patientId = registerThreeDocuments();
        var denied = findFolders(patientId).getFolders().get(0).getUniqueId();
        enforce(entry -> !entry.getUniqueId().equals(denied));

        var response = findFolders(patientId);

        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        assertEquals(2, response.getFolders().size());
        assertFalse(response.getFolders().stream().map(Folder::getUniqueId).anyMatch(denied::equals));
    }

    /**
     * The GetDocuments call that follows a two-phase query is not pageable -- the requestor names the
     * UUIDs -- but it is still enforced against the same policy, or the second phase becomes a way
     * around the first.
     */
    @Test
    void theSecondPhaseIsEnforced() {
        var entries = seedDocuments(6);
        denyEveryThird();

        var query = new GetDocumentsQuery();
        query.setUuids(entries.stream().map(DocumentEntry::getEntryUuid).toList());
        var response = query(query);

        assertEquals(permittedUniqueIds(entries.get(0).getPatientId()),
                documentUniqueIds(response).stream().sorted().toList());
    }

    /**
     * Leaves the decision point as the other tests expect to find it: permitting everything, and with
     * nothing remembered. Replacing the rule is a policy change, and a policy change has to bump the
     * epoch -- a cache that outlived one would answer from the policy that was in force before it.
     */
    @AfterEach
    void restorePermitAll() {
        decisionPoint().permitAll();
        decisionPoint().reset();
        appContext.getBean("cachingDecisionPoint", CachingDecisionPoint.class).invalidate();
    }

    private static DemoDecisionPoint decisionPoint() {
        return appContext.getBean("decisionPoint", DemoDecisionPoint.class);
    }

    /**
     * Denies every third document, by the numeric tail of its unique id -- a stand-in for a consent rule
     * that has to be evaluated per document.
     */
    private static void denyEveryThird() {
        enforce(entry -> tailOf(entry.getUniqueId()) % 3 != 0);
    }

    /**
     * Installs a rule, and forgets what was decided under the previous one. Changing the policy is
     * exactly the event the verdict cache has to be told about -- without that, this registry keeps
     * answering from the consent that was in force before.
     */
    private static void enforce(Predicate<XDSMetaClass> rule) {
        decisionPoint().setRule(rule);
        appContext.getBean("cachingDecisionPoint", CachingDecisionPoint.class).invalidate();
    }

    private static int tailOf(String uniqueId) {
        return Integer.parseInt(uniqueId.substring(uniqueId.lastIndexOf('.') + 1));
    }

    /**
     * The unique ids {@link #denyEveryThird()} leaves, in ascending order -- what the authorized set of a
     * seeded patient looks like.
     */
    private static List<String> permittedUniqueIds(Identifiable patientId) {
        return appContext.getBean("dataStore", DataStore.class).getEntries().stream()
                .filter(entry -> entry instanceof DocumentEntry documentEntry
                        && documentEntry.getPatientId().equals(patientId))
                .map(entry -> ((DocumentEntry) entry).getUniqueId())
                .filter(uniqueId -> tailOf(uniqueId) % 3 != 0)
                .sorted()
                .toList();
    }

    /**
     * Puts document entries into the store directly rather than registering them over ITI-42: the point
     * of these tests is the query side, and a match set has to be big enough for the lazy fill to have
     * something to be lazy about.
     */
    private static List<DocumentEntry> seedDocuments(int count) {
        var store = appContext.getBean("dataStore", DataStore.class);
        var patientId = patient(UUID.randomUUID().toString());
        var prefix = "1.2.3." + (++seedCount) + ".";

        return IntStream.rangeClosed(1, count).mapToObj(i -> {
            var entry = SampleData.createRegisterDocumentSet().getDocumentEntries().get(0);
            entry.setEntryUuid("urn:uuid:" + UUID.randomUUID());
            // three digits without a leading zero, so that the lexicographic order of the unique ids is
            // their numeric order and the expected page is easy to state
            entry.setUniqueId(prefix + (100 + i));
            entry.setPatientId(patientId);
            entry.setAvailabilityStatus(AvailabilityStatus.APPROVED);
            store.store(entry);
            return entry;
        }).toList();
    }

    private QueryResponse findFolders(Identifiable patientId) {
        var query = new FindFoldersQuery();
        query.setPatientId(patientId);
        query.setStatus(List.of(AvailabilityStatus.APPROVED));
        return send(iti18, leafClassRequest(query), QueryResponse.class);
    }

    private static SortOrder ascendingByUniqueId() {
        return new SortOrder(SortKey.ascending("$XDSDocumentEntryUniqueId")).withStableTiebreaker();
    }

    private static List<String> documentUniqueIds(QueryResponse response) {
        return response.getDocumentEntries().stream().map(DocumentEntry::getUniqueId).toList();
    }
}
