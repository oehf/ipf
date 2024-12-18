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
     */
    @Override
    default Producer createProducer() throws Exception {
        var producer = doCreateProducer();

        var initialChain = createInitialProducerInterceptorChain();
        var additionalInterceptors = new ArrayList<Interceptor>();
        additionalInterceptors.addAll(getInterceptableComponent().getAdditionalProducerInterceptors());
        // add interceptors provided by the user
        additionalInterceptors.addAll(getCustomInterceptors());
        var producerInterceptorChain = ChainUtils.createChain(initialChain, additionalInterceptors);

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
        var initialChain = createInitialConsumerInterceptorChain();

        var additionalInterceptors = new ArrayList<Interceptor>();
        additionalInterceptors.addAll(getInterceptableComponent().getAdditionalConsumerInterceptors());
        // add interceptors provided by the user
        additionalInterceptors.addAll(getCustomInterceptors());
        var consumerInterceptorChain = ChainUtils.createChain(initialChain, additionalInterceptors);

        var processor = originalProcessor;
        for (var i = consumerInterceptorChain.size() - 1; i >= 0; --i) {
            var interceptor = consumerInterceptorChain.get(i);
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
        var result = new ArrayList<Interceptor>();
        var factories = getInterceptableConfiguration().getCustomInterceptorFactories();
        factories.stream()
                .map(InterceptorFactory::getNewInstance)
                .forEach(result::add);
        return result;
    }

    /**
     * @return the actual producer without any interceptors configured
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
