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
package org.openehealth.ipf.platform.camel.ihe.ws;

import static org.apache.commons.lang.Validate.notNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

/**
 * Camel producer used to make calls to a Web Service.
 * 
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public abstract class DefaultItiProducer<InType, OutType> extends DefaultProducer {
    private static final Log log = LogFactory.getLog(DefaultItiProducer.class);

    private final ItiClientFactory clientFactory;
    private final Class<InType> inTypeClass;

    /**
     * Constructs the producer.
     * 
     * @param endpoint
     *          the endpoint that creates this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.              
     */
    @SuppressWarnings("unchecked")
    public DefaultItiProducer(DefaultItiEndpoint endpoint, ItiClientFactory clientFactory) {
        super(endpoint);
        notNull(clientFactory, "clientFactory cannot be null");
        this.clientFactory = clientFactory;
        
        // get Class object for the input type
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type type = genericSuperclass.getActualTypeArguments()[0];
        this.inTypeClass = (Class<InType>) type;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        log.debug("Calling webservice on '" + getServiceInfo().getServiceName() + "' with " + exchange);
        
        InType body = exchange.getIn().getBody(inTypeClass);
        OutType result = callService(getClient(), body);
        Exchanges.resultMessage(exchange).setBody(result);
    }

    /**
     * Sends the exchange to the webservice.
     * 
     * @param exchange
     *            the exchange that is send and changed with the result of the call.
     */
    //protected abstract void callService(Exchange exchange);
    protected abstract OutType callService(Object client, InType body);

    /**
     * Retrieves the client stub for the webservice.
     * <p>
     * This method caches the client stub instance and therefore requires thread
     * synchronization.
     * 
     * @return the client stub.
     */
    protected Object getClient() {
        return clientFactory.getClient();
    }

    /**
     * @return the info describing the web-service.
     */
    public ItiServiceInfo getServiceInfo() {
        return clientFactory.getServiceInfo();
    }
}
