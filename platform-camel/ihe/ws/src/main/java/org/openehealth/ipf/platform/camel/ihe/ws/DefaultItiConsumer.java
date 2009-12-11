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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.cxf.endpoint.Server;

import static org.apache.commons.lang.Validate.notNull;

/**
 * Camel component used to create process incoming exchanges based on webservice calls.
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class DefaultItiConsumer extends DefaultConsumer {
    private final Server server;

    /**
     * Constructs the consumer.
     * @param endpoint
     *          the endpoint representation in Camel.
     * @param processor
     *          the processor to start processing incoming exchanges.
     * @param service
     *          the service to consume messages from.
     * @deprecated It is strongly recommended to use {@link #DefaultItiConsumer(DefaultItiEndpoint, Processor, DefaultItiWebService, Server)}
     *          instead of this constructor because this version does not allow
     *          stopping/restarting of the service.
     */
    @Deprecated
    public DefaultItiConsumer(DefaultItiEndpoint endpoint, Processor processor, DefaultItiWebService service) {
        super(endpoint, processor);
        notNull(service, "service cannot be null");
        service.setConsumer(this);
        server = null;
    }

    /**
     * Constructs the consumer.
     * @param endpoint
     *          the endpoint representation in Camel.
     * @param processor
     *          the processor to start processing incoming exchanges.
     * @param service
     *          the service to consume messages from.
     * @param server
     *          the CXF server instance driving the service.
     */
    public DefaultItiConsumer(DefaultItiEndpoint endpoint, Processor processor, DefaultItiWebService service, Server server) {
        super(endpoint, processor);
        notNull(service, "service cannot be null");
        notNull(server, "server cannot be null");
        service.setConsumer(this);
        this.server = server;
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

    @Override
    protected void doStop() throws Exception {
        if (server != null) {
            server.stop();
        }
        super.doStop();
    }

    @Override
    protected void doStart() throws Exception {
        if (server != null) {
            server.start();
        }
        super.doStart();
    }
}
