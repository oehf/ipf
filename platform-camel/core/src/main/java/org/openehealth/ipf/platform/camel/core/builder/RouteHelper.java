/*
 * Copyright 2009 the original author or authors.
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

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.builder.Builder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.CamelContextHelper;
import org.openehealth.ipf.commons.core.modules.api.*;
import org.openehealth.ipf.commons.xml.SchematronValidator;
import org.openehealth.ipf.commons.xml.XsdValidator;
import org.openehealth.ipf.platform.camel.core.adapter.*;
import org.openehealth.ipf.platform.camel.core.process.Enricher;
import org.openehealth.ipf.platform.camel.core.process.Validation;

import javax.xml.transform.stream.StreamSource;

/**
 * Helper class for creating IPF extensions in Java-based route definitions.
 * 
 * @author Martin Krasser
 */
public class RouteHelper {
    
    private RouteBuilder routeBuilder;

    public RouteHelper(RouteBuilder routeBuilder) {
        this.routeBuilder = routeBuilder;
    }
    
    /**
     * @param routeBuilder the routeBuilder to set
     */
    public void setRouteBuilder(RouteBuilder routeBuilder) {
        this.routeBuilder = routeBuilder;
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
        return new PredicateAdapter(routeBuilder.getContext().getRegistry()
                .lookupByNameAndType(predicateBeanName, Predicate.class));
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
        return new ConverterAdapter(routeBuilder.getContext().getRegistry()
                .lookupByNameAndType(converterBeanName, Converter.class));
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
        return new ParserAdapter(routeBuilder.getContext().getRegistry()
                .lookupByNameAndType(parserBeanName, Parser.class));
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
        return new RendererAdapter(routeBuilder.getContext().getRegistry()
                .lookupByNameAndType(rendererBeanName, Renderer.class));
    }

    /**
     * Creates a new {@link RendererAdapter} that adapts a {@link Renderer}.
     * 
     * @param renderer
     *            a {@link Renderer}.
     * @return an adapted {@link Renderer}.
     */
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
        return new TransmogrifierAdapter(routeBuilder.getContext().getRegistry()
                .lookupByNameAndType(transmogrifierBeanName, Transmogrifier.class));
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
        return new ValidatorAdapter(routeBuilder.getContext().getRegistry()
                .lookupByNameAndType(validatorBeanName, Validator.class));
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
    
    /**
     * Creates a new {@link ValidatorAdapter} that adapts the given
     * <code>XsdValidator</code>.
     * 
     * @return an adapted validator.
     */
    public ValidatorAdapter xsdValidator() {
        ValidatorAdapter adapter = new ValidatorAdapter(new XsdValidator());
        adapter.input(Builder.bodyAs(StreamSource.class));
        return adapter;
    }
    
    /**
     * Creates a new {@link ValidatorAdapter} that adapts the given
     * <code>SchematronValidator</code>.
     * 
     * @return an adapted validator.
     */
    public ValidatorAdapter schematronValidator() {
        ValidatorAdapter adapter = new ValidatorAdapter(new SchematronValidator());
        adapter.input(Builder.bodyAs(StreamSource.class));
        return adapter;
    }    

    /**
     * Creates a new {@link AggregatorAdapter} that adapts the given
     * <code>aggregator</code>.
     * 
     * @param aggregator
     *            an aggregator.
     * @return an adapted aggregator.
     */
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
        return new AggregatorAdapter(routeBuilder.getContext().getRegistry()
                .lookupByNameAndType(aggregatorBeanName, Aggregator.class));
    }
    
    public DataFormatAdapter dataFormatParser(Parser<?> parser) {
        return new DataFormatAdapter(parser);
    }
    
    public DataFormatAdapter dataFormatParser(String parserBeanName) {
        return new DataFormatAdapter(routeBuilder.getContext().getRegistry()
                .lookupByNameAndType(parserBeanName, Parser.class));
    }
    
    public DataFormatAdapter dataFormatRenderer(Renderer<?> renderer) {
        return new DataFormatAdapter(renderer);
    }
    
    public DataFormatAdapter dataFormatRenderer(String rendererBeanName) {
        return new DataFormatAdapter(routeBuilder.getContext().getRegistry()
                .lookupByNameAndType(rendererBeanName, Renderer.class));
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
     * @throws Exception
     *             if a producer cannot be created for the endpoint represented
     *             by <code>resourceUri</code>.
     */
    public Enricher enricher(AggregationStrategy aggregationStrategy, String resourceUri) throws Exception {
        Endpoint endpoint = CamelContextHelper.getMandatoryEndpoint(routeBuilder.getContext(), resourceUri);
        return new Enricher(aggregationStrategy, endpoint.createProducer());
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
     * @throws Exception
     *             if a producer cannot be created for the endpoint represented
     *             by <code>resourceUri</code>.
     */
    public Enricher enricher(String aggregatorBeanName, String resourceUri) throws Exception {
        Endpoint endpoint = CamelContextHelper.getMandatoryEndpoint(routeBuilder.getContext(), resourceUri);
        return new Enricher(aggregationStrategy(aggregatorBeanName).aggregationInput(routeBuilder.body()), endpoint.createProducer());
    }

    /**
     * Creates a {@link Validation} process object.
     * 
     * @param validatorUri
     *            URI of the endpoint that validates an exchange.
     * @return a validation process object.
     * @throws Exception
     *             if a producer cannot be created for the endpoint represented
     *             by <code>validatorUri</code>.
     */
    public Validation validation(String validatorUri) throws Exception {
        Endpoint endpoint = CamelContextHelper.getMandatoryEndpoint(routeBuilder.getContext(), validatorUri);
        return new Validation(endpoint.createProducer());
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
    
}
