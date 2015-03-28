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

import static org.apache.commons.lang3.Validate.notNull;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.ws.addressing.MAPAggregator;
import org.apache.cxf.ws.addressing.soap.MAPCodec;
import org.openehealth.ipf.commons.ihe.ws.cxf.Cxf3791WorkaroundInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.FixContentTypeOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.MustUnderstandDecoratorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.ProvidedAttachmentOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutStreamSubstituteInterceptor;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;

/**
 * Factory for ITI Web Service stubs.
 * @author Jens Riemschneider
 */
public class JaxWsClientFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JaxWsClientFactory.class);

    protected final ThreadLocal<Object> threadLocalPort = new ThreadLocal<>();
    protected final WsTransactionConfiguration wsTransactionConfiguration;
    protected final String serviceUrl;
    protected final InterceptorProvider customInterceptors;
    protected final WsAuditStrategy auditStrategy;

    /**
     * Constructs the factory.
     * @param wsTransactionConfiguration
     *          the info about the Web Service.
     * @param serviceUrl
     *          the URL of the Web Service.
     * @param auditStrategy
     *          client-side ATNA audit strategy.
     * @param customInterceptors
     *          user-defined custom CXF interceptors.          
     */
    public JaxWsClientFactory(
            WsTransactionConfiguration wsTransactionConfiguration,
            String serviceUrl,
            WsAuditStrategy auditStrategy,
            InterceptorProvider customInterceptors) 
    {
        notNull(wsTransactionConfiguration, "wsTransactionConfiguration");
        this.wsTransactionConfiguration = wsTransactionConfiguration;
        this.serviceUrl = serviceUrl;
        this.auditStrategy = auditStrategy;
        this.customInterceptors = customInterceptors;
    }

    /**
     * Returns a client stub for the web-service.
     * <p>
     * This method reuses the last stub created by the current thread. 
     * @return the client stub.
     */
    public synchronized Object getClient() {
        if (threadLocalPort.get() == null) {
            URL wsdlURL = getClass().getClassLoader().getResource(wsTransactionConfiguration.getWsdlLocation());
            Service service = Service.create(wsdlURL, wsTransactionConfiguration.getServiceName());
            Object port = service.getPort(wsTransactionConfiguration.getSei());
            Client client = ClientProxy.getClient(port);
            configureBinding(port);
            configureInterceptors(client);

            threadLocalPort.set(port);
            LOG.debug("Created client adapter for: {}", wsTransactionConfiguration.getServiceName());
        }        
        return threadLocalPort.get();
    }

    
    /**
     * @return the service info of this factory.
     */
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return wsTransactionConfiguration;
    }


    /**
     * Configures SOAP interceptors for the given client. 
     */
    protected void configureInterceptors(Client client) {
        client.getInInterceptors().add(new Cxf3791WorkaroundInterceptor());

        // WS-Addressing-related interceptors
        if (wsTransactionConfiguration.isAddressing()) {
            MustUnderstandDecoratorInterceptor interceptor = new MustUnderstandDecoratorInterceptor();
            for (String nsUri : SoapUtils.WS_ADDRESSING_NS_URIS) {
                interceptor.addHeader(new QName(nsUri, "Action"));
            }

            client.getOutInterceptors().add(interceptor);

            MAPCodec mapCodec = new MAPCodec();
            MAPAggregator mapAggregator = new MAPAggregator();
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

        InterceptorUtils.copyInterceptorsFromProvider(customInterceptors, client);
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
        BindingProvider bindingProvider = (BindingProvider) port;

        Map<String, Object> reqContext = bindingProvider.getRequestContext();
        reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

        Binding binding = bindingProvider.getBinding();
        SOAPBinding soapBinding = (SOAPBinding) binding;
        soapBinding.setMTOMEnabled(wsTransactionConfiguration.isMtom());
    }
}
