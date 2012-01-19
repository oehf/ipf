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

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.WsRejectionHandlingStrategy;

/**
 * Camel endpoint used to create producers and consumers based on webservice calls.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@ManagedResource(description = "Managed IPF eHealth Web Service Endpoint")
public abstract class AbstractWsEndpoint<ComponentType extends AbstractWsComponent<?>>
    extends DefaultEndpoint {

    private static final String ENDPOINT_PROTOCOL = "http://";
    private static final String ENDPOINT_PROTOCOL_SECURE = "https://";

    /**
     * Name of incoming Camel header where the user should store the URL
     * of asynchronous response endpoint (WS-Addressing header "ReplyTo").  
     */
    public static final String WSA_REPLYTO_HEADER_NAME =
            AbstractWsEndpoint.class.getName() + ".REPLY_TO";
    
    /**
     * Name of Camel message header where the user should store 
     * the optional correlation key.  
     */
    public static final String CORRELATION_KEY_HEADER_NAME =
            AbstractWsEndpoint.class.getName() + ".CORRELATION_KEY";
    
    /**
     * Name of Camel message header where incoming HTTP headers
     * will be stored as a <code>Map&lt;String, String&gt;</code>.
     */
    public static final String INCOMING_HTTP_HEADERS =
            AbstractWsEndpoint.class.getName() + ".INCOMING_HTTP_HEADERS";

    /**
     * Name of Camel message header from where additional user-defined HTTP 
     * headers will be taken as a <code>Map&lt;String, String&gt;</code>.
     */
    public static final String OUTGOING_HTTP_HEADERS =
            AbstractWsEndpoint.class.getName() + ".OUTGOING_HTTP_HEADERS";

    /**
     * Name of Camel message header where incoming SOAP headers
     * will be stored as a <code>Map&lt;{@link QName}, {@link Header}&gt;</code>.
     */
    public static final String INCOMING_SOAP_HEADERS =
            AbstractWsEndpoint.class.getName() + ".INCOMING_SOAP_HEADERS";

    /**
     * Name of Camel message header from where additional user-defined HTTP 
     * headers will be taken as a <code>List&lt;{@link Header}&gt;</code>.
     */
    public static final String OUTGOING_SOAP_HEADERS =
            AbstractWsEndpoint.class.getName() + ".OUTGOING_SOAP_HEADERS";

    private final String address;

    private String serviceAddress;
    private String serviceUrl;    
    private boolean secure;
    private boolean audit = true;
    private boolean allowIncompleteAudit = false;
    private AsynchronyCorrelator correlator = null;
    private InterceptorProvider customInterceptors = null;
    private String homeCommunityId = null;
    private WsRejectionHandlingStrategy rejectionHandlingStrategy = null;

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
    protected AbstractWsEndpoint(
            String endpointUri,
            String address,
            ComponentType component,
            InterceptorProvider customInterceptors) 
    {
        super(endpointUri, component);
        this.address = address;
        this.customInterceptors = customInterceptors;
        configure();
    }

    private void configure() {
        serviceUrl = (secure ? ENDPOINT_PROTOCOL_SECURE : ENDPOINT_PROTOCOL) + address;
        serviceAddress = "/" + address;
    }

    @Override
    @ManagedAttribute
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
    @ManagedAttribute(description = "Service Address")
    public String getServiceAddress() {
        return serviceAddress;
    }

    /**
     * @return <code>true</code> if auditing is turned on. <code>true</code> by default.
     */
    @ManagedAttribute(description = "Audit Enabled")
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
    @ManagedAttribute(description = "Incomplete Audit Allowed")
    public boolean isAllowIncompleteAudit() {
        return allowIncompleteAudit;
    }

    /**
     * @return <code>true</code> if https should be used instead of http. Defaults
     *          to <code>false</code>.
     */
    @ManagedAttribute(description = "Security Enabled")
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
    @ManagedAttribute(description = "HomeCommunityId")
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

    /**
     * @return
     *      rejection handling strategy, if any configured.
     */
    public WsRejectionHandlingStrategy getRejectionHandlingStrategy() {
        return rejectionHandlingStrategy;
    }

    /**
     * @param rejectionHandlingStrategy
     *      a rejection handling strategy instance.
     */
    public void setRejectionHandlingStrategy(WsRejectionHandlingStrategy rejectionHandlingStrategy) {
        this.rejectionHandlingStrategy = rejectionHandlingStrategy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ComponentType getComponent() {
        return (ComponentType) super.getComponent();
    }

    /**
     * @return JAX-WS client object factory.
     */
    public abstract JaxWsClientFactory getJaxWsClientFactory();

    /**
     * @return JAX-WS service object factory.
     */
    public abstract JaxWsServiceFactory getJaxWsServiceFactory();


    @Override
    public Producer createProducer() throws Exception {
        return getComponent().getProducer(this, getJaxWsClientFactory());
    }


    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        AbstractWebService serviceInstance = getComponent().getServiceInstance(this);
        ServerFactoryBean serverFactory = getJaxWsServiceFactory().createServerFactory(serviceInstance);
        Server server = serverFactory.create();
        AbstractWebService service = (AbstractWebService) serverFactory.getServiceBean();
        return new DefaultWsConsumer(this, processor, service, server);
    }


    //special managed attributes
    /**
     * @return <code>true</code> if WS-Addressing enabled.
     */
    @ManagedAttribute(description = "Addressing Enabled")
    public boolean isAddressing() {
        return getComponent().getWsTransactionConfiguration().isAddressing();
    }

    /**
     * @return <code>true</code> if MTOM enabled.
     */
    @ManagedAttribute(description = "Mtom Enabled")
    public boolean isMtom() {
        return getComponent().getWsTransactionConfiguration().isMtom();
    }

    /**
     * @return <code>true</code> if SOAP With Attachments Output enabled.
     */
    @ManagedAttribute(description = "SOAP With Attachments Output Enabled")
    public boolean isSwaOutSupport() {
        return getComponent().getWsTransactionConfiguration().isSwaOutSupport();
    }


}