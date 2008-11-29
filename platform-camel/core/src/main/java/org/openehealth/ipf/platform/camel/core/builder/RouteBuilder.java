/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.core.builder;

import org.apache.camel.Processor;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.spring.SpringRouteBuilder;

import org.openehealth.ipf.commons.core.modules.api.Aggregator;
import org.openehealth.ipf.commons.core.modules.api.Converter;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openehealth.ipf.commons.core.modules.api.Predicate;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.platform.camel.core.adapter.AggregatorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ConverterAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.DataFormatAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ParserAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.PredicateAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.RendererAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.TransmogrifierAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;
import org.openehealth.ipf.platform.camel.core.bridge.InOnlyBridge;
import org.openehealth.ipf.platform.camel.core.extend.RouteModelExtender;
import org.openehealth.ipf.platform.camel.core.process.Enricher;
import org.openehealth.ipf.platform.camel.core.process.Validation;

/**
 * Route builder with support methods for common platform DSL extensions. 
 * 
 * @author Martin Krasser
 */
public class RouteBuilder extends SpringRouteBuilder {

    // ----------------------------------------------------------------
    //  Configuration
    // ----------------------------------------------------------------
    
    private RouteModelExtender routeModelExtender;
    
    private RouteBuilderConfig routeBuilderConfig;
    
    /**
     * Sets an optional route model extender instance used for activating 
     * extensions required by route builder configurations.
     * 
     * @param routeModelExtender route model extender instance.
     */
    public void setRouteModelExtender(RouteModelExtender routeModelExtender) {
        this.routeModelExtender = routeModelExtender;
    }
    
    /**
     * Sets an optional external route builder configuration object. If an
     * external configuration object is set it will be used to configure this
     * builder, otherwise, subclasses must override {@link #configure()} to
     * configure this builder.
     * 
     * @param routeBuilderConfig external route builder configuration.
     */
    public void setRouteBuilderConfig(RouteBuilderConfig routeBuilderConfig) {
        this.routeBuilderConfig = routeBuilderConfig;
    }
    
    /**
     * Configures this route builder using an external route builder
     * configuration if it was injected via
     * {@link #setRouteBuilderConfig(RouteBuilderConfig)}. If there is no
     * external configuration injected subclasses must override this method to
     * configure this builder. This method also activates extensions if a route
     * model extender has been injected via
     * {@link #setRouteModelExtender(RouteModelExtender)}.
     * 
     * @throws Exception
     *             if the builder configuration failed.
     */
    @Override
    public void configure() throws Exception {
        if (routeModelExtender != null) {
            routeModelExtender.activate();
        }
        if (routeBuilderConfig != null) {
            routeBuilderConfig.apply(this);
        } 
    }
    
    // ----------------------------------------------------------------
    //  Adapter
    // ----------------------------------------------------------------
    
    public PredicateAdapter predicate(Predicate predicate) {
        return new PredicateAdapter(predicate);
    }
    
    /**
     * Creates a new {@link PredicateAdapter} that adapts a {@link Predicate}
     * Spring bean identified by name <code>predicateBeanName</code>.
     * 
     * @param predicateBeanName
     *            name of the {@link Predicate} bean.
     * @return an adapted {@link Predicate} bean.
     */
    public PredicateAdapter predicate(String predicateBeanName) {
        return new PredicateAdapter(bean(Predicate.class, predicateBeanName));
    }
    
    /**
     * Creates a new {@link ConverterAdapter} that adapts a
     * {@link Converter} Spring bean identified by name
     * <code>converterBeanName</code>.
     * 
     * @param converterBeanName
     *            name of the {@link Converter} bean.
     * @return an adapted {@link Converter} bean.
     */
    public ConverterAdapter converter(String converterBeanName) {
        return new ConverterAdapter(bean(Converter.class, converterBeanName));
    }

    /**
     * Creates a new {@link ParserAdapter} that adapts a
     * {@link Parser} Spring bean identified by name
     * <code>parserBeanName</code>.
     * 
     * @param parserBeanName
     *            name of the {@link Parser} bean.
     * @return an adapted {@link Parser} bean.
     */
    public ParserAdapter parser(String parserBeanName) {
        return new ParserAdapter(bean(Parser.class, parserBeanName));
    }

    public ParserAdapter parser(Parser<?> parser) {
        return new ParserAdapter(parser);
    }

    /**
     * Creates a new {@link RendererAdapter} that adapts a
     * {@link Renderer} Spring bean identified by name
     * <code>rendererBeanName</code>.
     * 
     * @param rendererBeanName
     *            name of the {@link Renderer} bean.
     * @return an adapted {@link Renderer} bean.
     */
    public RendererAdapter renderer(String rendererBeanName) {
        return new RendererAdapter(bean(Renderer.class, rendererBeanName));
    }

    public RendererAdapter renderer(Renderer<?> renderer) {
        return new RendererAdapter(renderer);
    }

