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
import org.openehealth.ipf.platform.camel.core.extend.DefaultConfigExtender;
import org.openehealth.ipf.platform.camel.core.extend.RouteModelExtender;
import org.openehealth.ipf.platform.camel.core.process.Enricher;
import org.openehealth.ipf.platform.camel.core.process.Validation;

/**
 * Route builder with support methods for common platform DSL extensions.
 * <p>
 * <strong>This route builder will be deprecated in one of the upcoming
 * releases.</strong>. It is recommended to use the {@link SpringRouteBuilder}
 * and {@link RouteHelper} instead.
 * 
 * @author Martin Krasser
 */
@SuppressWarnings("deprecation")
public class RouteBuilder extends SpringRouteBuilder {

    public static final String DEFAULT_CONTEXT_PROPERTY_NAME = "context"; 
    
    // ----------------------------------------------------------------
    //  Configuration
    // ----------------------------------------------------------------
    
    private String contextPropertyName = DEFAULT_CONTEXT_PROPERTY_NAME;
    
    private RouteModelExtender routeModelExtender;
    
    private RouteBuilderConfig routeBuilderConfig;
    
    private DefaultConfigExtender routeConfigExtender;
    
    private RouteHelper routeHelper;
    
    public RouteBuilder() {
        routeConfigExtender = new DefaultConfigExtender();
        routeConfigExtender.setRouteBuilder(this);
        routeHelper = new RouteHelper(this);
    }
    
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
     * Returns the name of the context property to be dynamically defined on the
     * {@link #routeBuilderConfig} object of this builder. This is only relevant
     * for {@link RouteBuilderConfig} Groovy implementations that want to lookup
     * Spring beans from the context object.
     * 
     * @return the contextPropertyName
     * @see #setContextPropertyName(String)
     */
    public String getContextPropertyName() {
        return contextPropertyName;
    }

    /**
     * Set the name of the context property to be dynamically defined on the
     * {@link #routeBuilderConfig} object of this builder. The default name is
     * <code>context</code>. If this conflicts with existing properties defined
     * for {@link #routeBuilderConfig} then the property name can be changed
     * using this setter. This is only relevant for {@link RouteBuilderConfig}
     * Groovy implementations that want to lookup Spring beans from the context
     * object.
     * 
     * @param contextPropertyName
     *            the contextPropertyName to set
     */
    public void setContextPropertyName(String contextPropertyName) {
        this.contextPropertyName = contextPropertyName;
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
            // dynamically add a property for bean lookups from context
            routeConfigExtender.defineContextProperty(routeBuilderConfig);
            routeBuilderConfig.apply(this);
        }
    }
    
    // ----------------------------------------------------------------
    //  Adapter
    // ----------------------------------------------------------------
    
    public PredicateAdapter predicate(Predicate predicate) {
        return routeHelper.predicate(predicate);
    }
    
    public PredicateAdapter predicate(String predicateBeanName) {
        return routeHelper.predicate(predicateBeanName);
    }
    
    public ConverterAdapter converter(String converterBeanName) {
        return routeHelper.converter(converterBeanName);
    }

    public ParserAdapter parser(String parserBeanName) {
        return routeHelper.parser(parserBeanName);
    }

    public ParserAdapter parser(Parser<?> parser) {
        return routeHelper.parser(parser);
    }

    public RendererAdapter renderer(String rendererBeanName) {
        return routeHelper.renderer(rendererBeanName);
    }

    public RendererAdapter renderer(Renderer<?> renderer) {
        return routeHelper.renderer(renderer);
    }

    public TransmogrifierAdapter transmogrifier(String transmogrifierBeanName) {
        return routeHelper.transmogrifier(transmogrifierBeanName);
    }
 
    public TransmogrifierAdapter transmogrifier(Transmogrifier<?, ?> transmogrifier) {
        return routeHelper.transmogrifier(transmogrifier);
    }
 
    public ValidatorAdapter validator(String validatorBeanName) {
        return routeHelper.validator(validatorBeanName);
    }

    public ValidatorAdapter validator(Validator<?, ?> validator) {
        return routeHelper.validator(validator);
    }

    public AggregatorAdapter aggregationStrategy(Aggregator aggregator) {
        return routeHelper.aggregationStrategy(aggregator);
    }
    
    public AggregatorAdapter aggregationStrategy(String aggregatorBeanName) {
        return routeHelper.aggregationStrategy(aggregatorBeanName);
    }
    
    public DataFormatAdapter dataFormatParser(Parser<?> parser) {
        return routeHelper.dataFormatParser(parser);
    }
    
    public DataFormatAdapter dataFormatParser(String parserBeanName) {
        return routeHelper.dataFormatParser(parserBeanName);
    }
    
    public DataFormatAdapter dataFormatRenderer(Renderer<?> renderer) {
        return routeHelper.dataFormatRenderer(renderer);
    }
    
    public DataFormatAdapter dataFormatRenderer(String rendererBeanName) {
        return routeHelper.dataFormatRenderer(rendererBeanName);
    }
    
    // ----------------------------------------------------------------
    //  Processor
    // ----------------------------------------------------------------

    public Enricher enricher(AggregationStrategy aggregationStrategy, String resourceUri) throws Exception {
        return routeHelper.enricher(aggregationStrategy, resourceUri);
    }
    
    public Enricher enricher(String aggregatorBeanName, String resourceUri) throws Exception {
        return routeHelper.enricher(aggregatorBeanName, resourceUri);
    }

    public Validation validation(String validatorUri) throws Exception {
        return routeHelper.validation(validatorUri);
    }
    
    public Validation validation(Processor validator) {
        return routeHelper.validation(validator);
    }
    
    // ----------------------------------------------------------------
    //  Bridge
    // ----------------------------------------------------------------
    
    /**
     * Creates an {@link InOnlyBridge}.
     * 
     * @return an {@link InOnlyBridge}.
     */
    @Deprecated
    public DelegateProcessor inOnlyBridge() {
        return new InOnlyBridge();
    }

}
