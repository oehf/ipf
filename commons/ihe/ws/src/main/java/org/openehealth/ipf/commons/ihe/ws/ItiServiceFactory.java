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

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.openehealth.ipf.commons.ihe.ws.cxf.WsSecurityUnderstandingInInterceptor;

/**
 * Factory for ITI web-services.
 * @author Jens Riemschneider
 */
public class ItiServiceFactory {
    private static final Log log = LogFactory.getLog(ItiServiceFactory.class);
    
    protected final ItiServiceInfo serviceInfo;
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
     */
    public <T> T createService(Class<T> serviceImplClass) {
        try {
            T service = serviceImplClass.newInstance();
            
            ServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
            configureService(svrFactory, service);
            configureBinding(svrFactory);
            configureInterceptors(svrFactory);
            svrFactory.create();
            
            log.debug("Published webservice endpoint for: " + serviceInfo.getServiceName());
            return service;
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

    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        svrFactory.getInInterceptors().add(new WsSecurityUnderstandingInInterceptor());
    }
}