    /**
     * Creates a new {@link TransmogrifierAdapter} that adapts a
     * {@link Transmogrifier} Spring bean identified by name
     * <code>transmogrifierBeanName</code>.
     * 
     * @param transmogrifierBeanName
     *            name of the {@link Transmogrifier} bean.
     * @return an adapted {@link Transmogrifier} bean.
     */
    public TransmogrifierAdapter transmogrifier(String transmogrifierBeanName) {
        return new TransmogrifierAdapter(bean(Transmogrifier.class, transmogrifierBeanName));
    }
 
    /**
     * Creates a new {@link TransmogrifierAdapter} that adapts the given
     * <code>transmogrifier</code>.
     * 
     * @param transmogrifier
     *            a transmogrifier.
     * @return an adapted transmogrifier.
     */
    public TransmogrifierAdapter transmogrifier(Transmogrifier<?, ?> transmogrifier) {
        return new TransmogrifierAdapter(transmogrifier);
    }
 
    /**
     * Creates a new {@link ValidatorAdapter} that adapts a
     * {@link Validator} Spring bean identified by name
     * <code>validatorBeanName</code>.
     * 
     * @param validatorBeanName
     *            name of the {@link Validator} bean.
     * @return an adapted {@link Validator} bean.
     */
    public ValidatorAdapter validator(String validatorBeanName) {
        return new ValidatorAdapter(bean(Validator.class, validatorBeanName));
    }

    /**
     * Creates a new {@link ValidatorAdapter} that adapts the given
     * <code>validator</code>.
     * 
     * @param validator
     *            a validator.
     * @return an adapted validator.
     */
    public ValidatorAdapter validator(Validator<?, ?> validator) {
        return new ValidatorAdapter(validator);
    }

    public AggregatorAdapter aggregationStrategy(Aggregator aggregator) {
        return new AggregatorAdapter(aggregator);
    }
    
    /**
     * Creates a new {@link AggregatorAdapter} that adapts a
     * {@link Aggregator} Spring bean identified by name
     * <code>aggregatorBeanName</code>.
     * 
     * @param aggregatorBeanName
     *            name of the {@link Aggregator} bean.
     * @return an adapted {@link Aggregator} bean.
     */
    public AggregatorAdapter aggregationStrategy(String aggregatorBeanName) {
        return new AggregatorAdapter(bean(Aggregator.class, aggregatorBeanName));
    }
    
    public DataFormatAdapter dataFormatParser(Parser<?> parser) {
        return new DataFormatAdapter(parser);
    }
    
    public DataFormatAdapter dataFormatParser(String parserBeanName) {
        return new DataFormatAdapter(bean(Parser.class, parserBeanName));
    }
    
    public DataFormatAdapter dataFormatRenderer(Renderer<?> renderer) {
        return new DataFormatAdapter(renderer);
    }
    
    public DataFormatAdapter dataFormatRenderer(String rendererBeanName) {
        return new DataFormatAdapter(bean(Renderer.class, rendererBeanName));
    }
    
    // ----------------------------------------------------------------
    //  Processor
    // ----------------------------------------------------------------
    
    /**
     * Creates a new {@link Enricher}.
     * 
     * @param aggregationStrategy
     *            aggregation strategy to combine input data and additional
     *            data.
     * @param resourceUri
     *            URI of resource endpoint for obtaining additional data.
     * @return an enricher.
     */
    public Enricher enricher(AggregationStrategy aggregationStrategy, String resourceUri) {
        return new Enricher(aggregationStrategy, resourceUri);
    }
    
    /**
     * Creates a new {@link Enricher} using an {@link AggregatorAdapter} that
     * adapts an {@link Aggregator} Spring bean identified by name
     * <code>aggregatorBeanName</code>. The adapter is configured to obtain
     * the enrichment data from the resource's response-message's body (i.e.
     * from the out-message's body of the exchange used to communicate with the
     * resource).
     * 
     * @param aggregatorBeanName
     *            name of the {@link Aggregator} bean.
     * @param resourceUri
     *            URI of resource endpoint for obtaining additional data.
     * @return an enricher.
     */
    public Enricher enricher(String aggregatorBeanName, String resourceUri) {
        return new Enricher(aggregationStrategy(aggregatorBeanName).aggregationInput(outBody()), resourceUri);
    }

    /**
     * Creates a {@link Validation} process object.
     * 
     * @param validatorUri
     *            URI of the endpoint that validates an exchange.
     * @return a validation process object.
     */
    public Validation validation(String validatorUri) {
        return new Validation(validatorUri);
    }
    
    /**
     * Creates a {@link Validation} process object.
     * 
     * @param validator
     *            processor that validates an exchange.
     * @return a validation process object.
     */
    public Validation validation(Processor validator) {
        return new Validation(validator);
    }
    
    // ----------------------------------------------------------------
    //  Bridge
    // ----------------------------------------------------------------
    
    /**
     * Creates a {@link InOnlyBridge}.
     * 
     * @return a {@link InOnlyBridge}.
     */
    public DelegateProcessor inOnlyBridge() {
        return new InOnlyBridge();
    }

}
