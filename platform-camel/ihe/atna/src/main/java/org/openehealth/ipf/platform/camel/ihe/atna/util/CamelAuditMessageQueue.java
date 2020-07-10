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
    private InetAddress destinationAddress;
    private int destinationPort;

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
    public void setEndpointUri(String endpointUri) throws URISyntaxException, UnknownHostException {
        endpointUriString = endpointUri;
        endpointUriObject = new URI(endpointUri);
        var host = endpointUriObject.getHost();
        destinationAddress = InetAddress.getByName(host == null ? "0.0.0.0" : host);
        destinationPort = endpointUriObject.getPort();
    }

    public void init() {
        producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.start();
    }

    public void destroy() {
        producerTemplate.stop();
    }


    @Override
    public void audit(AuditContext auditContext, AuditMessage... auditMessages) {
        for (var m : auditMessages) {
            var headers = new HashMap<String, Object>();
            headers.put(HEADER_NAMESPACE + ".destination.address", destinationAddress.getHostAddress());
            headers.put(HEADER_NAMESPACE + ".destination.port", destinationPort);
            headers.put(X_IPF_ATNA_TIMESTAMP, auditContext.getAuditMetadataProvider().getTimestamp());
            headers.put(X_IPF_ATNA_HOSTNAME, auditContext.getAuditMetadataProvider().getHostname());
            headers.put(X_IPF_ATNA_PROCESSID, auditContext.getAuditMetadataProvider().getProcessID());
            headers.put(X_IPF_ATNA_APPLICATION, auditContext.getSendingApplication());
            producerTemplate.sendBodyAndHeaders(endpointUriString, m, headers);
        }
    }

    private InetAddress getDestinationAddress() throws UnknownHostException {
        var host = endpointUriObject.getHost();
        return InetAddress.getByName(host == null ? "0.0.0.0" : host);
    }

}
