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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti18;

import static junit.framework.Assert.assertEquals;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.cxf.bus.CXFBusImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.platform.camel.core.junit.DirtySpringContextJUnit4ClassRunner;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.query.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rim.AdhocQueryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Tests the ITI-18 component with the webservice and the client defined within the URI.
 * @author Jens Riemschneider
 */
@RunWith(DirtySpringContextJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/iti-18.xml")
public class TestIti18 {
    @Autowired
    private ProducerTemplate<Exchange> producerTemplate;

    private static final String SERVICE1 = "xdsb-iti18://localhost:9091/xdsb-iti18-service1?audit=false";
    private static final String SERVICE2 = "xdsb-iti18://localhost:9091/xdsb-iti18-service2";

    
    @Before
    public void setUp() {
        /*
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
        */
        
        JaxWsServerFactoryBean jaxwsBean = new JaxWsServerFactoryBean();
        CXFBusImpl bus = (CXFBusImpl)jaxwsBean.getBus();
        bus.getOutInterceptors().add(new TestIti18AuditFinalInterceptor(true));
        bus.getInInterceptors().add(new TestIti18AuditFinalInterceptor(false));
    }
    
    /** Calls the route attached to the ITI-18 endpoint. */
    @Test
    public void testIti18() {
        AdhocQueryRequest request = new AdhocQueryRequest();
        request.setAdhocQuery(new AdhocQueryType());
        request.getAdhocQuery().setId(UUID.randomUUID().toString());
        request.setComment("ok");

        AdhocQueryResponse response1 =
                (AdhocQueryResponse) producerTemplate.requestBody(SERVICE1, request);

        assertEquals("service 1: ok", response1.getStatus());

        AdhocQueryResponse response2 =
                (AdhocQueryResponse) producerTemplate.requestBody(SERVICE2, request);

        assertEquals("service 2: ok", response2.getStatus());
    }
}