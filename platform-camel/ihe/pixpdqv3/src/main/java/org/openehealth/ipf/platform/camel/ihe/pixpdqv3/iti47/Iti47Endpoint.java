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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti47;

import javax.xml.namespace.QName;

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
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti47.Iti47PortType;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3ContinuationAwareProducer;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3Endpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * The Camel endpoint for the ITI-47 transaction.
 */
public class Iti47Endpoint extends Hl7v3Endpoint {
    private static final String[][] REQUEST_VALIDATION_PROFILES = new String[][] {
            new String[] {"PRPA_IN201305UV02", "iti47/PRPA_IN201305UV02"},
            new String[] {"QUQI_IN000003UV01", null},
            new String[] {"QUQI_IN000003UV01_Cancel", null}
    };

    private static final String[][] RESPONSE_VALIDATION_PROFILES = new String[][] {
            new String[] {"PRPA_IN201306UV02", "iti47/PRPA_IN201306UV02"},
            new String[] {"MCCI_IN000002UV01", null}
    };

    private final static String NS_URI = "urn:ihe:iti:pdqv3:2007";
    public final static Hl7v3ContinuationAwareServiceInfo ITI_47 = new Hl7v3ContinuationAwareServiceInfo(
            new QName(NS_URI, "PDSupplier_Service", "ihe"),
            Iti47PortType.class,
            new QName(NS_URI, "PDSupplier_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti47/iti47-raw.wsdl",
            REQUEST_VALIDATION_PROFILES,
            RESPONSE_VALIDATION_PROFILES,
            "PRPA_IN201306UV02",
            true,
            false,
            "PRPA_IN201305UV02",
            "PRPA_IN201306UV02");

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti47Component
     *          the component creating this endpoint.
     */
    public Iti47Endpoint(
            String endpointUri, 
            String address, 
            Iti47Component iti47Component,
            InterceptorProvider customInterceptors) 
    {
        super(endpointUri, address, iti47Component, customInterceptors);
    }

    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new Hl7v3ClientFactory(
                ITI_47, 
                getServiceUrl(), 
                getCustomInterceptors());
        return new Hl7v3ContinuationAwareProducer(
                this,
                clientFactory,
                ITI_47,
                isSupportContinuation(),
                isAutoCancel(),
                isValidationOnContinuation());
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new Hl7v3ServiceFactory(
                ITI_47, 
                getServiceAddress(),
                getCustomInterceptors());

        Iti47PortType portTypeImpl = isSupportContinuation() ?
                new Iti47ContinuationAwareService(
                        getContinuationStorage(),
                        getDefaultContinuationThreshold(),
                        isValidationOnContinuation()) :
                new Iti47Service();

        ServerFactoryBean serverFactory = serviceFactory.createServerFactory(portTypeImpl);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
