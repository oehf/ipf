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
package org.openehealth.ipf.platform.camel.lbs.builder;

import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.platform.camel.lbs.process.FetchProcessor;
import org.openehealth.ipf.platform.camel.lbs.process.StoreProcessor;

/**
 * Route builder with support methods for lbs management DSL extensions.
 * 
 * @author Jens Riemschneider
 */
public class RouteBuilder extends org.openehealth.ipf.platform.camel.core.builder.RouteBuilder {

    // ----------------------------------------------------------------
    //  LBS management
    // ----------------------------------------------------------------
    /**
     * Creates a store processor.
     * <p>
     * The created processor extracts binary data from a protocol dependent 
     * Camel input body (e.g. an HTTP request from the jetty endpoint) and 
     * creates a resource that is stored in a {@link LargeBinaryStore}.
     * @return the created store processor
     */
    public StoreProcessor store() {        
        return new StoreProcessor();
    }

    /**
     * Creates a fetch processor.
     * <p>
     * The created processor integrates resources into a protocol dependent 
     * Camel input body (e.g. an HTTP request for the http endpoint).
     * @return the created fetch processor
     */
    public FetchProcessor fetch() {        
        return new FetchProcessor();
    }
}
