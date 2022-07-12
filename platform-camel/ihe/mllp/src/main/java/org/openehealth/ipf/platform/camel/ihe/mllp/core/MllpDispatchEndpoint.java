/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import org.apache.camel.component.netty.NettyEndpoint;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerDispatchingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerStringProcessingInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * MLLP dispatching Camel endpoint.
 *
 * @author Dmytro Rud
 */
public class MllpDispatchEndpoint extends MllpEndpoint<MllpDispatchEndpointConfiguration, MllpAuditDataset, MllpDispatchComponent> {

    public MllpDispatchEndpoint(
            MllpDispatchComponent mllpComponent,
            NettyEndpoint wrappedEndpoint,
            MllpDispatchEndpointConfiguration config) {
        super(mllpComponent, wrappedEndpoint, config);
    }

    @Override
    public List<Interceptor> createInitialConsumerInterceptorChain() {
        var initialChain = new ArrayList<Interceptor>();
        initialChain.add(new ConsumerStringProcessingInterceptor());

        var dispatcher = getConfig().getDispatcher();
        if (dispatcher != null) {
            dispatcher.addTransactionRoutes(getConfig().getRoutes());
        } else {
            dispatcher = new ConsumerDispatchingInterceptor(getCamelContext(), getConfig().getRoutes());
        }
        initialChain.add(dispatcher);
        return initialChain;
    }


    @Override
    public List<Interceptor> createInitialProducerInterceptorChain() {
        throw new IllegalStateException("No producer support for MLLP dispatch endpoints.");
    }

}
