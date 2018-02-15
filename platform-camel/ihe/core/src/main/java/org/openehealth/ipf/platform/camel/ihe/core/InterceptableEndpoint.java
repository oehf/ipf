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

import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.openehealth.ipf.commons.ihe.core.chain.ChainUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Interface for endpoints that use the Interceptor framework defined in this module.
 *
 * This has been changed to an interface with default methods as of IPF 3.2 so that it
 * can also be used when extending other endpoint types than DefaultEndpoint.
 *
 * @since 3.1
 */
public interface InterceptableEndpoint<
        ConfigType extends InterceptableEndpointConfiguration,
        ComponentType extends InterceptableComponent> extends Endpoint {


    /**
     * Default implementation that creates a producer by calling {@link #doCreateProducer()}
     * and weaves in configured interceptor processors to be executed before reaching out
     * for the target
     *
     * @return intercepted producer
     * @throws Exception
     */
    @Override
    default Producer createProducer() throws Exception {
        Producer producer = doCreateProducer();

        List<Interceptor> initialChain = createInitialProducerInterceptorChain();
        List<Interceptor> additionalInterceptors = new ArrayList<>();
        additionalInterceptors.addAll(getInterceptableComponent().getAdditionalProducerInterceptors());
        // add interceptors provided by the user
        additionalInterceptors.addAll(getCustomInterceptors());
        List<Interceptor> producerInterceptorChain = ChainUtils.createChain(initialChain, additionalInterceptors);

        return InterceptorUtils.adaptProducerChain(
                producerInterceptorChain,
                this,
                producer);
    }

    /**
     * Default implementation that creates a consumer by calling {@link #doCreateConsumer(Processor)}
     * and weaves in configured interceptor processors to be executed before calling the first
     * processor in the consumer route.
     *
     * @param originalProcessor processor for handling the consumed request
     * @return intercepted consumer
     * @throws Exception
     */
    @Override
    default Consumer createConsumer(Processor originalProcessor) throws Exception {
        // Configure interceptor chain
        List<Interceptor> initialChain = createInitialConsumerInterceptorChain();

        List<Interceptor> additionalInterceptors = new ArrayList<>();
        additionalInterceptors.addAll(getInterceptableComponent().getAdditionalConsumerInterceptors());
        // add interceptors provided by the user
        additionalInterceptors.addAll(getCustomInterceptors());
        List<Interceptor> consumerInterceptorChain = ChainUtils.createChain(initialChain, additionalInterceptors);

        Processor processor = originalProcessor;
        for (int i = consumerInterceptorChain.size() - 1; i >= 0; --i) {
            Interceptor interceptor = consumerInterceptorChain.get(i);
            interceptor.setWrappedProcessor(processor);
            interceptor.setEndpoint(this);
            processor = interceptor;
        }
        // Create the component-specific consumer
        return doCreateConsumer(processor);
    }

    /**
     * Returns a list of endpoint-specific custom interceptors from {@link #getInterceptableConfiguration()}
     *
     * @return a list of endpoint-specific custom interceptors
     */
    default List<Interceptor> getCustomInterceptors() {
        List<Interceptor> result = new ArrayList<>();
        List<InterceptorFactory> factories = getInterceptableConfiguration().getCustomInterceptorFactories();
        factories.stream()
                .map((Function<InterceptorFactory, Interceptor>) InterceptorFactory::getNewInstance)
                .forEach(result::add);
        return result;
    }

    /**
     * @return the actual producer without any interceptors configured
     * @throws Exception
     */
    Producer doCreateProducer() throws Exception;

    /**
     * @return the actual consumer without any interceptors configured
     * @throws Exception
     */
    Consumer doCreateConsumer(Processor processor) throws Exception;

    /**
     * @return the component for this endpoint
     */
    ComponentType getInterceptableComponent();

    /**
     * @return the configuration for this endpoint
     */
    ConfigType getInterceptableConfiguration();

    /**
     * @return the initial chain of consumer interceptors for this endpoint
     */
    List<Interceptor> createInitialConsumerInterceptorChain();

    /**
     * @return the initial chain of producer interceptors for this endpoint
     */
    List<Interceptor> createInitialProducerInterceptorChain();
}
