/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadExtractorInterceptor;

/**
 * Service factory for HL7v2 WS endpoints.
 * @author Dmytro Rud
 */
public class SimpleHl7v2WsServiceFactory extends ItiServiceFactory {
    private final Hl7v2WsFailureHandler faultHandler;

    public SimpleHl7v2WsServiceFactory(
            ItiServiceInfo serviceInfo,
            String serviceAddress,
            InterceptorProvider customInterceptors,
            Hl7v2WsFailureHandler faultHandler)
    {
        super(serviceInfo, serviceAddress, customInterceptors);
        this.faultHandler = faultHandler;
    }

    @Override
    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        super.configureInterceptors(svrFactory);

        if (faultHandler != null) {
            svrFactory.getInInterceptors().add(new InPayloadExtractorInterceptor(false));

            Hl7v2WsFaultHandlerInterceptor faultHandlerInterceptor =
                    new Hl7v2WsFaultHandlerInterceptor(faultHandler);

            svrFactory.getOutInterceptors().add(faultHandlerInterceptor);
            svrFactory.getOutFaultInterceptors().add(faultHandlerInterceptor);
        }
    }
}
