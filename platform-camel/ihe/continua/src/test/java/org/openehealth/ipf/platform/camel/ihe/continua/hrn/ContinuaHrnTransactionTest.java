/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

/**
 * Tests the Continua HRN transaction.
 * 
 * @author Stefan Ivanov
 * 
 */
public class ContinuaHrnTransactionTest extends StandardTestContainer {
    
    private static JAXBContext jaxbContext;
    private ProvideAndRegisterDocumentSetRequestType hrnRequest;
    
    @BeforeClass
    public static void setUpClass() throws JAXBException {
        startServer(new CXFServlet(), "continua-hrn.xml");
        jaxbContext = JAXBContext
            .newInstance("org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30");
    }
    
    @Before
    public void setUp() throws Exception {
        initRequest();
    }

    @Test
    public void happyCase() throws Exception {
        String uri = "xds-iti41://localhost:" + getPort() + "/continuaHRNService";
        Response response = (Response) send(uri, hrnRequest, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus());
    }
    
    @SuppressWarnings("unchecked")
    private void initRequest() throws Exception {
        Unmarshaller u = jaxbContext.createUnmarshaller();
        InputStream is = getClass().getClassLoader().getResourceAsStream(
            "continua-hrn/ProvideAndRegisterDocumentSet.xml");
        hrnRequest = ((JAXBElement<ProvideAndRegisterDocumentSetRequestType>) u.unmarshal(is))
            .getValue();
    }
}
