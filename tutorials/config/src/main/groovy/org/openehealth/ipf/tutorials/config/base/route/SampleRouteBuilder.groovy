/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.tutorials.config.base.route

import org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilder

/**
 * 
 * @author Boris Stanojevic
 */
class SampleRouteBuilder extends CustomRouteBuilder {

    void configure() {

        from('jetty:http://0.0.0.0:8800/reverse')
            .convertBodyTo(String.class)
            .multicast().to('direct:file-save','direct:reverse-response')

        from('direct:reverse-response')
            .transmogrify{'reversed response: ' + it.reverse()}
		
		from('jetty:http://0.0.0.0:8800/map')
            .convertBodyTo(String.class)
            .unmarshal().hl7()
			.to('direct:map')

        from('direct:map')
            .marshal().hl7()
            .to('direct:file-save')
            .transmogrify{'map response ok!'}
		
		from('direct:file-save')
            .setFileHeaderFrom('destination')
            .to('file:target/output')
    }
    
}
