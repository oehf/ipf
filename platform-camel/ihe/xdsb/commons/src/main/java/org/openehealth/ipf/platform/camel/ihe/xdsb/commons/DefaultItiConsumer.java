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
package org.openehealth.ipf.platform.camel.ihe.xdsb.commons;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.bus.CXFBusImpl;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;

import java.util.Arrays;
import java.util.Collections;

/**
 * Camel component used to create process incoming exchanges based on webservice calls.
 *
 * @author Jens Riemschneider
 */
public class DefaultItiConsumer extends DefaultConsumer<Exchange> {
    private static final Log log = LogFactory.getLog(DefaultItiConsumer.class);

    private final DefaultItiEndpoint endpoint;

    /**
     * Constructs the consumer.
     *
     * @param endpoint
     *          the endpoint representation in Camel.
     * @param processor
     *          the processor to start processing incoming exchanges.
     * @param webService
     *          the service that this consumer is using.
     * @param serviceInfo
     *          info describing the service.
     */
    public DefaultItiConsumer(DefaultItiEndpoint endpoint, Processor processor, DefaultItiWebService webService, ItiServiceInfo serviceInfo) {
        super(endpoint, processor);
        this.endpoint = endpoint;

        publishWebService(webService, serviceInfo);
    }

    /**
     * Processes an exchange with the processor configured in the constructor.
     *
     * @param exchange
     *          the exchange to process.
     */
    public void process(Exchange exchange) {
        try {
            getProcessor().process(exchange);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Publishes a webservice with the standard ITI transaction configuration.
     * @param webService
     *          a webservice instance that is to be used to create the webservice endpoint.
     * @param info
     *          webservice info describing various information about the service.
     */
    protected void publishWebService(DefaultItiWebService webService, ItiServiceInfo info) {
        Validate.notNull(endpoint.getServiceAddress());
        Validate.notNull(info);
        Validate.notNull(webService);

        webService.setConsumer(this);

        SoapBindingConfiguration bindingConfig = new SoapBindingConfiguration();
        bindingConfig.setBindingName(info.getBindingName());

        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setServiceClass(info.getServiceClass());
        svrFactory.setServiceName(info.getServiceName());
        svrFactory.setWsdlLocation(info.getWsdlLocation());
        svrFactory.setAddress(endpoint.getServiceAddress());
        svrFactory.setServiceBean(webService);
        svrFactory.setBindingConfig(bindingConfig);
        svrFactory.getFeatures().add(new WSAddressingFeature());
        if (info.isMtom()) {
            svrFactory.setProperties(Collections.<String, Object>singletonMap("mtom-enabled", "true"));
        }
        CXFBusImpl bus = (CXFBusImpl) svrFactory.getBus();
        bus.setFeatures(Arrays.<AbstractFeature>asList(new WSAddressingFeature()));
        svrFactory.create();

        log.debug("Published webservice endpoint for: " + info.getServiceName());
    }
}
