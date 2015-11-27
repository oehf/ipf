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

import org.apache.camel.Endpoint;

import java.io.Serializable;

/**
 * Factory creating an {@link Interceptor}
 *
 * @since 3.1
 */
public interface InterceptorFactory<E extends Endpoint, T extends Interceptor<E>> extends Serializable {

    /**
     * Returns a new instance of an Interceptor
     *
     * @return new instance of an Interceptor
     */
    T getNewInstance();
}
