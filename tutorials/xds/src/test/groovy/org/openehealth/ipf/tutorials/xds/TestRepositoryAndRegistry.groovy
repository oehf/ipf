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
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import static junit.framework.Assert.assertEquals
import javax.activation.DataHandler
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType

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
        docEntry.hash = ContentUtils.sha1(provide.documents[0].getContent(DataHandler))
        docEntry.size = ContentUtils.size(provide.documents[0].getContent(DataHandler))

        def response = send(ITI41, provide, Response.class)
        assertEquals(response.toString(), Status.SUCCESS, response.status)

        def query = new FindDocumentsQuery()
        query.patientId = docEntry.patientId
        query.status = [AvailabilityStatus.APPROVED]
        def queryReg = new QueryRegistry(query)
        queryReg.returnType = QueryReturnType.LEAF_CLASS
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
        queryReg.returnType = QueryReturnType.LEAF_CLASS
        def queryResponse = send(ITI18, queryReg, QueryResponse.class)
        assertEquals(queryResponse.toString(), Status.SUCCESS, queryResponse.status)
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
        assertEquals(response.toString(), Status.SUCCESS, response.status)

        def retrieve = new RetrieveDocumentSet()
        def doc1 = new RetrieveDocument()
        doc1.documentUniqueId = provide.documents[0].documentEntry.uniqueId
        doc1.repositoryUniqueId = 'something'
        retrieve.documents.add(doc1)
        retrieve.documents.add(doc1)
        def retrieveResponse = send(ITI43, retrieve, RetrievedDocumentSet.class)
        assertEquals(retrieveResponse.toString(), Status.SUCCESS, retrieveResponse.status)

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
