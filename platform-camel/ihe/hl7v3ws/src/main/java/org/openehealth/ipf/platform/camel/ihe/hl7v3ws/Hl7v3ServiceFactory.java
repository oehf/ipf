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
package org.openehealth.ipf.platform.camel.ihe.hl7v3ws;

import org.apache.cxf.frontend.ServerFactoryBean;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.cxf.databinding.plainxml.PlainXmlDataBinding;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.ServerParameterInjectorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.ServerPayloadExtractorInterceptor;

/**
 * Factory for HL7 v3 Web Services.
 * @author Dmytro Rud
 */
public class Hl7v3ServiceFactory extends ItiServiceFactory {
    
    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the service to produce.
     * @param serviceAddress
     *          the address of the service that it should be published with.
     */
    public Hl7v3ServiceFactory(ItiServiceInfo serviceInfo, String serviceAddress) {
        super(serviceInfo, serviceAddress);
    }
    
    @Override
    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        super.configureInterceptors(svrFactory);
        svrFactory.getInInterceptors().add(new ServerPayloadExtractorInterceptor());
        svrFactory.getInInterceptors().add(new ServerParameterInjectorInterceptor(0));
        svrFactory.setDataBinding(new PlainXmlDataBinding());
    }
}
