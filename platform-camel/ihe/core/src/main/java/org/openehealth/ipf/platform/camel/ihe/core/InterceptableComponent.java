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

import org.apache.camel.Component;

import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for components that use the Interceptor framework defined in this module
 *
 * @since 3.1
 */
public interface InterceptableComponent extends Component {

    /**
     * Returns a list of component-specific (i.e. transaction-specific)
     * interceptors which will be integrated into the interceptor
     * chain of each consumer instance created by this component.
     * <p/>
     * Per default returns an empty list.
     * <br>
     * When overwriting this method, please note:
     * <ul>
     * <li>Neither the returned list nor any element of it
     * are allowed to be <code>null</code>.
     * <li>Each invocation should return freshly created instances
     * of interceptors (like prototype-scope beans in Spring),
     * because interceptors cannot be reused by multiple endpoints.
     * </ul>
     *
     * @return a list of component-specific (i.e. transaction-specific) FHIR interceptors
     */
    default List<Interceptor> getAdditionalConsumerInterceptors() {
        return Collections.emptyList();
    }

    /**
     * Returns a list of component-specific (i.e. transaction-specific)
     * FHIR interceptors which will be integrated into the interceptor
     * chain of each producer instance created by this component.
     * <p/>
     * Per default returns an empty list.
     * <br>
     * When overwriting this method, please note:
     * <ul>
     * <li>Neither the returned list nor any element of it
     * are allowed to be <code>null</code>.
     * <li>Each invocation should return freshly created instances
     * of interceptors (like prototype-scope beans in Spring),
     * because interceptors cannot be reused by multiple endpoints.
     * </ul>
     *
     * @return a list of component-specific (i.e. transaction-specific) FHIR interceptors
     */
    default List<Interceptor> getAdditionalProducerInterceptors() {
        return Collections.emptyList();
    }
}
