/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.core;

import org.apache.camel.support.DefaultComponent;

import java.util.List;
import java.util.Map;

/**
 * Abstract base class for configuration for {@link InterceptableEndpoint} that use the Interceptor framework
 * defined in this module
 *
 * @since 3.1
 */
public abstract class InterceptableEndpointConfiguration {

    private final List<InterceptorFactory> customInterceptorFactories;

    protected InterceptableEndpointConfiguration(DefaultComponent component, Map<String, Object> parameters) {
        customInterceptorFactories = component.resolveAndRemoveReferenceListParameter(
                parameters, "interceptorFactories", InterceptorFactory.class);
    }

    /**
     * @return configured interceptor factories for the endpoint
     */
    public List<InterceptorFactory> getCustomInterceptorFactories() {
        return customInterceptorFactories;
    }
}
