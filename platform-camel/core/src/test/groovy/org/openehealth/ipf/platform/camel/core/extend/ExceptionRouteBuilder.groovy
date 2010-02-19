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
package org.openehealth.ipf.platform.camel.core.extend

import org.apache.camel.spring.SpringRouteBuilder

/**
 * @author Martin Krasser
 */
class ExceptionRouteBuilder extends SpringRouteBuilder {
    
    void configure() {
        
        onException(Exception.class).onWhen {it.in.body == 'abc'}.handled(true).to('mock:error1')
        onException(Exception.class).onWhen {it.in.body == 'def'}.handled(true).to('mock:error2')

        onException(Exception.class).onWhen {it.in.body ==~ /u.w/}.handled(true).to('mock:error1')
        onException(Exception.class).onWhen {it.in.body ==~ /x.z/}.handled(true).to('mock:error2')

        from('direct:input').process {throw new Exception('test')}

    }
    
}