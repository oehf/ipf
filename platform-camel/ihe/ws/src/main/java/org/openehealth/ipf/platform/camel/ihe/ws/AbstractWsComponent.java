/*
 * Copyright 2010 the original author or authors.
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

import java.util.Map;

import org.apache.camel.impl.DefaultComponent;
import org.apache.cxf.interceptor.AbstractBasicInterceptorProvider;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;

/**
 * Base component class for Web Service-based IHE components.
 * @author Dmytro Rud
 */
abstract public class AbstractWsComponent<C extends ItiServiceInfo> extends DefaultComponent {

    protected InterceptorProvider getCustomInterceptors(Map<String, Object> parameters) {
        AbstractBasicInterceptorProvider provider = new AbstractBasicInterceptorProvider() {};
        
        provider.setInInterceptors(resolveAndRemoveReferenceListParameter(
                parameters, "inInterceptors", Interceptor.class));
        provider.setInFaultInterceptors(resolveAndRemoveReferenceListParameter(
                parameters, "inFaultInterceptors", Interceptor.class));
        provider.setOutInterceptors(resolveAndRemoveReferenceListParameter(
                parameters, "outInterceptors", Interceptor.class));
        provider.setOutFaultInterceptors(resolveAndRemoveReferenceListParameter(
                parameters, "outFaultInterceptors", Interceptor.class));
        
        return provider;
    }

    public abstract C getWebServiceConfiguration();
}
