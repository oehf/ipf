package org.openehealth.ipf.tutorials.xds


import static junit.framework.Assert.assertEquals
import org.apache.commons.io.IOUtils
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.xds.SampleData
import org.openehealth.ipf.platform.camel.ihe.xds.core.StandardTestContainer
import org.openehealth.ipf.commons.ihe.xds.metadata.AvailabilityStatus
import org.openehealth.ipf.commons.ihe.xds.metadata.DocumentEntry
import org.openehealth.ipf.commons.ihe.xds.metadata.Identifiable
import org.openehealth.ipf.commons.ihe.xds.requests.*
import org.openehealth.ipf.commons.ihe.xds.requests.query.FindDocumentsQuery
import org.openehealth.ipf.commons.ihe.xds.responses.QueryResponse
import org.openehealth.ipf.commons.ihe.xds.responses.Response
import org.openehealth.ipf.commons.ihe.xds.responses.RetrievedDocumentSet
import org.openehealth.ipf.commons.ihe.xds.responses.Status

import javax.activation.DataHandler
import java.io.IOException
import java.io.InputStream
import java.util.Arrays
import java.util.UUID

/**
 * Tests against the registry/repository.
 * @author Jens Riemschneider
 */
class TestRepositoryAndRegistry extends StandardTestContainer {
    def ITI18 = "xds-iti18://localhost:${port}/xds-iti18"
    def ITI41 = "xds-iti41://localhost:${port}/xds-iti41"
    def ITI42 = "xds-iti42://localhost:${port}/xds-iti42"
    def ITI43 = "xds-iti43://localhost:${port}/xds-iti43"

    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), 'context.xml', false)
    }

    @Test
    void testProvideAndRegister() {
        def provide = SampleData.createProvideAndRegisterDocumentSet()
        def docEntry = provide.documents[0].documentEntry
        def patientId = docEntry.patientId
        patientId.id = UUID.randomUUID().toString()
        docEntry.uniqueId = '4.3.2.1'

        def response = send(ITI41, provide, Response.class)
        assertEquals(response.toString(), Status.SUCCESS, response.status)

        def query = new FindDocumentsQuery()
        query.patientId = docEntry.patientId
        query.status = [AvailabilityStatus.APPROVED]
        def queryReg = new QueryRegistry(query)
        queryReg.returnLeafObjects = true
        def queryResponse = send(ITI18, queryReg, QueryResponse.class)
        assertEquals(queryResponse.toString(), Status.SUCCESS, queryResponse.status)
        assertEquals(1, queryResponse.documentEntries.size())
        assertEquals(docEntry.uniqueId, queryResponse.documentEntries[0].uniqueId)
    }

    @Test
    void testRegister() {
        def register = SampleData.createRegisterDocumentSet()
        def docEntry = register.documentEntries[0]
        def patientId = docEntry.patientId
        patientId.id = UUID.randomUUID().toString()
        docEntry.uniqueId = '1.2.3.4'

        def response = send(ITI42, register, Response.class)
        assertEquals(response.toString(), Status.SUCCESS, response.status)

        def query = new FindDocumentsQuery()
        query.patientId = docEntry.patientId
        query.status = [AvailabilityStatus.APPROVED]
        def queryReg = new QueryRegistry(query)
        queryReg.returnLeafObjects = true
        def queryResponse = send(ITI18, queryReg, QueryResponse.class)
        assertEquals(queryResponse.toString(), Status.SUCCESS, queryResponse.status)
        assertEquals(1, queryResponse.documentEntries.size())
        assertEquals(docEntry.uniqueId, queryResponse.documentEntries[0].uniqueId)
    }

    @Test
    void testRetrieve() {
        def provide = SampleData.createProvideAndRegisterDocumentSet()
        def docEntry = provide.documents[0].documentEntry
        def patientId = docEntry.patientId
        patientId.id = UUID.randomUUID().toString()

        def response = send(ITI41, provide, Response.class)
        assertEquals(response.toString(), Status.SUCCESS, response.status)

        def retrieve = new RetrieveDocumentSet()
        def doc1 = new RetrieveDocument()
        doc1.documentUniqueId = provide.documents[0].documentEntry.uniqueId
        doc1.repositoryUniqueId = 'something'
        retrieve.documents.add(doc1)
        retrieve.documents.add(doc1)
        def retrieveResponse = send(ITI43, retrieve, RetrievedDocumentSet.class)
        assertEquals(retrieveResponse.toString(), Status.SUCCESS, retrieveResponse.status)
        assertEquals(2, retrieveResponse.documents.size())

        def expectedContents = read(SampleData.createDataHandler())
        assertEquals(expectedContents, read(retrieveResponse.documents[0].dataHandler))
        assertEquals(expectedContents, read(retrieveResponse.documents[1].dataHandler))
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
