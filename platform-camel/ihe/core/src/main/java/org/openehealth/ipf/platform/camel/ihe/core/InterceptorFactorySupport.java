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

import static java.util.Objects.requireNonNull;

/**
 * Trivial InterceptorFactory that calls Class.newInstance()
 *
 * @since 3.1
 */
public class InterceptorFactorySupport<E extends Endpoint, T extends Interceptor<E>> implements InterceptorFactory<E, T> {

    protected final Class<T> clazz;

    public InterceptorFactorySupport(Class<T> clazz) {
        this.clazz = requireNonNull(clazz);
    }

    @Override
    public T getNewInstance() {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not create interceptor instance of class " + clazz.getName(), e);
        }
    }
}
