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

import static org.apache.commons.lang.Validate.notNull;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.lang.Validate;
import org.openehealth.ipf.commons.ihe.xds.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.xds.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ItiAuditStrategy;

/**
 * Camel component used to create process incoming exchanges based on webservice calls.
 * @param <T>
 *          the type of web-service used.
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class DefaultItiConsumer extends DefaultConsumer {
    private final ItiServiceFactory serviceFactory;

    /**
     * Constructs the consumer.
     *
     * @param endpoint
     *          the endpoint representation in Camel.
     * @param processor
     *          the processor to start processing incoming exchanges.
     * @param serviceInfo
     *          info describing the service.
     * @param auditStrategy
     *          the strategy to use for auditing.
     * @param serviceImplClass
     *          the class of the service implementation. 
     */
    public DefaultItiConsumer(DefaultItiEndpoint endpoint, Processor processor, ItiServiceInfo serviceInfo, ItiAuditStrategy auditStrategy, Class<?> serviceImplClass) {
        super(endpoint, processor);
        notNull(serviceInfo, "serviceInfo cannot be null");
        Validate.notNull(endpoint.getServiceAddress());
        
        this.serviceFactory = new ItiServiceFactory(serviceInfo, auditStrategy, endpoint.getServiceAddress());

        DefaultItiWebService service = (DefaultItiWebService) serviceFactory.createService(serviceImplClass);
        service.setConsumer(this);
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
}
