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

import static org.apache.commons.lang.Validate.notNull;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.addressing.MAPAggregator;
import org.apache.cxf.ws.addressing.soap.MAPCodec;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.MustUnderstandDecoratorInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditDatasetEnrichmentInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditFinalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ClientOutputStreamSubstituteInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ClientPayloadExtractorInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.SoapUtils;

/**
 * Camel producer used to make calls to a webservice.
 * 
 * @param <T>
 *            the type of the webservice.
 * 
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public abstract class DefaultItiProducer<T> extends DefaultProducer<Exchange> implements Auditable {
    private static final Log log = LogFactory.getLog(DefaultItiProducer.class);

    private final ThreadLocal<T> threadLocalPort = new ThreadLocal<T>();
    private final ItiServiceInfo<T> serviceInfo;
    private final DefaultItiEndpoint endpoint;

    /**
     * Constructs the producer.
     * 
     * @param endpoint
     *            the endpoint that creates this producer.
     * @param serviceInfo
     *            the info describing the web-service.
     */
    public DefaultItiProducer(DefaultItiEndpoint endpoint, ItiServiceInfo<T> serviceInfo) {
        super(endpoint);
        notNull(serviceInfo, "serviceInfo");
        this.serviceInfo = serviceInfo;
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        log.debug("Calling webservice on '" + serviceInfo.getServiceName() + "' with " + exchange);
        callService(exchange);
    }

    /**
     * Sends the exchange to the webservice.
     * 
     * @param exchange
     *            the exchange that is send and changed with the result of the call.
     */
    protected abstract void callService(Exchange exchange);

    /**
     * Retrieves the client stub for the webservice.
     * <p>
     * This method caches the client stub instance and therefore requires thread
     * synchronization.
     * 
     * @return the client stub.
     */
    protected synchronized T getClient() {
        if (threadLocalPort.get() == null) {
            URL wsdlURL = getClass().getClassLoader().getResource(serviceInfo.getWsdlLocation());
            Service service = Service.create(wsdlURL, serviceInfo.getServiceName());
            T port = service.getPort(serviceInfo.getServiceClass());
            configureBinding(port);
            configureInterceptors(port);

            threadLocalPort.set(port);
            log.debug("Created client adapter for: " + serviceInfo.getServiceName());
        }        
        return threadLocalPort.get();
    }

    private void configureInterceptors(T port) {
        Client client = ClientProxy.getClient(port);
        
        // WS-Addressing-related interceptors
        if(serviceInfo.isAddressing()) {
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
        
        // install auditing-related interceptors if the user has not switched
        // auditing off
        if (endpoint.isAudit()) {
            AuditStrategy auditStrategy = createAuditStrategy(endpoint.isAllowIncompleteAudit());
            client.getOutInterceptors().add(new AuditDatasetEnrichmentInterceptor(auditStrategy, false));
            client.getOutInterceptors().add(new ClientOutputStreamSubstituteInterceptor(auditStrategy));
            client.getOutInterceptors().add(new ClientPayloadExtractorInterceptor(auditStrategy));
        
            AuditFinalInterceptor finalInterceptor = new AuditFinalInterceptor(auditStrategy, false);
            client.getInInterceptors().add(finalInterceptor);
            client.getInFaultInterceptors().add(finalInterceptor);
        }
    }

    private void configureBinding(T port) {
        BindingProvider bindingProvider = (BindingProvider) port;

        Map<String, Object> reqContext = bindingProvider.getRequestContext();
        reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint.getServiceUrl());

        Binding binding = bindingProvider.getBinding();
        SOAPBinding soapBinding = (SOAPBinding) binding;
        soapBinding.setMTOMEnabled(serviceInfo.isMtom());
    }
}
