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
package org.openehealth.ipf.platform.camel.core.model;

import org.apache.camel.Processor;
import org.apache.camel.model.OutputDefinition;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.core.process.Validation;

/**
 * @author Martin Krasser
 */
public class ValidationDefinition extends OutputDefinition<ValidationDefinition>{

    private final Processor responseGeneratorProcessor;
    
    private final String responseGeneratorUri;
    
    public ValidationDefinition(String responseGeneratorUri) {
        this(null, responseGeneratorUri);
    }
    
    public ValidationDefinition(Processor responseGeneratorProcessor) {
        this(responseGeneratorProcessor, null);
    }
    
    private ValidationDefinition(Processor responseGeneratorProcessor, String responseGeneratorUri) {
        this.responseGeneratorProcessor = responseGeneratorProcessor;
        this.responseGeneratorUri = responseGeneratorUri;
    }
    
    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Validation validation = createValidationProcessor(routeContext);
        validation.setProcessor(createChildProcessor(routeContext, false));
        return validation;
    }
    
    private Validation createValidationProcessor(RouteContext routeContext) throws Exception {
        if (responseGeneratorUri != null) {
            return new Validation(routeContext.resolveEndpoint(responseGeneratorUri).createProducer());
        } else {
            return new Validation(responseGeneratorProcessor);
        }
    }
    
}
