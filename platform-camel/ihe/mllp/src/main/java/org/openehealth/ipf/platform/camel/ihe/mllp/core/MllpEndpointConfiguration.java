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

import lombok.Getter;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpointConfiguration;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerDispatchingInterceptor;

import java.util.Map;

/**
 * Configuration of an MLLP endpoint. Only IPF-specific parameters are maintained here, the rest is
 * maintained in {@link org.apache.camel.component.netty.NettyConfiguration}.
 *
 * @author Dmytro Rud
 */
public class MllpEndpointConfiguration extends AuditableEndpointConfiguration {

    @Getter
    private final boolean supportSegmentFragmentation;
    @Getter
    private final int segmentFragmentationThreshold;

    @Getter
    private final ConsumerDispatchingInterceptor dispatcher;

    protected MllpEndpointConfiguration(MllpComponent<?, ?> component, String uri, Map<String, Object> parameters) {
        super(component, parameters);

        supportSegmentFragmentation = component.getAndRemoveParameter(
                parameters, "supportSegmentFragmentation", boolean.class, false);
        segmentFragmentationThreshold = component.getAndRemoveParameter(
                parameters, "segmentFragmentationThreshold", int.class, -1);                // >= 5 characters

        dispatcher = component.resolveAndRemoveReferenceParameter(parameters, "dispatcher", ConsumerDispatchingInterceptor.class);

    }

}
