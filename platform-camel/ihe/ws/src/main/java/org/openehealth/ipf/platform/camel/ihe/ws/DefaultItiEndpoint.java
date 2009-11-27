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

import org.apache.camel.Component;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * Camel endpoint used to create producers and consumers based on webservice calls.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public abstract class DefaultItiEndpoint extends DefaultEndpoint {
    private static final String ENDPOINT_PROTOCOL = "http://";
    private static final String ENDPOINT_PROTOCOL_SECURE = "https://";

    private final String address;
    
    private String serviceAddress;
    private String serviceUrl;    
    private boolean secure;
    private boolean audit = true;
    private boolean allowIncompleteAudit = false;
    private boolean soap11 = false;

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the URI of the endpoint.
     * @param address
     *          the endpoint address from the URI.
     * @param component
     *          the component creating this endpoint.
     */
    protected DefaultItiEndpoint(String endpointUri, String address, Component component) {
        super(endpointUri, component);
        this.address = address;
        configure();
    }

    private void configure() {
        this.serviceUrl = (secure ? ENDPOINT_PROTOCOL_SECURE : ENDPOINT_PROTOCOL) + address;
        this.serviceAddress = "/" + address;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * Returns the URL of the service.
     * <p>
     * The URL is derived from the endpoint URI defined in the constructor. If the
     * URI does not represent a producer, this method throws an exception.
     * @return the service URL.
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * Returns the address of the service.
     * <p>
     * The address is derived from the endpoint URI defined in the constructor. If the
     * URI does not represent a consumer, this method throws an exception.
     * @return the service address.
     */
    public String getServiceAddress() {
        return serviceAddress;
    }


    /**
     * @return <code>true</code> if auditing is turned on. <code>true</code> by default.
     */
    public boolean isAudit() {
        return audit;
    }
    
    /**
     * @param audit
     *          <code>true</code> if auditing is turned on.
     */
    public void setAudit(boolean audit) {
        this.audit = audit;
    }

    /**
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available.
     */
    public void setAllowIncompleteAudit(boolean allowIncompleteAudit) {
        this.allowIncompleteAudit = allowIncompleteAudit;
    }

    /**
     * @return <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. Defaults to <code>false</code>.
     */
    public boolean isAllowIncompleteAudit() {
        return allowIncompleteAudit;
    }

    /**
     * @return Whether SOAP 1.1 should be used instead of SOAP 1.2 for XDS.b 
     *          transactions. Default is <code>false</code>. Does not have any 
     *          meaning for XDS.a transactions.
     */
    public boolean isSoap11() {
        return soap11;
    }

    /**
     * @param soap11
     *          Whether SOAP 1.1 should be used instead of SOAP 1.2 for XDS.b 
     *          transactions. Does not have any meaning for XDS.a transactions.
     */
    public void setSoap11(boolean soap11) {
        this.soap11 = soap11;
    }

    /**
     * @return <code>true</code> if https should be used instead of http. Defaults
     *          to <code>false</code>.
     *          
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * @param secure
     *          <code>true</code> if https should be used instead of http.
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
        configure();
    }
}