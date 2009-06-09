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
package org.openehealth.ipf.platform.camel.ihe.xds.iti43;

import static junit.framework.Assert.assertEquals;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.cxf.bus.CXFBusImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.CxfTestUtils;
import org.openehealth.ipf.platform.camel.ihe.xds.iti43.service.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.xds.iti43.service.RetrieveDocumentSetResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.activation.DataHandler;

/**
 * Tests the ITI-43 transaction with a webservice and client adapter defined via URIs.
 * author Jens Riemschneider
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/iti-43.xml")
public class TestIti43 {
    @Autowired
    private ProducerTemplate<Exchange> producerTemplate;

    private static final String SERVICE1 = "xds-iti43://localhost:9091/xds-iti43-service1";
    private static final String SERVICE2 = "xds-iti43://localhost:9091/xds-iti43-service2";

    
    @Before
    public void setUp() {
        /*
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
        */
        
        JaxWsServerFactoryBean jaxwsBean = new JaxWsServerFactoryBean();
        CXFBusImpl bus = (CXFBusImpl)jaxwsBean.getBus();
        bus.getOutInterceptors().add(new Iti43TestAuditFinalInterceptor(true));
        bus.getInInterceptors().add(new Iti43TestAuditFinalInterceptor(false));
    }

    
    /** Calls the route attached to the ITI-43 endpoint. */
    @Test
    public void testIti43() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        RetrieveDocumentSetRequestType.DocumentRequest documentRequest = new RetrieveDocumentSetRequestType.DocumentRequest();
        documentRequest.setDocumentUniqueId("ok");
        request.getDocumentRequest().add(documentRequest);

        RetrieveDocumentSetResponseType response1 =
                (RetrieveDocumentSetResponseType) producerTemplate.requestBody(SERVICE1, request);
        assertEquals("service 1: ok", response1.getRegistryResponse().getStatus());

        RetrieveDocumentSetResponseType response2 =
                (RetrieveDocumentSetResponseType) producerTemplate.requestBody(SERVICE2, request);

        assertEquals("service 2: ok", response2.getRegistryResponse().getStatus());
    }

    /** Calls the route attached to the ITI-43 endpoint with a large content stream to
     *  check if the infrastructure is supporting MTOM with efficient memory usage */
    @Test
    public void testIti43LargeDocument() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        RetrieveDocumentSetRequestType.DocumentRequest documentRequest = new RetrieveDocumentSetRequestType.DocumentRequest();
        documentRequest.setDocumentUniqueId("large");
        request.getDocumentRequest().add(documentRequest);
        RetrieveDocumentSetResponseType response =
                (RetrieveDocumentSetResponseType) producerTemplate.requestBody(SERVICE1, request);

        DataHandler dataHandler = response.getDocumentResponse().get(0).getDocument();
        assertTrue(CxfTestUtils.isCxfUsingMtom(dataHandler));

        assertEquals("service 1: large", response.getRegistryResponse().getStatus());
    }
}