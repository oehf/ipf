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

import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultExchange

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

import org.openehealth.ipf.commons.event.EventChannelAdapter
import org.openehealth.ipf.commons.event.EventObject

import org.openehealth.ipf.platform.camel.event.MyEventImpl1
import org.openehealth.ipf.platform.camel.event.MyEventImpl2

/**
 * @author Jens Riemschneider
 */
class EventRouteBuilderConfig implements RouteBuilderConfig {
     
    void apply(RouteBuilder builder) {
        builder.errorHandler(builder.deadLetterChannel().maximumRedeliveries(2).initialRedeliveryDelay(0));
        
        builder.from('direct:start_simple')
            .publish { new MyEventImpl1('hello world') }
            .to('mock:mock')

        builder.from('direct:start_unsub_topic')
            .publish { new MyEventImpl1('hello world') }.toTopic('noone_subscribed')                
            .to('mock:mock')
            
        builder.from('direct:start_topic')
            .publish { new MyEventImpl1('hello world') }.toTopic('test')                
            .to('mock:mock')

        builder.from('direct:start_filter')
            .publish { new MyEventImpl2() }.toTopic('filtered')
            .to('mock:mock')
    }
}    
