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

import static org.apache.camel.builder.Builder.*

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

import org.apache.camel.builder.RouteBuilder

/**
 * @author Martin Krasser
 */
class SampleRouteBuilderConfig implements RouteBuilderConfig {
        
    void apply(RouteBuilder builder) {
        
        builder
            .input('direct:input1')
            .transmogrify('blubConverter')  
            .filter('test')             
            .output('mock:output')          
    
        builder
            .from('direct:input2') 
            .transmogrifyAndFilter('blubConverter', 'blah') 
            .to('mock:output')     

         builder
            .from('direct:input3')
            .transmogrify {input, headers -> 
                input == 'blub' ? headers.foo : input
            }  
            .filter('test')
            .to('mock:output')         

        /* ----------------------------------------------------------------- *
            Transmogrifier closures are always given in-body and in-header
            as closure arguments. Any additional expressions like params.foo
            can be defined in the closure itself or currying can be used to
            support a more dynamic way of applying exressions to in-body and
            in-header. For example:
         * ----------------------------------------------------------------- */

        def closure1 = {expr, input, headers -> 
            input == 'blub' ? expr(headers) : input
        }
        
        def closure2 = closure1.curry {headers ->
            headers.foo + headers.bar 
        }
        
        builder
            .from('direct:input4')
            .transmogrify(closure2)              // use curried closure 
            .filter('test')
            .to('mock:output')         

       // Test two delegate processors in sequence with in-out exchange
            
        builder
            .from('direct:input5')
            .transmogrify {'result'}
            .validate {it == 'result'}
            .to('mock:output')

        builder
            .from('direct:input6')
            // runtime exception thrown by closure
            // is not wrapped
            .onException(IllegalArgumentException.class)
                .handled(true)
                .process { it.out.body = 'failure' }
                .end()
            .transmogrify { body ->
                if (body == 'error') {
                    // runtime exception
                    throw new IllegalArgumentException(body)
                }
                body
            }
            .to('mock:output')
                        
        builder
            .from('direct:input7')
            // checked exception thrown by closure
            // wrapped by GroovyRuntimeException
            .onException(GroovyRuntimeException.class)
                .handled(true)
                .process { it.out.body = 'failure' }
                .end()
            .transmogrify { body ->
                if (body == 'error') {
                    // checked exception
                    throw new IOException(body)
                }
                body
            }
            .to('mock:output')
                        
            
    }
    
}