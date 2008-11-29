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
package org.openehealth.ipf.platform.camel.core.adapter.builder;

import org.apache.camel.Processor;
import org.openehealth.ipf.platform.camel.core.builder.RouteBuilder;


/**
 * @author Martin Krasser
 */
public class TransmogrifierRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:transmogrifier-test-1")
        .process(transmogrifier1());

        from("direct:transmogrifier-test-2")
        .process(transmogrifier2());

        from("direct:transmogrifier-test-3")
        .process(transmogrifier3());
    
        from("direct:transmogrifier-test-4")
        .process(transmogrifier1());
    }

    private Processor transmogrifier1() {
    	// take input from body (no params)
        return transmogrifier("testTransmogrifier");
    }
    
    private Processor transmogrifier2() {
    	// take input from body (static params)
        return transmogrifier("testTransmogrifier")
            .staticParams(" eats", " mice");
    }
    
    private Processor transmogrifier3() {
    	// take input from foo header (static params)
        return transmogrifier("testTransmogrifier")
            .staticParams(" likes", " fish")
            .input(header("foo"));
    }
    
}
