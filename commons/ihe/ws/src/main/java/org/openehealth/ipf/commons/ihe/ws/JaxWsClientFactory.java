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
package org.openehealth.ipf.commons.ihe.ws;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.addressing.MAPAggregator;
import org.apache.cxf.ws.addressing.soap.MAPCodec;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.Cxf3791WorkaroundInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.FixContentTypeOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.MustUnderstandDecoratorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.ProvidedAttachmentOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutStreamSubstituteInterceptor;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vibur.objectpool.ConcurrentPool;
import org.vibur.objectpool.PoolObjectFactory;
import org.vibur.objectpool.PoolService;
import org.vibur.objectpool.util.ConcurrentLinkedQueueCollection;

/**
 * Factory for ITI Web Service stubs.
 *
 * @author Jens Riemschneider
 */
public class JaxWsClientFactory<AuditDatasetType extends WsAuditDataset> {
    private static final Logger LOG = LoggerFactory.getLogger(JaxWsClientFactory.class);

    public static final String POOL_SIZE_PROPERTY = JaxWsClientFactory.class.getName() + ".POOLSIZE";
    private static final int DEFAULT_POOL_SIZE = 100;

    protected final PoolService<Object> clientPool;
    protected final WsTransactionConfiguration<AuditDatasetType> wsTransactionConfiguration;
    protected final String serviceUrl;
    protected final InterceptorProvider customInterceptors;
    protected final List<AbstractFeature> features;
    protected final Map<String, Object> properties;
    protected final AuditStrategy<AuditDatasetType> auditStrategy;
    protected final AuditContext auditContext;
    protected final AsynchronyCorrelator<AuditDatasetType> correlator;
    protected final WsSecurityInformation securityInformation;
    protected final HTTPClientPolicy httpClientPolicy;

    /**
     * Constructs the factory.
     *
     * @param wsTransactionConfiguration the info about the Web Service.
     * @param serviceUrl                 the URL of the Web Service.
     * @param auditStrategy              client-side ATNA audit strategy.
     * @param customInterceptors         user-defined custom CXF interceptors.
     * @param correlator                 optional asynchrony correlator.
     */
    public JaxWsClientFactory(
            WsTransactionConfiguration<AuditDatasetType> wsTransactionConfiguration,
            String serviceUrl,
            AuditStrategy<AuditDatasetType> auditStrategy,
            AuditContext auditContext,
            InterceptorProvider customInterceptors,
            List<AbstractFeature> features,
            Map<String, Object> properties,
            AsynchronyCorrelator<AuditDatasetType> correlator,
            WsSecurityInformation securityInformation,
            HTTPClientPolicy httpClientPolicy)
    {
        requireNonNull(wsTransactionConfiguration, "wsTransactionConfiguration");
        this.wsTransactionConfiguration = wsTransactionConfiguration;
        this.serviceUrl = serviceUrl;
        this.auditStrategy = auditStrategy;
        this.auditContext = auditContext;
        this.customInterceptors = customInterceptors;
        this.features = features;
        this.properties = properties;
        this.correlator = correlator;
        this.securityInformation = securityInformation;
        this.httpClientPolicy = httpClientPolicy;

        int poolSize = Integer.getInteger(POOL_SIZE_PROPERTY, -1);
        clientPool = new ConcurrentPool<>(new ConcurrentLinkedQueueCollection<>(), new PortFactory(),
                0, (poolSize > 0) ? poolSize : DEFAULT_POOL_SIZE, false);
    }

    /**
     * Returns a client stub for the web-service.
     *
     * @return the client stub
     */
    public synchronized Object getClient() {
        return clientPool.take();
    }

    /**
     * @return the service info of this factory.
     */
    public WsTransactionConfiguration<AuditDatasetType> getWsTransactionConfiguration() {
        return wsTransactionConfiguration;
    }


