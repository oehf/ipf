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
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.openehealth.ipf.commons.ihe.core.chain.ChainUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for endpoints that use the Interceptor framework defined in this module
 *
 * @since 3.1
 */
public abstract class InterceptableEndpoint<
        ConfigType extends InterceptableEndpointConfiguration,
        ComponentType extends InterceptableComponent> extends DefaultEndpoint {

    public InterceptableEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    @Override
    public final Producer createProducer() throws Exception {
        Producer producer = doCreateProducer();
        return InterceptorUtils.adaptProducerChain(
                getProducerInterceptorChain(),
                this,
                producer);
    }

    @Override
    public final Consumer createConsumer(Processor originalProcessor) throws Exception {

        // Configure interceptor chain
        List<Interceptor> chain = getConsumerInterceptorChain();
        Processor processor = originalProcessor;
        for (int i = chain.size() - 1; i >= 0; --i) {
            Interceptor interceptor = chain.get(i);
            interceptor.setWrappedProcessor(processor);
            interceptor.setEndpoint(this);
            processor = interceptor;
        }
        // Create the component-specific consumer
        return doCreateConsumer(processor);
    }

    private List<Interceptor> getProducerInterceptorChain() {
        List<Interceptor> initialChain = createInitialProducerInterceptorChain();
        List<Interceptor> additionalInterceptors = new ArrayList<>();
        additionalInterceptors.addAll(getInterceptableComponent().getAdditionalProducerInterceptors());
        // add interceptors provided by the user
        additionalInterceptors.addAll(getCustomInterceptors());

        return ChainUtils.createChain(initialChain, additionalInterceptors);
    }

    private List<Interceptor> getConsumerInterceptorChain() {
        // set up initial interceptor chain
        List<Interceptor> initialChain = createInitialConsumerInterceptorChain();

        List<Interceptor> additionalInterceptors = new ArrayList<>();
        additionalInterceptors.addAll(getInterceptableComponent().getAdditionalConsumerInterceptors());
        // add interceptors provided by the user
        additionalInterceptors.addAll(getCustomInterceptors());
        return ChainUtils.createChain(initialChain, additionalInterceptors);
    }

    /**
     * @return a list of endpoint-specific interceptors
     */
    private synchronized List<Interceptor> getCustomInterceptors() {
        List<Interceptor> result = new ArrayList<>();
        List<InterceptorFactory> factories = getInterceptableConfiguration().getCustomInterceptorFactories();
        for (InterceptorFactory customInterceptorFactory : factories) {
            result.add(customInterceptorFactory.getNewInstance());
        }
        return result;
    }

    /**
     * @return the actual producer without any interceptors configured
     * @throws Exception
     */
    protected abstract Producer doCreateProducer() throws Exception;

    /**
     * @return the actual consumer without any interceptors configured
     * @throws Exception
     */
    protected abstract Consumer doCreateConsumer(Processor processor) throws Exception;

    /**
     * @return the component for this endpoint
     */
    protected abstract ComponentType getInterceptableComponent();

    /**
     * @return the configuration for this endpoint
     */
    protected abstract ConfigType getInterceptableConfiguration();

    /**
     * @return the initial chain of consumer interceptors for this endpoint
     */
    protected abstract List<Interceptor> createInitialConsumerInterceptorChain();

    /**
     * @return the initial chain of producer interceptors for this endpoint
     */
    protected abstract List<Interceptor> createInitialProducerInterceptorChain();
}
