/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept;

import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException;

/**
 * Trivial Hl7v2InterceptorFactory that calls Class.newInstance()
 */
public class Hl7v2InterceptorFactorySupport<T extends Hl7v2Interceptor> implements Hl7v2InterceptorFactory<T> {

    private Class<T> clazz;

    public Hl7v2InterceptorFactorySupport(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T getNewInstance() {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new Hl7v2AdaptingException("Could not create interceptor instance of class" + clazz.getName(), e);
        }
    }
}
