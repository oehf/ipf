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
package org.openehealth.ipf.platform.camel.ihe.xds.commons;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.bus.CXFBusImpl;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.WsSecurityUnderstandingInInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditDatasetEnrichmentInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditFinalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ServerPayloadExtractorInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditStrategy;

import java.util.Arrays;
import java.util.Collections;

/**
 * Camel component used to create process incoming exchanges based on webservice calls.
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public abstract class DefaultItiConsumer extends DefaultConsumer<Exchange> implements Auditable {
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
    public DefaultItiConsumer(DefaultItiEndpoint endpoint, Processor processor, DefaultItiWebService webService, ItiServiceInfo<?> serviceInfo) {
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
    protected void publishWebService(DefaultItiWebService webService, ItiServiceInfo<?> info) {
        Validate.notNull(endpoint.getServiceAddress());
        Validate.notNull(info);
        Validate.notNull(webService);

        webService.setConsumer(this);

        ServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        configureService(svrFactory, webService, info);
        configureBinding(svrFactory, info);
        configureInterceptors(svrFactory);        
        configureBus(svrFactory);
        
        svrFactory.create();

        log.debug("Published webservice endpoint for: " + info.getServiceName());
    }

    private void configureBus(ServerFactoryBean svrFactory) {
        CXFBusImpl bus = (CXFBusImpl) svrFactory.getBus();
        bus.setFeatures(Arrays.<AbstractFeature>asList(new WSAddressingFeature()));
    }

    private void configureService(ServerFactoryBean svrFactory, Object webService, ItiServiceInfo<?> info) {
        svrFactory.setServiceClass(info.getServiceClass());
        svrFactory.setServiceName(info.getServiceName());
        svrFactory.setWsdlLocation(info.getWsdlLocation());
        svrFactory.setAddress(endpoint.getServiceAddress());
        svrFactory.setServiceBean(webService);
        svrFactory.getFeatures().add(new WSAddressingFeature());
        if (info.isMtom()) {
            svrFactory.setProperties(Collections.<String, Object>singletonMap("mtom-enabled", "true"));
        }
    }

    private void configureBinding(ServerFactoryBean svrFactory, ItiServiceInfo<?> info) {
        SoapBindingConfiguration bindingConfig = new SoapBindingConfiguration();
        bindingConfig.setBindingName(info.getBindingName());
        svrFactory.setBindingConfig(bindingConfig);
    }

    private void configureInterceptors(ServerFactoryBean svrFactory) {
        // install auditing-related interceptors if the user has not switched auditing off
        if (endpoint.isAudit()) {
            AuditStrategy auditStrategy = createAuditStrategy(endpoint.isAllowIncompleteAudit());
            svrFactory.getInInterceptors().add(new ServerPayloadExtractorInterceptor(auditStrategy));
            svrFactory.getInInterceptors().add(new AuditDatasetEnrichmentInterceptor(auditStrategy, true));
    
            AuditFinalInterceptor auditOutInterceptor = new AuditFinalInterceptor(auditStrategy, true);
            svrFactory.getOutInterceptors().add(auditOutInterceptor);
            svrFactory.getOutFaultInterceptors().add(auditOutInterceptor);
        }
        
        // protocol-related interceptors
        svrFactory.getInInterceptors().add(new WsSecurityUnderstandingInInterceptor());
    }
}
