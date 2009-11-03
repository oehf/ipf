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
import org.apache.cxf.ws.addressing.MAPAggregator;
import org.apache.cxf.ws.addressing.soap.MAPCodec;
import org.openehealth.ipf.commons.ihe.ws.cxf.FixContentTypeOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.MustUnderstandDecoratorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.ProvidedAttachmentOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.ClientOutputStreamSubstituteInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.ClientPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;

/**
 * Factory for ITI web-service stubs.
 * @author Jens Riemschneider
 */
public class ItiClientFactory {
    private static final Log log = LogFactory.getLog(ItiClientFactory.class);

    protected final ThreadLocal<Object> threadLocalPort = new ThreadLocal<Object>();
    protected final ItiServiceInfo serviceInfo;
    protected final boolean soap11;
    protected final String serviceUrl;

    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the web-service.
     * @param soap11
     *          whether SOAP 1.1 should be used instead of SOAP 1.2.
     * @param serviceUrl
     *          the URL of the web-service.
     */
    public ItiClientFactory(ItiServiceInfo serviceInfo, boolean soap11, String serviceUrl) {
        notNull(serviceInfo, "serviceInfo");
        this.serviceInfo = serviceInfo;
        this.soap11 = soap11;
        this.serviceUrl = serviceUrl;
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
            
            QName portName = 
                ((serviceInfo.getPortName12() == null) || soap11) ?
                    serviceInfo.getPortName11() : 
                    serviceInfo.getPortName12();

            Object port = service.getPort(portName, serviceInfo.getServiceClass());
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
    }

    
    /**
     * Helper method for installing of payload-collecting SOAP interceptors
     * for the given Client.
     */
    static protected void installPayloadInterceptors(Client client) {
        client.getOutInterceptors().add(new ClientOutputStreamSubstituteInterceptor());
        client.getOutInterceptors().add(new ClientPayloadExtractorInterceptor());
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
