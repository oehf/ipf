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

import static org.apache.commons.lang.Validate.notNull;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.ws.addressing.MAPAggregator;
import org.apache.cxf.ws.addressing.soap.MAPCodec;
import org.openehealth.ipf.commons.ihe.ws.cxf.FixContentTypeOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.MustUnderstandDecoratorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.ProvidedAttachmentOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutStreamSubstituteInterceptor;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;

/**
 * Factory for ITI web-service stubs.
 * @author Jens Riemschneider
 */
public class ItiClientFactory {
    private static final Log log = LogFactory.getLog(ItiClientFactory.class);

    protected final ThreadLocal<Object> threadLocalPort = new ThreadLocal<Object>();
    protected final ItiServiceInfo serviceInfo;
    protected final String serviceUrl;
    protected final InterceptorProvider customInterceptors;

    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the web-service.
     * @param serviceUrl
     *          the URL of the web-service.
     * @param customInterceptors
     *          user-defined custom CXF interceptors.          
     */
    public ItiClientFactory(
            ItiServiceInfo serviceInfo, 
            String serviceUrl, 
            InterceptorProvider customInterceptors) 
    {
        notNull(serviceInfo, "serviceInfo");
        this.serviceInfo = serviceInfo;
        this.serviceUrl = serviceUrl;
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
            URL wsdlURL = getClass().getClassLoader().getResource(serviceInfo.getWsdlLocation());
            Service service = Service.create(wsdlURL, serviceInfo.getServiceName());
            Object port = service.getPort(serviceInfo.getServiceClass());
            Client client = ClientProxy.getClient(port);
            configureBinding(port);
            configureInterceptors(client);

            threadLocalPort.set(port);
            log.debug("Created client adapter for: " + serviceInfo.getServiceName());
        }        
        return threadLocalPort.get();
    }

    
    /**
     * @return the service info of this factory.
     */
    public ItiServiceInfo getServiceInfo() {
        return serviceInfo;
    }


    /**
     * Configures SOAP interceptors for the given client. 
     */
    protected void configureInterceptors(Client client) {
        // WS-Addressing-related interceptors
        if (serviceInfo.isAddressing()) {
            MustUnderstandDecoratorInterceptor interceptor = new MustUnderstandDecoratorInterceptor();
            for (String nsUri : SoapUtils.WS_ADDRESSING_NS_URIS) {
                interceptor.addHeader(new QName(nsUri, "Action"));
                interceptor.addHeader(new QName(nsUri, "ReplyTo"));
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
        
        if (serviceInfo.isSwaOutSupport()) {
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
        soapBinding.setMTOMEnabled(serviceInfo.isMtom());
    }
}
