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
package org.openehealth.ipf.commons.ihe.xds.iti42;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer;
import org.openehealth.ipf.commons.ihe.ws.server.ServletServer;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.ResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.SubmitObjectsRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RegistryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42PortType;
import org.springframework.core.io.ClassPathResource;

public class CxfEndpointTest {
    private final EbXMLFactory factory = new EbXMLFactory30();

    private final RegisterDocumentSetTransformer reqTransformer = 
        new RegisterDocumentSetTransformer(factory);
    
    private final ResponseTransformer respTransformer = 
        new ResponseTransformer(factory);
    
    @Test
    public void test() throws IOException, Exception {
        int port = ServletServer.getFreePort();
        
        JettyServer server = new JettyServer();
        server.setContextResource(new ClassPathResource("cxf-context.xml").getURI().toString());
        server.setPort(port);
        server.setContextPath("");
        server.setServletPath("/*");
        server.setServlet(new CXFServlet());
        
        server.start();
        try {
            ItiServiceFactory serviceFactory = Iti42.getServiceFactory(false, false, "/iti-42");
            ItiClientFactory clientFactory = Iti42.getClientFactory(false, false, false, "http://localhost:" + port + "/iti-42");
            
            serviceFactory.createService(MyIti42.class);
            Iti42PortType client = (Iti42PortType) clientFactory.getClient();
            
            RegisterDocumentSet request = SampleData.createRegisterDocumentSet();
            EbXMLSubmitObjectsRequest ebXMLReq = reqTransformer.toEbXML(request);            
            SubmitObjectsRequest rawReq = (SubmitObjectsRequest) ebXMLReq.getInternal();
            
            RegistryResponseType rawResp = client.documentRegistryRegisterDocumentSetB(rawReq);
            
            EbXMLRegistryResponse30 ebXMLResp = new EbXMLRegistryResponse30(rawResp);
            Response response = respTransformer.fromEbXML(ebXMLResp);
            
            assertEquals(Status.SUCCESS, response.getStatus());
        }
        finally {
            server.stop();
        }
    }
    
    public static class MyIti42 implements Iti42PortType {
        private final EbXMLFactory factory = new EbXMLFactory30();

        private final RegisterDocumentSetTransformer reqTransformer = 
            new RegisterDocumentSetTransformer(factory);
        
        private final ResponseTransformer respTransformer = 
            new ResponseTransformer(factory);
        
        private final SubmitObjectsRequestValidator reqValidator = 
            new SubmitObjectsRequestValidator();

        private final RegistryResponseValidator respValidator = 
            new RegistryResponseValidator();
        
        @Override
        public RegistryResponseType documentRegistryRegisterDocumentSetB(SubmitObjectsRequest rawReq) {
            EbXMLSubmitObjectsRequest30 ebXMLReq = new EbXMLSubmitObjectsRequest30(rawReq);            
            reqValidator.validate(ebXMLReq, null);
            RegisterDocumentSet request = reqTransformer.fromEbXML(ebXMLReq);
            
            Response response = new Response(Status.SUCCESS);
            if (!request.getSubmissionSet().getEntryUuid().equals("submissionSet01")) {
                response.setStatus(Status.FAILURE);
                response.setErrors(Arrays.asList(new ErrorInfo(ErrorCode.REGISTRY_ERROR, "unexpected value", Severity.ERROR, null)));
            }
            
            EbXMLRegistryResponse ebXMLResp = respTransformer.toEbXML(response);
            respValidator.validate(ebXMLResp, null);
            RegistryResponseType rawResp = (RegistryResponseType) ebXMLResp.getInternal();
            return rawResp;
        }
    }
}
