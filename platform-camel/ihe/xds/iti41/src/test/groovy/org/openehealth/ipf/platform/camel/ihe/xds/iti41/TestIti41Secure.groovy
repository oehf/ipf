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
package org.openehealth.ipf.platform.camel.ihe.xds.iti41

import static junit.framework.Assert.assertEquals
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status.*

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.xds.commons.SampleData
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestContainer

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.ProvideAndRegisterDocumentSet
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response

/**
 * Tests the ITI-41 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
class TestIti41Secure extends StandardTestContainer {
    def SERVICE1 = "xds-iti41://localhost:${port}/xds-iti41-service1?secure=true"
    def SERVICE2 = "xds-iti41://localhost:${port}/xds-iti41-service2?secure=true"
    
    def request
    def docEntry

    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), 'iti-41.xml', true)
    }
    
    @Before
    void setUp() {
        request = SampleData.createProvideAndRegisterDocumentSet()
        docEntry = request.documents[0].documentEntry
    }
    
    @Test
    void testIti41() {
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2').status
        assert auditSender.messages.size() == 4
    }

    def sendIt(endpoint, value) {
        docEntry.comments = new LocalizedString(value)
        send(endpoint, request, Response.class)
    }
}