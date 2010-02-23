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
package org.openehealth.ipf.platform.camel.lbs.core.process;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.processor.DelegateProcessor;

/**
 * Base class for processors that require an {@link ResourceHandler} to 
 * process exchanges.
 * @author Jens Riemschneider
 */
public abstract class ResourceHandlingProcessor extends DelegateProcessor {
    private final List<ResourceHandler> resourceHandlers = new ArrayList<ResourceHandler>();

    /**
     * Sets the {@link ResourceHandler} of the processor.
     * <p>
     * A resource handler enables the processor to handle a specific type of
     * endpoint exchange. The handler contains the actual strategy for 
     * integrating/extracting resources into/from a specific message type 
     * (e.g. an HTTP message).
     * <p>
     * This method can be called multiple times to add multiple handlers.
     * @param handlers
     *          handlers for integrating and extracting resources
     * @return this instance for usage with a fluent API
     */
    public ResourceHandlingProcessor with(List<ResourceHandler> handlers) {
        notNull(handlers, "handlers cannot be null");
        resourceHandlers.addAll(handlers);
        return this;
    }
    
    /**
     * @return the resource handlers configured by {@link #with}
     */
    protected final List<ResourceHandler> getResourceHandlers() {
        return resourceHandlers;
    }
    
    /**
     * @return {@code true} if an resource handler was configured
     */
    protected final boolean hasResourceHandler() {
        return resourceHandlers.size() > 0;
    }
    
    /* (non-Javadoc)
     * @see org.apache.camel.processor.DelegateProcessor#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: resourceHandlers=%2$s, super=%3$s}",
                getClass().getSimpleName(), resourceHandlers, super.toString());
    }
}
