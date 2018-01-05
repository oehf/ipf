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
package org.openehealth.ipf.platform.camel.ihe.atna.util;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * An audit message sender that sends audit messages to a configured Camel
 * endpoint. When configured in a Spring application context, it is attempted to
 * auto-wire the required Camel context.
 *
 * @author Martin Krasser
 */
public class CamelAuditMessageQueue implements AuditMessageQueue {

    public static final String HEADER_NAMESPACE = "org.openehealth.ipf.platform.camel.ihe.atna";

    private CamelContext camelContext;

    private ProducerTemplate producerTemplate;

    private String endpointUriString;
    private URI endpointUriObject;

    /**
     * Sets the Camel context that contains the endpoint to send audit messages
     * to. This method needs only be used for setting the Camel context if
     * auto-wiring is not possible (e.g. in certain test environments).
     *
     * @param camelContext
     */
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    /**
     * The endpoint URI to send audit messages to.
     *
     * @param endpointUri
     * @throws URISyntaxException
     */
    public void setEndpointUri(String endpointUri) throws URISyntaxException {
        endpointUriString = endpointUri;
        endpointUriObject = new URI(endpointUri);
    }

    public void init() throws Exception {
        producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.start();
    }

    public void destroy() throws Exception {
        producerTemplate.stop();
    }


    @Override
    public void audit(AuditContext auditContext, AuditMessage... auditMessages) throws Exception {
        for (AuditMessage m : auditMessages) {
            HashMap<String, Object> headers = new HashMap<>();
            headers.put(HEADER_NAMESPACE + ".destination.address", getDestinationAddress().getHostAddress());
            headers.put(HEADER_NAMESPACE + ".destination.port", getDestinationPort());
            producerTemplate.sendBodyAndHeaders(endpointUriString, m, headers);
        }
    }


    private int getDestinationPort() {
        return endpointUriObject.getPort();
    }

    private InetAddress getDestinationAddress() throws UnknownHostException {
        String host = endpointUriObject.getHost();
        return InetAddress.getByName(host == null ? "0.0.0.0" : host);
    }

}
