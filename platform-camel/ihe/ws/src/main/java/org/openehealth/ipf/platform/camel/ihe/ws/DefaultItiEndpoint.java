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

import javax.xml.namespace.QName;

import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.ManagementAware;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.platform.camel.ihe.ws.mbean.ManagedWsItiEndpoint;

/**
 * Camel endpoint used to create producers and consumers based on webservice calls.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@SuppressWarnings("deprecation")
public abstract class DefaultItiEndpoint<C extends ItiServiceInfo> extends DefaultEndpoint
    implements ManagementAware<DefaultItiEndpoint<C>> {

    private static final String ENDPOINT_PROTOCOL = "http://";
    private static final String ENDPOINT_PROTOCOL_SECURE = "https://";

    /**
     * Name of incoming Camel header where the user should store the URL
     * of asynchronous response endpoint (WS-Addressing header "ReplyTo").  
     */
    public static final String WSA_REPLYTO_HEADER_NAME = "ipf.wsa.ReplyTo";
    
    /**
     * Name of Camel message header where the user should store 
     * the optional correlation key.  
     */
    public static final String CORRELATION_KEY_HEADER_NAME = "ipf.correlation.key";
    
    /**
     * Name of Camel message header where incoming HTTP headers
     * will be stored as a <code>Map&lt;String, String&gt</code>.
     */
    public static final String INCOMING_HTTP_HEADERS = "ipf.ihe.http.headers.incoming";

    /**
     * Name of Camel message header from where additional user-defined HTTP 
     * headers will be taken as a <code>Map&lt;String, String&gt</code>.
     */
    public static final String OUTGOING_HTTP_HEADERS = "ipf.ihe.http.headers.outgoing";
    
    /**
     * Name of Camel message header where incoming SOAP headers
     * will be stored as a <code>Map&lt;{@link QName}, {@link Header}&gt</code>.
     */
    public static final String INCOMING_SOAP_HEADERS = "ipf.ihe.soap.headers.incoming";

    /**
     * Name of Camel message header from where additional user-defined HTTP 
     * headers will be taken as a <code>List&lt;{@link Header}&gt</code>.
     */
    public static final String OUTGOING_SOAP_HEADERS = "ipf.ihe.soap.headers.outgoing";
    
    private final String address;

    private String serviceAddress;
    private String serviceUrl;    
    private boolean secure;
    private boolean audit = true;
    private boolean allowIncompleteAudit = false;
    private AsynchronyCorrelator correlator = null;
    private InterceptorProvider customInterceptors = null;
    private String homeCommunityId = null;

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the URI of the endpoint.
     * @param address
     *          the endpoint address from the URI.
     * @param component
     *          the component creating this endpoint.
     * @param customInterceptors
     *          user-defined set of additional CXF interceptors.
     */
    protected DefaultItiEndpoint(
            String endpointUri, 
            String address, 
            AbstractWsComponent<C> component,
            InterceptorProvider customInterceptors) 
    {
        super(endpointUri, component);
        this.address = address;
        this.customInterceptors = customInterceptors;
        configure();
    }

    /**
     * @return Web Service parameters of the component to
     *      which this endpoint belongs.
     */
    @SuppressWarnings("unchecked")
    protected C getWebServiceConfiguration() {
        return ((AbstractWsComponent<C>) getComponent()).getWebServiceConfiguration();
    }
    
    public Object getManagedObject(DefaultItiEndpoint<C> endpoint) {
        return new ManagedWsItiEndpoint(endpoint, getWebServiceConfiguration());
    }

    private void configure() {
        serviceUrl = (secure ? ENDPOINT_PROTOCOL_SECURE : ENDPOINT_PROTOCOL) + address;
        serviceAddress = "/" + address;
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

    /**
     * Configures the asynchrony correlator for this endpoint.
     */
    public void setCorrelator(AsynchronyCorrelator correlator) {
        this.correlator = correlator;
    }
    
    /**
     * Returns the correlator.
     */
    public AsynchronyCorrelator getCorrelator() {
        return correlator;
    }

    /**
     * Returns custom interceptors configured for this endpoint.
     */
    public InterceptorProvider getCustomInterceptors() {
        return customInterceptors;
    }

    /**
     * @return
     *      homeCommunityId of this endpoint.
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * Configures homeCommunityId for this endpoint.
     * @param homeCommunityId
     *      homeCommunityId in format "urn:oid:1.2.3.4.5".
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }
}