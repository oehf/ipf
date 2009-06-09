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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti41;

import static junit.framework.Assert.assertEquals;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.cxf.bus.CXFBusImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.utils.LargeDataSource;
import org.openehealth.ipf.platform.camel.ihe.xdsb.iti41.service.ProvideAndRegisterDocumentSetRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.activation.DataHandler;
import javax.activation.DataSource;

/**
 * Tests the ITI-41 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/iti-41.xml")
public class TestIti41 {
    @Autowired
    private ProducerTemplate<Exchange> producerTemplate;

    private static final String SERVICE1 = "xdsb-iti41://localhost:9091/xdsb-iti41-service1";
    private static final String SERVICE2 = "xdsb-iti41://localhost:9091/xdsb-iti41-service2";

    
    @Before
    public void setUp() {
        /*
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
        */
        
        JaxWsServerFactoryBean jaxwsBean = new JaxWsServerFactoryBean();
        CXFBusImpl bus = (CXFBusImpl)jaxwsBean.getBus();
        bus.getOutInterceptors().add(new TestIti41AuditFinalInterceptor(true));
        bus.getInInterceptors().add(new TestIti41AuditFinalInterceptor(false));
    }
    
    
    /** Calls the route attached to the ITI-41 endpoint. */
    @Test
    public void testIti41() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitObjectRequest = new SubmitObjectsRequest();
        submitObjectRequest.setId(UUID.randomUUID().toString());
        submitObjectRequest.setComment("ok");
        request.setSubmitObjectsRequest(submitObjectRequest);

        RegistryResponseType response1 =
                (RegistryResponseType)producerTemplate.requestBody(SERVICE1, request);

        assertEquals("service 1: ok", response1.getStatus());

        RegistryResponseType response2 =
                (RegistryResponseType)producerTemplate.requestBody(SERVICE2, request);

        assertEquals("service 2: ok", response2.getStatus());
    }

    /** Calls the route attached to the ITI-41 endpoint with a large content stream to
     *  check if the infrastructure is supporting MTOM with efficient memory usage */
    @Test
    public void testIti41LargeDocument() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        ProvideAndRegisterDocumentSetRequestType.Document document = new ProvideAndRegisterDocumentSetRequestType.Document();
        DataSource dataSource = new LargeDataSource();
        document.setValue(new DataHandler(dataSource));
        request.getDocument().add(document);
        SubmitObjectsRequest submitObjectRequest = new SubmitObjectsRequest();
        submitObjectRequest.setId("submission-set-id-large");
        submitObjectRequest.setComment("large");
        request.setSubmitObjectsRequest(submitObjectRequest);

        RegistryResponseType response =
            (RegistryResponseType)producerTemplate.requestBody(SERVICE1, request);

        assertEquals("service 1: ok", response.getStatus());
    }
}