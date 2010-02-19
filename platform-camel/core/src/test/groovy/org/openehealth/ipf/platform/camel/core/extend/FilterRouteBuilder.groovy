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

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.support.transform.min.TestPredicate

/**
 * @author Martin Krasser
 */
class FilterRouteBuilder extends SpringRouteBuilder {
    
    void configure() {
       
        errorHandler(noErrorHandler())
       
        def predicate1 = predicate(new TestPredicate('blub'))
        def predicate2 = predicate('samplePredicate') 
        def predicate3 = predicate { body -> body == 'blub'} 
        
        from('direct:input1')
            .filter(predicate1)
            .to('mock:output')

        from('direct:input2')
            .filter(predicate2)
            .to('mock:output')

        from('direct:input3')
            .filter(predicate3)
            .to('mock:output')
            
        from('direct:input4')
            .filter {exchange -> exchange.in.body == 'blub'}
            .to('mock:output')
            
    }
    
}
