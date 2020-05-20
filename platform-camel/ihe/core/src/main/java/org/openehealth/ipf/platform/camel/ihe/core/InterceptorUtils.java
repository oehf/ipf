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
import org.apache.camel.Processor;
import org.apache.camel.Producer;

import java.util.List;

/**
 * @author Dmytro Rud
 */
public class InterceptorUtils {

    public static Producer adaptProducerChain(
            List<Interceptor> chain,
            Endpoint endpoint,
            Producer originalProducer) {
        Processor processor = originalProducer;
        for (var interceptor : chain) {
            interceptor.setEndpoint(endpoint);
            interceptor.setWrappedProcessor(processor);
            processor = interceptor;
        }

        return new Interceptor2ProducerAdapter(processor, originalProducer);
    }
}
