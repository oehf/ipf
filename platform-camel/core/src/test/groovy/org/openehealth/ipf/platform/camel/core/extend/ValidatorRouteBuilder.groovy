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

import org.openehealth.ipf.commons.core.modules.api.ValidationException
import org.openehealth.ipf.platform.camel.core.support.transform.min.TestValidator

import org.apache.camel.spring.SpringRouteBuilder

/**
 * @author Martin Krasser
 */
class ValidatorRouteBuilder extends SpringRouteBuilder {
    
    void configure() {
        
        onException(ValidationException.class).to('mock:error')
        
        from('direct:input1') 
            .validate {body -> body == 'blah'}  
            .to('mock:output')
    
        from('direct:input2') 
            .validate {throw new ValidationException('juhu')}  
            .to('mock:output')
    
        from('direct:input3') 
            .validate {body, profile -> 
                !profile // profile is null
            }  
            .to('mock:output')
    
        from('direct:input4') 
            .validate {body, profile -> 
                profile == 'abcd'
            }
            .staticProfile('abcd')
            .to('mock:output')

        from('direct:input5') 
            .validate {message, profile -> 
                profile == message.body
            }
            .input {exchange -> exchange.in}
            .staticProfile('abcd')
            .to('mock:output')

        from('direct:input6') 
            .validate('sampleValidator')
            .staticProfile('bean')
            .to('mock:output')
    
        from('direct:input7') 
            .validate(new TestValidator())
            .staticProfile('object')
            .to('mock:output')
            
        from('direct:input8') 
            .validate {body, profile -> profile == 'derived'}
            .profile { it.in.headers.profile }
            .to('mock:output')
    }

}
