/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.pcc1;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationAwareServiceInfo;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.ihe.pixpdqv3.pcc1.Pcc1PortType;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3ContinuationAwareProducer;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3Endpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

import javax.xml.namespace.QName;

/**
 * Camel endpoint for the PCC-1 transaction.
 * @author Dmytro Rud
 */
public class Pcc1Endpoint extends Hl7v3Endpoint {
    private static final String[][] REQUEST_VALIDATION_PROFILES = new String[][] {
            new String[] {"QUPC_IN043100UV01", null},
            new String[] {"QUQI_IN000003UV01", null},
            new String[] {"QUQI_IN000003UV01_Cancel", null}
    };

    private static final String[][] RESPONSE_VALIDATION_PROFILES = new String[][] {
            new String[] {"QUPC_IN043200UV01", null},
            new String[] {"MCCI_IN000002UV01", null}
    };

    private final static String NS_URI = "urn:ihe:pcc:qed:2007";
    public final static Hl7v3ContinuationAwareServiceInfo PCC_1 = new Hl7v3ContinuationAwareServiceInfo(
            new QName(NS_URI, "ClinicalDataSource_Service", "qed"),
            Pcc1PortType.class,
            new QName(NS_URI, "ClinicalDataSource_Binding_Soap12", "qed"),
            false,
            "wsdl/pcc1/pcc1-raw.wsdl",
            REQUEST_VALIDATION_PROFILES,
            RESPONSE_VALIDATION_PROFILES,
            "QUPC_IN043200UV01",
            true,
            false,
            "QUPC_IN043100UV01",
            "QUPC_IN043200UV01");

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti47Component
     *          the component creating this endpoint.
     */
    public Pcc1Endpoint(
            String endpointUri, 
            String address, 
            Pcc1Component iti47Component,
            InterceptorProvider customInterceptors) 
    {
        super(endpointUri, address, iti47Component, customInterceptors);
    }

    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new Hl7v3ClientFactory(
                PCC_1, 
                getServiceUrl(), 
                getCustomInterceptors());
        return new Hl7v3ContinuationAwareProducer(
                this,
                clientFactory,
                PCC_1,
                isSupportContinuation(),
                isAutoCancel(),
                isValidationOnContinuation());
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new Hl7v3ServiceFactory(
                PCC_1, 
                getServiceAddress(),
                getCustomInterceptors());

        Pcc1PortType portTypeImpl = isSupportContinuation() ?
                new Pcc1ContinuationAwareService(
                        getContinuationStorage(),
                        getDefaultContinuationThreshold(),
                        isValidationOnContinuation()) :
                new Pcc1Service();

        ServerFactoryBean serverFactory = serviceFactory.createServerFactory(portTypeImpl);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
