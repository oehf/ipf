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

import org.openehealth.ipf.platform.camel.core.support.transformer.ConstantTransformer;
import org.openehealth.ipf.platform.camel.core.support.transformer.FailureTransformer;

import org.apache.camel.spring.SpringRouteBuilder

/**
 * @author Martin Krasser
 */
class ValidationRouteBuilder extends SpringRouteBuilder {
    
    void configure() {
        
       errorHandler(noErrorHandler())
       
       // -------------------------------------------------------------
       // Use validation process with responder object
       // -------------------------------------------------------------
       
       from('direct:input1') 
            // responder (transformer) generates constant result
            .validation(new ConstantTransformer('result'))
            .to('mock:output')
            
       from('direct:input2') 
            // responder (transformer) generates fault
            .validation(new FailureTransformer(false))
            .to('mock:output')
            
       from('direct:input3') 
            // responder (transformer) throws exception
            .validation(new FailureTransformer(true))
            .to('mock:output')
            
       // -------------------------------------------------------------
       // Use validation process with responder endpoint
       // -------------------------------------------------------------

        from('direct:input4') 
            .validation('direct:result-responder')
            .to('mock:output')
            
        from('direct:input5') 
            .validation('direct:fault-responder')
            .to('mock:output')
            
        from('direct:input6') 
            .validation('direct:error-responder')
            .to('mock:output')
       
       from('direct:result-responder').process(new ConstantTransformer('result'));
       from('direct:fault-responder').process(new FailureTransformer(false));
       from('direct:error-responder').process(new FailureTransformer(true));
       
       // -------------------------------------------------------------
       // Use validation process with responder closure
       // -------------------------------------------------------------

       from('direct:input7') 
           .validation {exchange -> exchange.out.body = 'result'}
           .to('mock:output')
       
       from('direct:input8') 
           .validation {exchange -> exchange.fault.body = 'failed'}
           .to('mock:output')
           
       from('direct:input9') 
           .validation {throw new RuntimeException('failed')}
           .to('mock:output')
      
    }

}
