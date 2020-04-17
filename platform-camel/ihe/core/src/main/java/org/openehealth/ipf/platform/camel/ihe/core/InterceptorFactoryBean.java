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
import org.springframework.beans.factory.FactoryBean;

/**
 * Optional Bridging from {@link InterceptorFactory} to a Spring {@link FactoryBean}.
 *
 * @since 3.1
 */
public class InterceptorFactoryBean<E extends Endpoint, T extends Interceptor<E>>
        extends InterceptorFactorySupport<E, T> implements FactoryBean<T> {

    public InterceptorFactoryBean(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public T getObject() throws Exception {
        return getNewInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
