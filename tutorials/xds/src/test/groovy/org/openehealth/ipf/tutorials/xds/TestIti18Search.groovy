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
package org.openehealth.ipf.tutorials.xds

import jakarta.activation.DataHandler
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * Matching rules of the ITI-18 stored queries (ITI TF-2: 3.18.4.1.2.3).
 * <p>
 * The document entry registered by {@link #provide} comes from {@link SampleData}: creationTime 1981,
 * serviceStartTime 198012, serviceStopTime 198101, one author with family name "Norbi" and institution
 * "authorOrg". Its submission set was submitted in 1980; its folder was last updated when it was registered.
 */
class TestIti18Search extends StandardTestContainer {
    def ITI18 = "xds-iti18://localhost:${port}/xds-iti18"
    def ITI41 = "xds-iti41://localhost:${port}/xds-iti41"

    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), 'context.xml', false)
    }

    // --- time ranges --------------------------------------------------------------------------------

    @Test
    void testCreationTimeRange() {
        def doc = provide()
        assertEquals(1, findDocuments(doc) { it.creationTime.from = '1980'; it.creationTime.to = '1982' }.size())
    }

    /** From is inclusive, To is exclusive (ITI TF-2: 3.18.4.1.2.3.3). */
    @Test
    void testTimeRangeUpperBoundIsExclusive() {
        def doc = provide()
        assertEquals(0, findDocuments(doc) { it.creationTime.from = '1980'; it.creationTime.to = '1981' }.size())
        assertEquals(1, findDocuments(doc) { it.creationTime.from = '1981'; it.creationTime.to = '1982' }.size())
    }

    /** A time value covers the period of its precision, so the year 1981 overlaps a range starting in June 1981. */
    @Test
    void testTimeRangeOverlapsLessPreciseTime() {
        def doc = provide()
        assertEquals(1, findDocuments(doc) { it.creationTime.from = '19810601' }.size())
        assertEquals(0, findDocuments(doc) { it.creationTime.from = '1982' }.size())
    }

    /** Entries without a service start time are not excluded by a range on it (ITI TF-2: 3.18.4.1.2.3.3.1). */
    @Test
    void testServiceTimeRangeIgnoredIfAbsent() {
        def withoutServiceTimes = provide { it.setServiceStartTime((String) null); it.setServiceStopTime((String) null) }
        assertEquals(1, findDocuments(withoutServiceTimes) { it.serviceStartTime.from = '2000' }.size())
        assertEquals(1, findDocuments(withoutServiceTimes) { it.serviceStopTime.to = '1900' }.size())

        def withServiceTimes = provide()
        assertEquals(0, findDocuments(withServiceTimes) { it.serviceStartTime.from = '2000' }.size())
        assertEquals(1, findDocuments(withServiceTimes) { it.serviceStartTime.from = '1980'; it.serviceStartTime.to = '1981' }.size())
    }

    @Test
    void testSubmissionTimeRange() {
        def doc = provide()
        def query = new FindSubmissionSetsQuery()
        query.patientId = doc.patientId
        query.status = [AvailabilityStatus.APPROVED]
        query.submissionTime.from = '1980'
        query.submissionTime.to = '1981'
        assertEquals(1, runQuery(query).submissionSets.size())

        query.submissionTime.from = '1981'
        query.submissionTime.setTo((String) null)
        assertEquals(0, runQuery(query).submissionSets.size())
    }

    @Test
    void testFolderLastUpdateTimeRange() {
        def doc = provide()
        def query = new FindFoldersQuery()
        query.patientId = doc.patientId
        query.status = [AvailabilityStatus.APPROVED]
        query.lastUpdateTime.from = '2000'
        assertEquals(1, runQuery(query).folders.size())

        query.lastUpdateTime.setFrom((String) null)
        query.lastUpdateTime.to = '2000'
        assertEquals(0, runQuery(query).folders.size())
    }

    // --- author person ------------------------------------------------------------------------------

    @Test
    void testAuthorPersonWildcards() {
        def doc = provide()
        assertEquals(1, findDocuments(doc) { it.authorPersons = ['%Norbi%'] }.size())
        assertEquals(1, findDocuments(doc) { it.authorPersons = ['%Norb_%'] }.size())
        assertEquals(0, findDocuments(doc) { it.authorPersons = ['Norbi'] }.size())
    }

    /** The pattern is matched against the XCN value, whose component separator ^ is no regex anchor. */
    @Test
    void testAuthorPersonXcnPattern() {
        def doc = provide()
        assertEquals(1, findDocuments(doc) { it.authorPersons = ['%^Norbi^%'] }.size())
    }

    @Test
    void testAuthorPersonWithRegexCharacters() {
        def doc = provide()
        assertEquals(0, findDocuments(doc) { it.authorPersons = ['%Norbi (Dr.%'] }.size())
        assertEquals(0, findDocuments(doc) { it.authorPersons = ['.*'] }.size())
    }

    /** Only the authorPerson is matched, not institution, role, specialty or telecom. */
    @Test
    void testAuthorPersonDoesNotMatchInstitution() {
        def doc = provide()
        assertEquals(0, findDocuments(doc) { it.authorPersons = ['%authorOrg%'] }.size())
    }

    @Test
    void testExcludedAuthorPerson() {
        def doc = provide()
        def query = new FindDocumentsExcludeQuery()
        query.patientId = doc.patientId
        query.status = [AvailabilityStatus.APPROVED]
        query.excludedAuthorPersons = ['%^Norbi^%']
        assertEquals(0, runQuery(query).documentEntries.size())

        query.excludedAuthorPersons = ['%authorOrg%']
        assertEquals(1, runQuery(query).documentEntries.size())
    }

    // --- document entry type ------------------------------------------------------------------------

    /** Without $XDSDocumentEntryType only stable entries are returned (ITI TF-2: 3.18.4.1.2.3.6.2). */
    @Test
    void testDocumentEntryTypeDefaultsToStable() {
        def doc = provide()
        storeOnDemandEntry(doc)

        assertEquals([DocumentEntryType.STABLE], findDocuments(doc).collect { it.type })
        assertEquals(1, findDocuments(doc) { it.documentEntryTypes = [DocumentEntryType.ON_DEMAND] }.size())
        assertEquals(2, findDocuments(doc) {
            it.documentEntryTypes = [DocumentEntryType.STABLE, DocumentEntryType.ON_DEMAND]
        }.size())
    }

    // --- duplicates ---------------------------------------------------------------------------------

    /** An association whose source and target are both among the requested UUIDs is returned once. */
    @Test
    void testGetAssociationsReturnsEachAssociationOnce() {
        def doc = provide()
        def docUuid = findDocuments(doc)[0].entryUuid
        def setsQuery = new FindSubmissionSetsQuery()
        setsQuery.patientId = doc.patientId
        setsQuery.status = [AvailabilityStatus.APPROVED]
        def setUuid = runQuery(setsQuery).submissionSets[0].entryUuid

        def query = new GetAssociationsQuery()
        query.uuids = [setUuid, docUuid]
        def uuids = runQuery(query).associations*.entryUuid

        assertEquals(uuids.unique(false), uuids)
        assertEquals(true, uuids.size() >= 3, "expected at least the 3 associations of the submission: $uuids")
    }

    // --- helpers ------------------------------------------------------------------------------------

    private DocumentEntry provide(Closure customize = {}) {
        def provide = SampleData.createProvideAndRegisterDocumentSet()
        def docEntry = provide.documents[0].documentEntry
        docEntry.patientId.id = UUID.randomUUID().toString()
        docEntry.uniqueId = '1.2.3.' + System.nanoTime()
        docEntry.hash = ContentUtils.sha1(provide.documents[0].getContent(DataHandler))
        docEntry.size = ContentUtils.size(provide.documents[0].getContent(DataHandler))
        customize(docEntry)
        def response = send(ITI41, provide, Response.class)
        assertEquals(Status.SUCCESS, response.status, response.toString())
        docEntry
    }

    /** On-demand entries are registered by ITI-61, which this registry does not offer, so go to the store. */
    private static void storeOnDemandEntry(DocumentEntry stable) {
        def onDemand = SampleData.createDocumentEntry(stable.patientId)
        onDemand.type = DocumentEntryType.ON_DEMAND
        onDemand.entryUuid = 'urn:uuid:' + UUID.randomUUID()
        onDemand.uniqueId = '1.2.3.' + System.nanoTime()
        onDemand.availabilityStatus = AvailabilityStatus.APPROVED
        // on-demand entries have no creation time, hash, size or legal authenticator
        onDemand.setCreationTime((String) null)
        onDemand.hash = null
        onDemand.size = null
        onDemand.legalAuthenticator = null
        ContextFacade.getBean(DataStore).store(onDemand)
    }

    private List<DocumentEntry> findDocuments(DocumentEntry doc, Closure customize = {}) {
        def query = new FindDocumentsQuery()
        query.patientId = doc.patientId
        query.status = [AvailabilityStatus.APPROVED]
        customize(query)
        runQuery(query).documentEntries
    }

    private QueryResponse runQuery(StoredQuery query) {
        def queryRegistry = new QueryRegistry(query)
        queryRegistry.returnType = QueryReturnType.LEAF_CLASS
        def response = send(ITI18, queryRegistry, QueryResponse.class)
        assertEquals(Status.SUCCESS, response.status, response.toString())
        response
    }
}
