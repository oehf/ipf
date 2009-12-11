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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.openehealth.ipf.commons.ihe.ws.cxf.WsSecurityUnderstandingInInterceptor;

import java.util.Collections;

/**
 * Factory for ITI web-services.
 * @author Jens Riemschneider
 */
public class ItiServiceFactory {
    private static final Log log = LogFactory.getLog(ItiServiceFactory.class);

    /** The service info. */
    protected final ItiServiceInfo serviceInfo;
    /** The service address. */
    protected final String serviceAddress;

    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the service to produce.
     * @param serviceAddress
     *          the address of the service that it should be published with.
     */
    public ItiServiceFactory(ItiServiceInfo serviceInfo, String serviceAddress) {
        this.serviceInfo = serviceInfo;
        this.serviceAddress = serviceAddress;
    }
    
    /**
     * Creates, configures and publishes a service.
     * @param <T>
     *          the type of the service implementation.
     * @param serviceImplClass
     *          the type of the service implementation.
     * @return the service implementation.
     * @deprecated Use {@link #createServerFactory(Class)} instead, because
     *          the service created by this method cannot be stopped or
     *          restarted.
     */
    @Deprecated
    public <T> T createService(Class<T> serviceImplClass) {
        ServerFactoryBean svrFactory = createServerFactory(serviceImplClass);
        svrFactory.setStart(true);
        svrFactory.create();
        log.debug("Published webservice endpoint for: " + serviceInfo.getServiceName());
        return serviceImplClass.cast(svrFactory.getServiceBean());
    }

    /**
     * Creates and configures a server factory.
     * Use the server factory to create a server instance that can be used
     * to start and stop the service.
     * @param serviceImplClass
     *          the type of the service implementation.
     * @return the server factory.
     */
    public ServerFactoryBean createServerFactory(Class<?> serviceImplClass) {
        try {
            ServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
            configureService(svrFactory, serviceImplClass.newInstance());
            configureBinding(svrFactory);
            configureInterceptors(svrFactory);
            svrFactory.setStart(false);
            return svrFactory;
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private void configureService(ServerFactoryBean svrFactory, Object service) {
        svrFactory.setServiceClass(serviceInfo.getServiceClass());
        svrFactory.setServiceName(serviceInfo.getServiceName());
        svrFactory.setWsdlLocation(serviceInfo.getWsdlLocation());
        svrFactory.setAddress(serviceAddress);
        svrFactory.setServiceBean(service);
        svrFactory.getFeatures().add(new WSAddressingFeature());
        if (serviceInfo.isMtom()) {
            svrFactory.setProperties(Collections.<String, Object>singletonMap("mtom-enabled", "true"));
        }
    }

    private void configureBinding(ServerFactoryBean svrFactory) {
        SoapBindingConfiguration bindingConfig = new SoapBindingConfiguration();
        bindingConfig.setBindingName(serviceInfo.getBindingName());
        svrFactory.setBindingConfig(bindingConfig);
    }

    /**
     * Called to configure any interceptors of the service.
     * @param svrFactory
     *          the server factory.
     */
    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        svrFactory.getInInterceptors().add(new WsSecurityUnderstandingInInterceptor());
    }
}
