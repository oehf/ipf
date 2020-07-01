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
package org.openehealth.ipf.platform.camel.core.extend

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.platform.camel.core.support.transform.min.TestConverter

/**
 * @author Martin Krasser
 */
class DataFormatRouteBuilder extends RouteBuilder {
    
    void configure() {
        
        def converter = new TestConverter()
        
        from("direct:external1")
            .unmarshal()
            .parse(converter)
        
        from("direct:internal1")
            .marshal()
            .render(converter)
            .convertBodyTo(String.class)
            
        from("direct:external2")
            .unmarshal()
            .parse('sampleConverter')
        
        from("direct:internal2")
            .marshal()
            .render('sampleConverter')
            .convertBodyTo(String.class)
            
    }
    
}