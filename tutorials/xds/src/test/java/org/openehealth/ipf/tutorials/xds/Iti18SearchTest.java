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

import jakarta.activation.DataHandler;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsExcludeQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindFoldersQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindSubmissionSetsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetAssociationsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.tutorials.xds.store.ContentUtils;
import org.openehealth.ipf.tutorials.xds.store.DataStore;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Matching rules of the ITI-18 stored queries (ITI TF-2: 3.18.4.1.2.3).
 * <p>
 * The document entry registered by {@link #provide} comes from {@link SampleData}: creationTime 1981,
 * serviceStartTime 198012, serviceStopTime 198101, one author with family name "Norbi" and institution
 * "authorOrg". Its submission set was submitted in 1980; its folder was last updated when it was registered.
 *
 * @author Christian Ohr
 */
class Iti18SearchTest extends XdsServerTestBase {

    // --- time ranges --------------------------------------------------------------------------------

    @Test
    void creationTimeRange() {
        var doc = provide();
        assertEquals(1, find(doc, query -> {
            query.getCreationTime().setFrom("1980");
            query.getCreationTime().setTo("1982");
        }));
    }

    /** From is inclusive, To is exclusive (ITI TF-2: 3.18.4.1.2.3.3). */
    @Test
    void timeRangeUpperBoundIsExclusive() {
        var doc = provide();
        assertEquals(0, find(doc, query -> {
            query.getCreationTime().setFrom("1980");
            query.getCreationTime().setTo("1981");
        }));
        assertEquals(1, find(doc, query -> {
            query.getCreationTime().setFrom("1981");
            query.getCreationTime().setTo("1982");
        }));
    }

    /** A time value covers the period of its precision, so the year 1981 overlaps a range starting in June 1981. */
    @Test
    void timeRangeOverlapsLessPreciseTime() {
        var doc = provide();
        assertEquals(1, find(doc, query -> query.getCreationTime().setFrom("19810601")));
        assertEquals(0, find(doc, query -> query.getCreationTime().setFrom("1982")));
    }

    /** Entries without a service start time are not excluded by a range on it (ITI TF-2: 3.18.4.1.2.3.3.1). */
    @Test
    void serviceTimeRangeIgnoredIfAbsent() {
        var withoutServiceTimes = provide(entry -> {
            entry.setServiceStartTime((String) null);
            entry.setServiceStopTime((String) null);
        });
        assertEquals(1, find(withoutServiceTimes, query -> query.getServiceStartTime().setFrom("2000")));
        assertEquals(1, find(withoutServiceTimes, query -> query.getServiceStopTime().setTo("1900")));

        var withServiceTimes = provide();
        assertEquals(0, find(withServiceTimes, query -> query.getServiceStartTime().setFrom("2000")));
        assertEquals(1, find(withServiceTimes, query -> {
            query.getServiceStartTime().setFrom("1980");
            query.getServiceStartTime().setTo("1981");
        }));
    }

    @Test
    void submissionTimeRange() {
        var doc = provide();
        var query = new FindSubmissionSetsQuery();
        query.setPatientId(doc.getPatientId());
        query.setStatus(List.of(AvailabilityStatus.APPROVED));
        query.getSubmissionTime().setFrom("1980");
        query.getSubmissionTime().setTo("1981");
        assertEquals(1, query(query).getSubmissionSets().size());

        query.getSubmissionTime().setFrom("1981");
        query.getSubmissionTime().setTo((String) null);
        assertEquals(0, query(query).getSubmissionSets().size());
    }

    @Test
    void folderLastUpdateTimeRange() {
        var doc = provide();
        var query = new FindFoldersQuery();
        query.setPatientId(doc.getPatientId());
        query.setStatus(List.of(AvailabilityStatus.APPROVED));
        query.getLastUpdateTime().setFrom("2000");
        assertEquals(1, query(query).getFolders().size());

        query.getLastUpdateTime().setFrom((String) null);
        query.getLastUpdateTime().setTo("2000");
        assertEquals(0, query(query).getFolders().size());
    }

    // --- author person ------------------------------------------------------------------------------

    @Test
    void authorPersonWildcards() {
        var doc = provide();
        assertEquals(1, find(doc, query -> query.setAuthorPersons(List.of("%Norbi%"))));
        assertEquals(1, find(doc, query -> query.setAuthorPersons(List.of("%Norb_%"))));
        assertEquals(0, find(doc, query -> query.setAuthorPersons(List.of("Norbi"))));
    }

    /** The pattern is matched against the XCN value, whose component separator ^ is no regex anchor. */
    @Test
    void authorPersonXcnPattern() {
        var doc = provide();
        assertEquals(1, find(doc, query -> query.setAuthorPersons(List.of("%^Norbi^%"))));
    }

    @Test
    void authorPersonWithRegexCharacters() {
        var doc = provide();
        assertEquals(0, find(doc, query -> query.setAuthorPersons(List.of("%Norbi (Dr.%"))));
        assertEquals(0, find(doc, query -> query.setAuthorPersons(List.of(".*"))));
    }

    /** Only the authorPerson is matched, not institution, role, specialty or telecom. */
    @Test
    void authorPersonDoesNotMatchInstitution() {
        var doc = provide();
        assertEquals(0, find(doc, query -> query.setAuthorPersons(List.of("%authorOrg%"))));
    }

    @Test
    void excludedAuthorPerson() {
        var doc = provide();
        var query = new FindDocumentsExcludeQuery();
        query.setPatientId(doc.getPatientId());
        query.setStatus(List.of(AvailabilityStatus.APPROVED));
        query.setExcludedAuthorPersons(List.of("%^Norbi^%"));
        assertEquals(0, query(query).getDocumentEntries().size());

        query.setExcludedAuthorPersons(List.of("%authorOrg%"));
        assertEquals(1, query(query).getDocumentEntries().size());
    }

    // --- FindDocumentsExclude ------------------------------------------------------------------------

    /**
     * FindDocumentsExclude returns what FindDocuments would, minus the documents whose metadata carry a
     * value the requester named as excluded (ITI TF-2: 3.18.4.1.2.3.7.15).
     */
    @Test
    void findDocumentsExcludeQuery() {
        var patientId = registerThreeDocuments(docEntry -> {
            // one of the three gets a class code of its own, so that excluding that code has to leave
            // exactly the other two -- a filter that drops everything or nothing proves little
            if (docEntry.getUniqueId().equals("2.2.2")) {
                docEntry.setClassCode(new Code("excludedCode", null, "scheme2"));
            }
        });

        var response = excludeQuery(patientId,
                query -> query.setExcludedClassCodes(List.of(new Code("excludedCode", null, "scheme2"))));

        assertEquals(List.of("1.1.1", "3.3.3"), uniqueIds(response.getDocumentEntries()).stream().sorted().toList());
    }

    /**
     * Excluding a value that no document carries excludes nothing, which is what tells the excluding
     * parameters apart from their non-excluding counterparts: those would have returned nothing here.
     */
    @Test
    void findDocumentsExcludeQueryMatchingNoDocument() {
        var patientId = registerThreeDocuments();

        var response = excludeQuery(patientId, query -> {
            query.setExcludedClassCodes(List.of(new Code("noDocumentHasThis", null, "scheme2")));
            query.setExcludedAuthorPersons(List.of("Sieglinde%"));
        });

        assertEquals(3, response.getDocumentEntries().size());
    }

    /**
     * Excluded event codes are a query list and keep the AND/OR semantics of the including ones: a
     * document is dropped when it matches the list, so it survives as soon as one of the ANDed groups
     * names none of its event codes.
     */
    @Test
    void findDocumentsExcludeQueryWithEventCodes() {
        var patientId = registerThreeDocuments();

        var matchingEventCodes = new QueryList<Code>();
        matchingEventCodes.getOuterList().add(List.of(new Code("code9", null, "scheme9")));
        assertEquals(0, excludeQuery(patientId, query -> query.setExcludedEventCodes(matchingEventCodes))
                .getDocumentEntries().size());

        // the second group matches no event code of the documents, so the list as a whole does not
        // match and nothing is excluded
        var partiallyMatchingEventCodes = new QueryList<Code>();
        partiallyMatchingEventCodes.getOuterList().add(List.of(new Code("code9", null, "scheme9")));
        partiallyMatchingEventCodes.getOuterList().add(List.of(new Code("otherCode", null, "scheme9")));
        assertEquals(3, excludeQuery(patientId, query ->
            query.setExcludedEventCodes(partiallyMatchingEventCodes)).getDocumentEntries().size());
    }

    // --- document entry type ------------------------------------------------------------------------

    /** Without $XDSDocumentEntryType only stable entries are returned (ITI TF-2: 3.18.4.1.2.3.6.2). */
    @Test
    void documentEntryTypeDefaultsToStable() {
        var doc = provide();
        storeOnDemandEntry(doc);

        assertEquals(List.of(DocumentEntryType.STABLE),
                findDocuments(doc.getPatientId(), query -> {}).stream()
                    .map(DocumentEntry::getType)
                    .toList());
        assertEquals(1, find(doc, query -> query.setDocumentEntryTypes(List.of(DocumentEntryType.ON_DEMAND))));
        assertEquals(2, find(doc, query ->
                query.setDocumentEntryTypes(List.of(DocumentEntryType.STABLE, DocumentEntryType.ON_DEMAND))));
    }

    // --- duplicates ---------------------------------------------------------------------------------

    /** An association whose source and target are both among the requested UUIDs is returned once. */
    @Test
    void getAssociationsReturnsEachAssociationOnce() {
        var doc = provide();
        var docUuid = findDocuments(doc.getPatientId(), query -> {})
            .get(0).getEntryUuid();
        var setsQuery = new FindSubmissionSetsQuery();
        setsQuery.setPatientId(doc.getPatientId());
        setsQuery.setStatus(List.of(AvailabilityStatus.APPROVED));
        var setUuid = query(setsQuery).getSubmissionSets().get(0).getEntryUuid();

        var query = new GetAssociationsQuery();
        query.setUuids(List.of(setUuid, docUuid));
        var uuids = query(query).getAssociations().stream()
            .map(Association::getEntryUuid)
            .toList();

        assertEquals(uuids.size(), new HashSet<>(uuids).size(), "an association was returned twice: " + uuids);
        assertTrue(uuids.size() >= 3, "expected at least the 3 associations of the submission: " + uuids);
    }

    // --- helpers ------------------------------------------------------------------------------------

    private DocumentEntry provide() {
        return provide(entry -> {});
    }

    private DocumentEntry provide(Consumer<DocumentEntry> customize) {
        var provide = SampleData.createProvideAndRegisterDocumentSet();
        var document = provide.getDocuments().get(0);
        var docEntry = document.getDocumentEntry();
        docEntry.getPatientId().setId(UUID.randomUUID().toString());
        docEntry.setUniqueId("1.2.3." + System.nanoTime());
        docEntry.setHash(ContentUtils.sha1(document.getContent(DataHandler.class)));
        docEntry.setSize(ContentUtils.size(document.getContent(DataHandler.class)));
        customize.accept(docEntry);
        var response = send(iti41, provide, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        return docEntry;
    }

    /** On-demand entries are registered by ITI-61, which this registry does not offer, so go to the store. */
    private static void storeOnDemandEntry(DocumentEntry stable) {
        var onDemand = SampleData.createDocumentEntry(stable.getPatientId());
        onDemand.setType(DocumentEntryType.ON_DEMAND);
        onDemand.setEntryUuid("urn:uuid:" + UUID.randomUUID());
        onDemand.setUniqueId("1.2.3." + System.nanoTime());
        onDemand.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        // on-demand entries have no creation time, hash, size or legal authenticator
        onDemand.setCreationTime((String) null);
        onDemand.setHash(null);
        onDemand.setSize(null);
        onDemand.setLegalAuthenticator(null);
        appContext.getBean("dataStore", DataStore.class).store(onDemand);
    }

    private QueryResponse excludeQuery(Identifiable patientId, Consumer<FindDocumentsExcludeQuery> parameters) {
        var query = new FindDocumentsExcludeQuery();
        query.setPatientId(patientId);
        query.setStatus(List.of(AvailabilityStatus.APPROVED));
        parameters.accept(query);
        return query(query);
    }

    private static List<String> uniqueIds(List<DocumentEntry> entries) {
        return entries.stream().map(DocumentEntry::getUniqueId).toList();
    }

    /**
     * @return how many approved documents of the patient of {@code doc} a FindDocuments query with the
     *         given further parameters finds
     */
    private int find(DocumentEntry doc, Consumer<FindDocumentsQuery> parameters) {
        return findDocuments(doc.getPatientId(), parameters).size();
    }
}
