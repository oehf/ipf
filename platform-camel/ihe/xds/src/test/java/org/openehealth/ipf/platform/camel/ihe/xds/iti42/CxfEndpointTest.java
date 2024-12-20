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
package org.openehealth.ipf.platform.camel.ihe.xds.iti42;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsRequestClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsRequestServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer;
import org.openehealth.ipf.commons.ihe.ws.server.ServletServer;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.responses.*;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.ResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.SubmitObjectsRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RegistryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42PortType;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_42;

public class CxfEndpointTest {
    private final EbXMLFactory factory = new EbXMLFactory30();

    private final RegisterDocumentSetTransformer reqTransformer =
        new RegisterDocumentSetTransformer(factory);

    private final ResponseTransformer respTransformer =
        new ResponseTransformer(factory);

    private int port;
    private JettyServer server;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        port = ServletServer.getFreePort();
        server = new JettyServer();
        server.setContextResource(getClass().getResource("/cxf-context.xml").toURI().toString());
        server.setPort(port);
        server.setContextPath("/");
        server.setServletPath("/*");
        server.setServlet(new CXFServlet());

        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void test() {
        runRequestAndExpectFailure();

        JaxWsServiceFactory<? extends XdsAuditDataset> serviceFactory = new JaxWsRequestServiceFactory<>(
                ITI_42.getWsTransactionConfiguration(), "/iti-42", null, null, null, null);
        var factory = serviceFactory.createServerFactory(MyIti42.class);
        var serviceServer = factory.create();

        runRequestAndExpectFailure();

        serviceServer.start();
        assertEquals(Status.SUCCESS, runRequest().getStatus());

        serviceServer.stop();
        runRequestAndExpectFailure();

        //serviceServer = factory.create();
        serviceServer.start();
        assertEquals(Status.SUCCESS, runRequest().getStatus());

        serviceServer.stop();
    }

    private void runRequestAndExpectFailure() {
        try {
            runRequest();
            fail("Expected Exception: " + RuntimeException.class);
        }
        catch (RuntimeException e) {
            // ok
        }
    }

    private Response runRequest() {
        JaxWsClientFactory<? extends XdsAuditDataset> clientFactory = new JaxWsRequestClientFactory<>(
                ITI_42.getWsTransactionConfiguration(),
                "http://localhost:" + port + "/iti-42",
                null, null, null, null, null, null, null, null);

        var client = (Iti42PortType) clientFactory.getClient();
        var request = SampleData.createRegisterDocumentSet();
        var ebXMLReq = reqTransformer.toEbXML(request);
        var rawReq = ebXMLReq.getInternal();
        var rawResp = client.documentRegistryRegisterDocumentSetB(rawReq);
        var ebXMLResp = new EbXMLRegistryResponse30(rawResp);
        return respTransformer.fromEbXML(ebXMLResp);
    }

    public static class MyIti42 implements Iti42PortType {
        private final EbXMLFactory factory = new EbXMLFactory30();

        private final RegisterDocumentSetTransformer reqTransformer =
            new RegisterDocumentSetTransformer(factory);

        private final ResponseTransformer respTransformer =
            new ResponseTransformer(factory);

        private final SubmitObjectsRequestValidator reqValidator =
            SubmitObjectsRequestValidator.getInstance();

        private final RegistryResponseValidator respValidator =
            RegistryResponseValidator.getInstance();

        @Override
        public RegistryResponseType documentRegistryRegisterDocumentSetB(SubmitObjectsRequest rawReq) {

            var ebXMLReq = new EbXMLSubmitObjectsRequest30(rawReq);
            reqValidator.validate(ebXMLReq, ITI_42);
            var request = reqTransformer.fromEbXML(ebXMLReq);

            var response = new Response(Status.SUCCESS);
            if (!request.getSubmissionSet().getEntryUuid().equals("submissionSet01")) {
                response.setStatus(Status.FAILURE);
                response.setErrors(List.of(new ErrorInfo(ErrorCode.REGISTRY_ERROR, "unexpected value", Severity.ERROR, null, null)));
            }

            var ebXMLResp = respTransformer.toEbXML(response);
            respValidator.validate(ebXMLResp, ITI_42);
            return ebXMLResp.getInternal();
        }
    }
}
