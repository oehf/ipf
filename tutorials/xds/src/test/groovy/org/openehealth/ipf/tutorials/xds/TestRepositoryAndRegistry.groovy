/*
 * Copyright 2009 the original author or authors.
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

import org.apache.commons.io.IOUtils
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsExcludeQuery
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindFoldersQuery
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortKey
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrder
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import jakarta.activation.DataHandler

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * Tests against the registry/repository.
 * @author Jens Riemschneider
 */
class TestRepositoryAndRegistry extends StandardTestContainer {
    def ITI18 = "xds-iti18://localhost:${port}/xds-iti18"
    def ITI41 = "xds-iti41://localhost:${port}/xds-iti41"
    def ITI42 = "xds-iti42://localhost:${port}/xds-iti42"
    def ITI43 = "xds-iti43://localhost:${port}/xds-iti43"

    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), 'context.xml', false)
    }

    @Test
    void testProvideAndRegister() {
        def provide = SampleData.createProvideAndRegisterDocumentSet()
        def docEntry = provide.documents[0].documentEntry
        def patientId = docEntry.patientId
        docEntry.extraMetadata = ['urn:abc': ['ddd']]
        patientId.id = UUID.randomUUID().toString()
        docEntry.uniqueId = '4.3.2.1'
        docEntry.hash = ContentUtils.sha1(provide.documents[0].getContent(DataHandler))
        docEntry.size = ContentUtils.size(provide.documents[0].getContent(DataHandler))

        def response = send(ITI41, provide, Response.class)
        assertEquals(Status.SUCCESS, response.status, response.toString())

        def query = new FindDocumentsQuery()
        query.patientId = docEntry.patientId
        query.status = [AvailabilityStatus.APPROVED]
        def queryReg = new QueryRegistry(query)
        queryReg.returnType = QueryReturnType.LEAF_CLASS
        def queryResponse = send(ITI18, queryReg, QueryResponse.class)
        assertEquals(Status.SUCCESS, queryResponse.status, queryResponse.toString())
        assertEquals(1, queryResponse.documentEntries.size())
        assertEquals(docEntry.uniqueId, queryResponse.documentEntries[0].uniqueId)
        assertEquals(docEntry.extraMetadata, queryResponse.documentEntries[0].extraMetadata)
    }

    /**
     * ITI-18 defines no ordering, so the registry of this tutorial serves it as the bilaterally agreed
     * extension: the order arrives in the $ipfSortOrder slot, and the registry reports back which order
     * it applied -- without that a consumer could not tell an applied order from an ignored one.
     */
    @Test
    void testSortedQuery() {
        def patientId = registerThreeDocuments()

        def response = query(patientId, new SortOrder(SortKey.descending('$XDSDocumentEntryUniqueId')), null, null)

        assertEquals(Status.SUCCESS, response.status, response.toString())
        assertEquals(['3.3.3', '2.2.2', '1.1.1'], response.documentEntries.collect { it.uniqueId })
        assertEquals(1, response.honoredSortOrder.keys.size())
        assertEquals(SortKey.Direction.DESCENDING, response.honoredSortOrder.keys[0].direction)
    }

    /**
     * Folders and submission sets carry their own attribute names, so ordering them takes its own
     * comparator: the same concept is $XDSDocumentEntryUniqueId for a document entry and
     * $XDSFolderUniqueId for a folder.
     */
    @Test
    void testSortedFolderQuery() {
        def patientId = registerThreeDocuments()

        def query = new FindFoldersQuery()
        query.patientId = patientId
        query.status = [AvailabilityStatus.APPROVED]
        query.sortOrder = new SortOrder(SortKey.descending('$XDSFolderUniqueId'))
        def queryReg = new QueryRegistry(query)
        queryReg.returnType = QueryReturnType.LEAF_CLASS

        def response = send(ITI18, queryReg, QueryResponse.class)

        assertEquals(Status.SUCCESS, response.status, response.toString())
        def uniqueIds = response.folders.collect { it.uniqueId }
        assertEquals(uniqueIds.toSorted().reverse(), uniqueIds, 'the folders are not in descending order')
        assertEquals(3, uniqueIds.size())
        assertEquals(query.sortOrder, response.honoredSortOrder)
    }

    /**
     * A document entry order names no folder attribute, so the folders are left as they are -- and the
     * registry must not claim to have honored anything.
     */
    @Test
    void testADocumentOrderDoesNotReorderFolders() {
        def patientId = registerThreeDocuments()

        def query = new FindFoldersQuery()
        query.patientId = patientId
        query.status = [AvailabilityStatus.APPROVED]
        query.sortOrder = new SortOrder(SortKey.descending('$XDSDocumentEntryUniqueId'))
        def queryReg = new QueryRegistry(query)
        queryReg.returnType = QueryReturnType.LEAF_CLASS

        def response = send(ITI18, queryReg, QueryResponse.class)

        assertEquals(Status.SUCCESS, response.status, response.toString())
        assertEquals(3, response.folders.size())
        assertEquals(null, response.honoredSortOrder)
    }

    /**
     * The window is asked for with the ebRS pagination attributes and answered with totalResultCount,
     * which is what lets a consumer show a result count it never fetched.
     */
    @Test
    void testPagedQuery() {
        def patientId = registerThreeDocuments()
        def ascending = new SortOrder(SortKey.ascending('$XDSDocumentEntryUniqueId')).withStableTiebreaker()

        def response = query(patientId, ascending, 1, 1)

        assertEquals(Status.SUCCESS, response.status, response.toString())
        assertEquals(['2.2.2'], response.documentEntries.collect { it.uniqueId })
        assertEquals(3, response.totalResultCount)
        assertEquals(1, response.startIndex)
    }

    /**
     * A query asking for neither must behave exactly as it did before the extension existed.
     */
    @Test
    void testUnsortedQueryReportsNothing() {
        def patientId = registerThreeDocuments()

        def response = query(patientId, null, null, null)

        assertEquals(Status.SUCCESS, response.status, response.toString())
        assertEquals(3, response.documentEntries.size())
        assertEquals(null, response.honoredSortOrder)
        assertEquals(null, response.totalResultCount)
        assertEquals(null, response.startIndex)
    }

    /**
     * A window starting past the end is refused. An empty page would be indistinguishable from a query
     * that simply matched nothing, and the two mean very different things to whoever is paging.
     */
    @Test
    void testAStartIndexBeyondTheEndIsRejected() {
        def patientId = registerThreeDocuments()
        def ascending = new SortOrder(SortKey.ascending('$XDSDocumentEntryUniqueId')).withStableTiebreaker()

        assertEquals(Status.FAILURE, query(patientId, ascending, 3, 2).status)
        assertEquals(Status.FAILURE, query(patientId, ascending, 1000, 2).status)

        // the last page is still fine -- one entry left at offset 2 of three
        def lastPage = query(patientId, ascending, 2, 2)
        assertEquals(Status.SUCCESS, lastPage.status, lastPage.toString())
        assertEquals(['3.3.3'], lastPage.documentEntries.collect { it.uniqueId })
    }

    /**
     * Offset zero is exempt: a search matching nothing is a legitimate empty answer, not a window out of
     * range, and must not be turned into an error.
     */
    @Test
    void testAnEmptyResultAtOffsetZeroIsNotAnError() {
        def unknownPatient = SampleData.createRegisterDocumentSet().documentEntries[0].patientId
        unknownPatient.id = UUID.randomUUID().toString()

        def response = query(unknownPatient, null, 0, 10)

        assertEquals(Status.SUCCESS, response.status, response.toString())
        assertEquals(0, response.documentEntries.size())
    }

    /**
     * FindDocumentsExclude returns what FindDocuments would, minus the documents whose metadata carry a
     * value the requester named as excluded (ITI TF-2: 3.18.4.1.2.3.7.15).
     */
    @Test
    void testFindDocumentsExcludeQuery() {
        def patientId = registerThreeDocuments { docEntry ->
            // one of the three gets a class code of its own, so that excluding that code has to leave
            // exactly the other two -- a filter that drops everything or nothing proves little
            if (docEntry.uniqueId == '2.2.2') {
                docEntry.classCode = new Code('excludedCode', null, 'scheme2')
            }
        }

        def response = excludeQuery(patientId) { query ->
            query.excludedClassCodes = [new Code('excludedCode', null, 'scheme2')]
        }

        assertEquals(Status.SUCCESS, response.status, response.toString())
        assertEquals(['1.1.1', '3.3.3'], response.documentEntries.collect { it.uniqueId }.toSorted())
    }

    /**
     * Excluding a value that no document carries excludes nothing, which is what tells the excluding
     * parameters apart from their non-excluding counterparts: those would have returned nothing here.
     */
    @Test
    void testFindDocumentsExcludeQueryMatchingNoDocument() {
        def patientId = registerThreeDocuments()

        def response = excludeQuery(patientId) { query ->
            query.excludedClassCodes = [new Code('noDocumentHasThis', null, 'scheme2')]
            query.excludedAuthorPersons = ['Sieglinde%']
        }

        assertEquals(Status.SUCCESS, response.status, response.toString())
        assertEquals(3, response.documentEntries.size())
    }

    /**
     * Excluded event codes are a query list and keep the AND/OR semantics of the including ones: a
     * document is dropped when it matches the list, so it survives as soon as one of the ANDed groups
     * names none of its event codes.
     */
    @Test
    void testFindDocumentsExcludeQueryWithEventCodes() {
        def patientId = registerThreeDocuments()

        def matchingEventCodes = new QueryList<Code>()
        matchingEventCodes.outerList.add([new Code('code9', null, 'scheme9')])
        assertEquals(0, excludeQuery(patientId) { it.excludedEventCodes = matchingEventCodes }
                .documentEntries.size())

        // the second group matches no event code of the documents, so the list as a whole does not
        // match and nothing is excluded
        def partiallyMatchingEventCodes = new QueryList<Code>()
        partiallyMatchingEventCodes.outerList.add([new Code('code9', null, 'scheme9')])
        partiallyMatchingEventCodes.outerList.add([new Code('otherCode', null, 'scheme9')])
        assertEquals(3, excludeQuery(patientId) { it.excludedEventCodes = partiallyMatchingEventCodes }
                .documentEntries.size())
    }

    private def excludeQuery(patientId, Closure parameters) {
        def query = new FindDocumentsExcludeQuery()
        query.patientId = patientId
        query.status = [AvailabilityStatus.APPROVED]
        parameters(query)
        def queryReg = new QueryRegistry(query)
        queryReg.returnType = QueryReturnType.LEAF_CLASS
        send(ITI18, queryReg, QueryResponse.class)
    }

    private def query(patientId, SortOrder sortOrder, Integer startIndex, Integer maxResults) {
        def query = new FindDocumentsQuery()
        query.patientId = patientId
        query.status = [AvailabilityStatus.APPROVED]
        query.sortOrder = sortOrder
        def queryReg = new QueryRegistry(query)
        queryReg.returnType = QueryReturnType.LEAF_CLASS
        queryReg.startIndex = startIndex
        queryReg.maxResults = maxResults
        send(ITI18, queryReg, QueryResponse.class)
    }

    /**
     * Registers three documents for one patient, in an order that is not the sorted one, so that a test
     * asserting the order cannot pass by accident.
     */
    private def registerThreeDocuments(Closure customizer = null) {
        def sharedPatientId = UUID.randomUUID().toString()
        def oidSuffix = 0
        ['2.2.2', '3.3.3', '1.1.1'].each { uniqueId ->
            def register = SampleData.createRegisterDocumentSet()
            def docEntry = register.documentEntries[0]

            // every entry needs its own identity, and the associations have to follow it. Unique ids are
            // OIDs, so they cannot simply be UUIDs like the entry uuids are.
            def newUuids = [:]
            [register.submissionSet, docEntry, register.folders[0]].each { entry ->
                def fresh = 'urn:uuid:' + UUID.randomUUID()
                newUuids[entry.entryUuid] = fresh
                entry.entryUuid = fresh
                entry.uniqueId = '1.2.3.4.' + (++oidSuffix)
                entry.patientId.id = sharedPatientId
            }
            register.associations.each { association ->
                association.sourceUuid = newUuids.getOrDefault(association.sourceUuid, association.sourceUuid)
                association.targetUuid = newUuids.getOrDefault(association.targetUuid, association.targetUuid)
                association.entryUuid = 'urn:uuid:' + UUID.randomUUID()
            }
            docEntry.uniqueId = uniqueId
            customizer?.call(docEntry)

            def response = send(ITI42, register, Response.class)
            assertEquals(Status.SUCCESS, response.status, response.toString())
        }
        def patientId = SampleData.createRegisterDocumentSet().documentEntries[0].patientId
        patientId.id = sharedPatientId
        patientId
    }

    @Test
    void testRegister() {
        def register = SampleData.createRegisterDocumentSet()
        def docEntry = register.documentEntries[0]
        def patientId = docEntry.patientId
        patientId.id = UUID.randomUUID().toString()
        docEntry.uniqueId = '1.2.3.4'

        def response = send(ITI42, register, Response.class)
        assertEquals(Status.SUCCESS, response.status, response.toString())

        def query = new FindDocumentsQuery()
        query.patientId = docEntry.patientId
        query.status = [AvailabilityStatus.APPROVED]
        def queryReg = new QueryRegistry(query)
        queryReg.returnType = QueryReturnType.LEAF_CLASS
        def queryResponse = send(ITI18, queryReg, QueryResponse.class)
        assertEquals(Status.SUCCESS, queryResponse.status, queryResponse.toString())
        assertEquals(1, queryResponse.documentEntries.size())
        assertEquals(docEntry.uniqueId, queryResponse.documentEntries[0].uniqueId)
    }

    @Test
    void testRetrieve() {
        def provide = SampleData.createProvideAndRegisterDocumentSet()
        def docEntry = provide.documents[0].documentEntry
        docEntry.hash = ContentUtils.sha1(provide.documents[0].dataHandler)
        docEntry.size = ContentUtils.size(provide.documents[0].dataHandler)
        def patientId = docEntry.patientId
        patientId.id = UUID.randomUUID().toString()

        def response = send(ITI41, provide, Response.class)
        assertEquals(Status.SUCCESS, response.status, response.toString())

        def retrieve = new RetrieveDocumentSet()
        def doc1 = new DocumentReference()
        doc1.documentUniqueId = provide.documents[0].documentEntry.uniqueId
        doc1.repositoryUniqueId = 'something'
        retrieve.documents.add(doc1)
        retrieve.documents.add(doc1)
        def retrieveResponse = send(ITI43, retrieve, RetrievedDocumentSet.class)
        assertEquals(Status.SUCCESS, retrieveResponse.status, retrieveResponse.toString())

        def attachments = retrieveResponse.documents[0].dataHandler.dataSource.attachments
        assertEquals(2, retrieveResponse.documents.size())
        assertEquals(2, attachments.size())
        def expectedContents = read(SampleData.createDataHandler())
        for (attachment in attachments) {
            assertEquals(expectedContents, read(attachment.dataHandler))
        }
    }

    def read(dataHandler) {
        def inputStream = dataHandler.inputStream
        try {
            return IOUtils.toString(inputStream)
        }
        finally {
            inputStream.close()
        }
    }
}
