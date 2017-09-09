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

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.cxf.common.i18n.Exception;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.core.URN;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.WsRejectionHandlingStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpoint;

import javax.net.ssl.HostnameVerifier;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

/**
 * Camel endpoint used to create producers and consumers based on webservice calls.
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@ManagedResource(description = "Managed IPF eHealth Web Service Endpoint")
public abstract class AbstractWsEndpoint<
        AuditDatasetType extends WsAuditDataset,
        ConfigType extends WsTransactionConfiguration>
        extends DefaultEndpoint implements AuditableEndpoint<AuditDatasetType> {

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
     * headers will be taken as a <code>List&lt;{@link Header}&gt;</code>
     * or <code>Map&lt;{@link QName}, {@link Header}&gt;</code>.
     */
    public static final String OUTGOING_SOAP_HEADERS =
            AbstractWsEndpoint.class.getName() + ".OUTGOING_SOAP_HEADERS";

    private final String address;

    private String serviceAddress;
    private String serviceUrl;

    private boolean audit = true;
    private AsynchronyCorrelator<AuditDatasetType> correlator = null;
    private InterceptorProvider customInterceptors = null;
    private String homeCommunityId = null;
    private WsRejectionHandlingStrategy rejectionHandlingStrategy = null;
    private List<AbstractFeature> features;
    private List<String> schemaLocations;
    private Class<? extends AbstractWebService> serviceClass;
    private Map<String, Object> properties;

    private boolean secure;
    private SSLContextParameters sslContextParameters;
    private HostnameVerifier hostnameVerifier;
    private String username;
    private String password;


    /**
     * Constructs the endpoint.
     *
     * @param endpointUri        the URI of the endpoint.
     * @param address            the endpoint address from the URI.
     * @param component          the component creating this endpoint.
     * @param customInterceptors user-defined set of additional CXF interceptors.
     * @param features           user-defined list of CXF features.
     */
    protected AbstractWsEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<AuditDatasetType, ConfigType, ? extends WsInteractionId> component,
            InterceptorProvider customInterceptors,
            List<AbstractFeature> features,
            List<String> schemaLocations,
            Map<String, Object> properties,
            Class<? extends AbstractWebService> serviceClass) {
        super(endpointUri, component);
        this.address = address;
        this.customInterceptors = customInterceptors;
        this.features = features;
        this.schemaLocations = schemaLocations;
        this.properties = properties;
        this.serviceClass = serviceClass;
        configure();
    }

    private void configure() {
        serviceUrl = (isSecure() ? ENDPOINT_PROTOCOL_SECURE : ENDPOINT_PROTOCOL) + address;
        serviceAddress = "/" + address;
    }

    /**
     * Constructs and returns a transaction-specific service class instance
     * for the given endpoint.
     *
     * @return service class instance for the given endpoint.
     */
    public AbstractWebService getServiceInstance() {
        AbstractWebService service = getCustomServiceInstance(this);
        if (service == null) {
            if (serviceClass != null) {
                try {
                    return serviceClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("Could not instantiate service of type " + serviceClass, e);
                }
            } else {
                throw new RuntimeException("Could not instantiate service of for endpoint of class " + getClass());
            }
        }
        return service;
    }

    /**
     * Returns a new instance of a service class.
     * Overwrite this method if a simple call to {@link Class#newInstance()} is not sufficient.
     *
     * @param endpoint this endpoint as paramater
     * @return service class instance
     */
    protected AbstractWebService getCustomServiceInstance(AbstractWsEndpoint<AuditDatasetType, ConfigType> endpoint) {
        return null;
    }

    @Override
    public AuditStrategy<AuditDatasetType> getClientAuditStrategy() {
        return getComponent().getClientAuditStrategy();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getServerAuditStrategy() {
        return getComponent().getServerAuditStrategy();
    }

    @Override
    @ManagedAttribute
    public boolean isSingleton() {
        return true;
    }

    /**
     * Returns the URL of the service.
     * <p/>
     * The URL is derived from the endpoint URI defined in the constructor. If the
     * URI does not represent a producer, this method throws an exception.
     *
     * @return the service URL.
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * Returns the address of the service.
     * <p/>
     * The address is derived from the endpoint URI defined in the constructor. If the
     * URI does not represent a consumer, this method throws an exception.
     *
     * @return the service address.
     */
    @ManagedAttribute(description = "Service Address")
    public String getServiceAddress() {
        return serviceAddress;
    }

    /**
     * @return <code>true</code> if auditing is turned on. <code>true</code> by default.
     */
    @Override
    @ManagedAttribute(description = "Audit Enabled")
    public boolean isAudit() {
        return audit;
    }

    /**
     * @param audit <code>true</code> if auditing is turned on.
     */
    public void setAudit(boolean audit) {
        this.audit = audit;
    }

    /**
     * @return <code>true</code> if https should be used instead of http. Defaults
     * to <code>false</code>.
     */
    @ManagedAttribute(description = "Security Enabled")
    public boolean isSecure() {
        return secure;
    }

    /**
     * @param secure <code>true</code> if https should be used instead of http.
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
        configure();
    }

    public String getUsername() {
        return username;
    }

    @ManagedAttribute(description = "Basic Authentication Username")
    public void setUsername(String username) {
        this.username = username;
    }

    @ManagedAttribute(description = "Basic Authentication Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SSLContextParameters getSslContextParameters() {
        return sslContextParameters;
    }

    public void setSslContextParameters(SSLContextParameters sslContextParameters) {
        this.sslContextParameters = sslContextParameters;
        setSecure(secure || (sslContextParameters != null));
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    /**
     * Configures the asynchrony correlator for this endpoint.
     */
    public void setCorrelator(AsynchronyCorrelator<AuditDatasetType> correlator) {
        this.correlator = correlator;
    }

    /**
     * Returns the correlator.
     */
    public AsynchronyCorrelator<AuditDatasetType> getCorrelator() {
        return correlator;
    }

    /**
     * Returns custom interceptors configured for this endpoint.
     */
    public InterceptorProvider getCustomInterceptors() {
        return customInterceptors;
    }

    /**
     * @return homeCommunityId of this endpoint.
     */
    @ManagedAttribute(description = "HomeCommunityId")
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * Configures homeCommunityId for this endpoint.
     *
     * @param homeCommunityId homeCommunityId in format "urn:oid:1.2.3.4.5".
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Configures homeCommunityId for this endpoint.
     *
     * @param urn homeCommunityId in format "urn:oid:1.2.3.4.5".
     */
    public void setHomeCommunityId(URN urn) {
        this.homeCommunityId = urn.toString();
    }

    /**
     * @return rejection handling strategy, if any configured.
     */
    public WsRejectionHandlingStrategy getRejectionHandlingStrategy() {
        return rejectionHandlingStrategy;
    }

    /**
     * @param rejectionHandlingStrategy a rejection handling strategy instance.
     */
    public void setRejectionHandlingStrategy(WsRejectionHandlingStrategy rejectionHandlingStrategy) {
        this.rejectionHandlingStrategy = rejectionHandlingStrategy;
    }

    /**
     * @return CXF features configured for this endpoint.
     */
    public List<AbstractFeature> getFeatures() {
        return features;
    }

    /**
     * @return CXF schema locations configured for this endpoint.
     */
    public List<String> getSchemaLocations() {
        return schemaLocations;
    }

    /**
     * @return CXF schema locations configured for this endpoint.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AbstractWsComponent<AuditDatasetType, ConfigType, ? extends WsInteractionId> getComponent() {
        return (AbstractWsComponent<AuditDatasetType, ConfigType, WsInteractionId<ConfigType>>) super.getComponent();
    }

    /**
     * @return JAX-WS client object factory.
     */
    public abstract JaxWsClientFactory<AuditDatasetType> getJaxWsClientFactory();

    /**
     * @return JAX-WS service object factory.
     */
    public abstract JaxWsServiceFactory<AuditDatasetType> getJaxWsServiceFactory();


    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        AbstractWebService serviceInstance = getServiceInstance();
        ServerFactoryBean serverFactory = getJaxWsServiceFactory().createServerFactory(serviceInstance);
        if (features != null) {
            serverFactory.getFeatures().addAll(features);
        }
        if (schemaLocations != null) {
            if (serverFactory.getSchemaLocations() == null) {
                serverFactory.setSchemaLocations(schemaLocations);
            } else {
                serverFactory.getSchemaLocations().addAll(schemaLocations);
            }
        }
        if (properties != null) {
            if (serverFactory.getProperties() == null) {
                serverFactory.setProperties(properties);
            } else {
                serverFactory.getProperties().putAll(properties);
            }
        }

        Server server = serverFactory.create();
        AbstractWebService service = (AbstractWebService) serverFactory.getServiceBean();
        return new DefaultWsConsumer<>(this, processor, service, server);
    }

    @Override
    public Producer createProducer() throws java.lang.Exception {
        return getProducer(this, getJaxWsClientFactory());
    }

    /**
     * Constructs and returns a transaction-specific Camel producer instance
     *
     * @param clientFactory JAX-WS client factory instance.
     * @return Camel producer instance.
     * @since 3.1
     */
    public abstract AbstractWsProducer<AuditDatasetType, ConfigType, ?, ?> getProducer(AbstractWsEndpoint<AuditDatasetType, ConfigType> endpoint, JaxWsClientFactory<AuditDatasetType> clientFactory);


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