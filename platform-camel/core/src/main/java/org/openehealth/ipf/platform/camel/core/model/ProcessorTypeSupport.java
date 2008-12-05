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
package org.openehealth.ipf.platform.camel.core.model;

import org.apache.camel.model.ProcessorType;
import org.apache.camel.model.RouteType;

/**
 * Support class for processor type extensions that enables inheritance features
 * for processors that can have child processors. Usually processors do require
 * child processors if the fluent API returns an instance of the processor type.
 * In this case, additional processors will be created as children of the
 * processor and not of the route. Processors that can have children must
 * provide a configuration strategy for their children that is similar to that
 * of the route, i.e. they need functionality that is actually implemented in
 * {@link RouteType}, especially the inheritance of the error handler as it done
 * in {@link RouteType#configureChild}.
 * 
 * @author Jens Riemschneider
 */
public abstract class ProcessorTypeSupport extends ProcessorType<ProcessorType> {

    /**
     * Configures the child processor. This implementation ensures that the
     * child inherits the error handler from the parent if the parent is
     * configured to inherit it.
     * 
     * @param output
     *            the processor type of the child processor
     */
    @Override
    protected void configureChild(ProcessorType output) {
        super.configureChild(output);
        if (isInheritErrorHandler()) {
            output.setErrorHandlerBuilder(getErrorHandlerBuilder());
        }
    }
}
