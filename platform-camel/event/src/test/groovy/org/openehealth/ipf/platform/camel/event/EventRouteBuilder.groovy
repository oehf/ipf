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
package org.openehealth.ipf.platform.camel.event

import org.apache.camel.spring.SpringRouteBuilder

/**
 * @author Jens Riemschneider
 */
class EventRouteBuilder extends SpringRouteBuilder {
     
    void configure() {
        errorHandler(defaultErrorHandler().maximumRedeliveries(2).redeliveryDelay(0));
        
        from('direct:start_simple')
            .publish { new MyEventImpl1('hello world') }
            .to('mock:mock')

        from('direct:start_unsub_topic')
            .publish { new MyEventImpl1('hello world') }.toTopic('noone_subscribed')                
            .to('mock:mock')
            
        from('direct:start_topic')
            .publish { new MyEventImpl1('hello world') }.toTopic('test')                
            .to('mock:mock')

        from('direct:start_filter')
            .publish { new MyEventImpl2() }.toTopic('filtered')
            .to('mock:mock')
    }
}    