    /**
     * Configures SOAP interceptors for the given client.
     */
    protected void configureInterceptors(Client client) {
        client.getInInterceptors().add(new Cxf3791WorkaroundInterceptor());

        // WS-Addressing-related interceptors
        if (wsTransactionConfiguration.isAddressing()) {
            var interceptor = new MustUnderstandDecoratorInterceptor();
            for (var nsUri : SoapUtils.WS_ADDRESSING_NS_URIS) {
                interceptor.addHeader(new QName(nsUri, "Action"));
            }

            client.getOutInterceptors().add(interceptor);

            var mapCodec = new MAPCodec();
            var mapAggregator = new MAPAggregator();
            client.getInInterceptors().add(mapCodec);
            client.getInInterceptors().add(mapAggregator);
            client.getInFaultInterceptors().add(mapCodec);
            client.getInFaultInterceptors().add(mapAggregator);
            client.getOutInterceptors().add(mapCodec);
            client.getOutInterceptors().add(mapAggregator);
            client.getOutFaultInterceptors().add(mapCodec);
            client.getOutFaultInterceptors().add(mapAggregator);
        }

        if (wsTransactionConfiguration.isSwaOutSupport()) {
            client.getOutInterceptors().add(new ProvidedAttachmentOutInterceptor());
            client.getOutInterceptors().add(new FixContentTypeOutInterceptor());
        }

        if (features != null) {
            for (var feature : features) {
                client.getEndpoint().getActiveFeatures().add(feature);
                feature.initialize(client, client.getBus());
            }
        }

        InterceptorUtils.copyInterceptorsFromProvider(customInterceptors, client);

    }

    protected void configureProperties(Client client) {
        if (properties != null) {
            client.getEndpoint().putAll(properties);
        }
    }

    /**
     * Helper method for installing of payload-collecting SOAP interceptors
     * for the given Client.
     */
    static protected void installPayloadInterceptors(Client client) {
        client.getOutInterceptors().add(new OutStreamSubstituteInterceptor());
        client.getOutInterceptors().add(new OutPayloadExtractorInterceptor());
    }


    /**
     * Configures SOAP binding of the given SOAP port.
     */
    private void configureBinding(Object port) {
        var bindingProvider = (BindingProvider) port;

        var reqContext = bindingProvider.getRequestContext();
        reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

        var binding = bindingProvider.getBinding();
        var soapBinding = (SOAPBinding) binding;
        soapBinding.setMTOMEnabled(wsTransactionConfiguration.isMtom());
    }

    /**
     * Returns a client stub (previously gained via {@link #getClient()}) to the pool.
     * This method MUST be called as soon as the use of the port stub is finished.
     *
     * @param client client stub, <code>null</code> values are safe.
     */
    public void restoreClient(Object client) {
        if (client != null) {
            // Reset the response context associated with the current thread to allow the context to be GCed.
            // See https://issues.apache.org/jira/browse/CXF-7710 and https://issues.apache.org/jira/browse/CXF-7591
            ClientProxy.getClient(client).getResponseContext().clear();
            clientPool.restore(client);
            LOG.debug("Returned client stub {} to the pool", client);
        }
    }

    
    class PortFactory implements PoolObjectFactory<Object> {
        @Override
        public Object create() {
            var wsdlURL = getClass().getClassLoader().getResource(wsTransactionConfiguration.getWsdlLocation());
            var service = Service.create(wsdlURL, wsTransactionConfiguration.getServiceName());
            var port = service.getPort(wsTransactionConfiguration.getSei());
            var client = ClientProxy.getClient(port);
            configureBinding(port);
            configureInterceptors(client);
            configureProperties(client);
            if (securityInformation != null) {
                securityInformation.configureHttpConduit((HTTPConduit) client.getConduit());
            }
            if (httpClientPolicy != null) {
                ((HTTPConduit) client.getConduit()).setClient(httpClientPolicy);
            }
            LOG.debug("Created client stub {} for {}", port, wsTransactionConfiguration.getServiceName());
            return port;
        }

        @Override
        public boolean readyToTake(Object o) {
            return true;
        }

        @Override
        public boolean readyToRestore(Object o) {
            return true;
        }

        @Override
        public void destroy(Object o) {
            // nop
        }
    }

}
