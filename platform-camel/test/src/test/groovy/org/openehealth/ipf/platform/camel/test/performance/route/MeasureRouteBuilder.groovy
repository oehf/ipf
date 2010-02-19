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
package org.openehealth.ipf.platform.camel.test.performance.route

import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder

/**
 * Contains test routes, that are instrumented for performance measurement
 * 
 * @author Mitko Kolev
 */
class MeasureRouteBuilder extends SpringRouteBuilder {

    //TODO add a test with a JMS queue
     
    void configure() {
        errorHandler(noErrorHandler())
        
        from('direct:basic') 
                
                .measure().time()
                .process {}  
                .measure().finish('finish')
                .to('mock:output')
        
        from('direct:one_checkpoint') 
                .measure().time()
                .process {}  
                .measure().checkpoint('checkpoint')
                .process{}
                .measure().finish('finish')
                .to('mock:output')
        
        from('direct:split') 
                .measure().time()
                .ipf().split { Exchange exchange -> 
                    exchange.in.getBody(String.class).split(',')
                }
                .measure().finish('finish')
                .to('mock:output')
        
        
        //each checkpoint(String) is preceeded with time()		
        from('direct:three_explicit_checkpoints') 
                .measure().time()
                .process {}  
                .measure().time()
                .measure().checkpoint('1')
                .measure().time()
                .process{}
                .measure().checkpoint('2')
                .measure().time()
                .process{}
                .measure().checkpoint('3')
                .process{}
                .measure().finish('finish')
                .to('mock:output')		
        
        //checkpoints followed by checkpoints directly
        from('direct:three_checkpoints') 
                .measure().time()
                .process {}  
                .measure().checkpoint('1')
                .process{}
                .measure().checkpoint('2')
                .process{}
                .measure().checkpoint('3')
                .process{}
                .measure().finish('finish')
                .to('mock:output')		
    }
}
