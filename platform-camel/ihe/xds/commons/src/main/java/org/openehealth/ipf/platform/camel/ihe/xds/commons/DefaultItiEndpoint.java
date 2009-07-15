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

import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.util.UnsafeUriCharactersEncoder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Camel endpoint used to create producers and consumers based on webservice calls.
 * @author Jens Riemschneider
 */
public abstract class DefaultItiEndpoint extends DefaultEndpoint<Exchange> {
    private static final String ENDPOINT_PROTOCOL = "http://";

    private final String serviceAddress;
    private final String serviceUrl;
    
    /**
     * Whether we should audit or not -- an URL parameter.  
     * Equals to <code>true</code> per default.
     */
    private boolean audit = true;
    
    /**
     * Whether the system is allowed to write down audit items
     * even if if was not able to collect all necessary data --
     * an URL parameter, targeted on debug purposes only,
     * defaults to <code>false</code>. 
     */
    private boolean allowIncompleteAudit = false;


    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the URI of the endpoint.
     * @param address
     *          the endpoint address from the URI.
     * @param component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    protected DefaultItiEndpoint(String endpointUri, String address, Component<Exchange> component) throws URISyntaxException {
        super(endpointUri, component);

        if (isProducer(endpointUri)) {
            this.serviceUrl = ENDPOINT_PROTOCOL + address;
            this.serviceAddress = null;
        }
        else {
            this.serviceUrl = null;
            this.serviceAddress = "/" + address;
        }
    }

    public boolean isSingleton() {
        return true;
    }

    private static boolean isProducer(String endpointUri) throws URISyntaxException {
        URI u = new URI(UnsafeUriCharactersEncoder.encode(endpointUri));
        String path = u.getSchemeSpecificPart();
        return path.startsWith("//");
    }

    /**
     * Returns the URL of the service.
     * <p>
     * The URL is derived from the endpoint URI defined in the constructor. If the
     * URI does not represent a producer, this method throws an exception.
     * @return the service URL.
     * @throws IllegalStateException
     *          if the endpoint only supports consuming messages.
     */
    public String getServiceUrl() {
        if (serviceUrl == null) {
            throw new IllegalStateException("Endpoint does not support calling a service. Endpoint URI = " + getEndpointUri());
        }
        return serviceUrl;
    }

    /**
     * Returns the address of the service.
     * <p>
     * The address is derived from the endpoint URI defined in the constructor. If the
     * URI does not represent a consumer, this method throws an exception.
     * @return the service address.
     * @throws IllegalStateException
     *          if the endpoint only supports producing messages.
     */
    public String getServiceAddress() {
        if (serviceAddress == null) {
            throw new IllegalStateException("Endpoint does not support publishing a service. Endpoint URI = " + getEndpointUri());
        }
        return serviceAddress;
    }



    /* ----- automatically generated getters and setters ----- */
    
    public boolean isAudit() {
        return audit;
    }
    
    public void setAudit(boolean audit) {
        this.audit = audit;
    }

    public void setAllowIncompleteAudit(boolean allowIncompleteAudit) {
        this.allowIncompleteAudit = allowIncompleteAudit;
    }

    public boolean isAllowIncompleteAudit() {
        return allowIncompleteAudit;
    }
}