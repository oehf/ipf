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
package org.openehealth.ipf.platform.camel.ihe.ws.mbean;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;

import java.util.List;

public class SomeItiEndpoint extends AbstractWsEndpoint<SomeItiComponent> {
    
    @SuppressWarnings("unchecked")
    public SomeItiEndpoint(
            String endpointUri,
            String address,
            SomeItiComponent someItiComponent,
            InterceptorProvider interceptorProvider,
            List<AbstractFeature> features)
    {
        super(endpointUri, address, someItiComponent, interceptorProvider, features);
    }

    @Override
    public JaxWsClientFactory getJaxWsClientFactory() {
        return null;   // dummy
    }

    @Override
    public JaxWsServiceFactory getJaxWsServiceFactory() {
        return null;   // dummy
    }

    @Override
    public Producer createProducer() throws Exception {
        return null;   // dummy
    }
    
    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return null;   // dummy
    }

}
