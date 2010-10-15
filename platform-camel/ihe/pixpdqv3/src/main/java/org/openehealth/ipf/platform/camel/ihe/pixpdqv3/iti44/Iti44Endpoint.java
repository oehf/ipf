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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti44;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti44.Iti44PixPortType;
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti44.Iti44XdsPortType;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

import javax.xml.namespace.QName;

/**
 * The Camel endpoint for the ITI-44 transaction.
 */
public class Iti44Endpoint extends DefaultItiEndpoint {
    private static final String[][] REQUEST_VALIDATION_PROFILES = new String[][] {
            new String[] {"PRPA_IN201301UV02", null},
            new String[] {"PRPA_IN201301UV02", null},
            new String[] {"PRPA_IN201301UV02", null}
    };

    private static final String[][] RESPONSE_VALIDATION_PROFILES = new String[][] {
            new String[] {"MCCI_IN000002UV01", null}
    };

    private static final String NS_URI_PIX = "urn:ihe:iti:pixv3:2007";
    public static final Hl7v3ServiceInfo ITI_44_PIX = new Hl7v3ServiceInfo(
            new QName(NS_URI_PIX, "PIXManager_Service", "ihe"),
            Iti44PixPortType.class,
            new QName(NS_URI_PIX, "PIXManager_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti44/iti44-pix-raw.wsdl",
            REQUEST_VALIDATION_PROFILES,
            RESPONSE_VALIDATION_PROFILES);

    private static final String NS_URI_XDS = "urn:ihe:iti:xds-b:2007";
    public final static Hl7v3ServiceInfo ITI_44_XDS = new Hl7v3ServiceInfo(
            new QName(NS_URI_XDS, "DocumentRegistry_Service", "ihe"),
            Iti44XdsPortType.class,
            new QName(NS_URI_XDS, "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti44/iti44-xds-raw.wsdl",
            REQUEST_VALIDATION_PROFILES,
            RESPONSE_VALIDATION_PROFILES);

    private final boolean isPix;


    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param component
     *          the component creating this endpoint.
     * @param customInterceptors 
     */
    public Iti44Endpoint(
            boolean isPix,
            String endpointUri, 
            String address, 
            Component component,
            InterceptorProvider customInterceptors) 
    {
        super(endpointUri, address, component, customInterceptors);
        this.isPix = isPix;
    }

    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new Hl7v3ClientFactory(
                isPix ? ITI_44_PIX : ITI_44_XDS,
                getServiceUrl(), 
                getCustomInterceptors());
        return new Iti44Producer(this, clientFactory);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new Hl7v3ServiceFactory(
                isPix ? ITI_44_PIX : ITI_44_XDS,
                getServiceAddress(),
                getCustomInterceptors());
        ServerFactoryBean serverFactory =
            serviceFactory.createServerFactory(Iti44Service.class);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
