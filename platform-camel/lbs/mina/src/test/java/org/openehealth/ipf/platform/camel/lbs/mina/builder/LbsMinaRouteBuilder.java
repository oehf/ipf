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
package org.openehealth.ipf.platform.camel.lbs.mina.builder;

import org.apache.camel.spring.SpringRouteBuilder;

/**
 * @author Jens Riemschneider
 */
public class LbsMinaRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel().maximumRedeliveries(2).initialRedeliveryDelay(0));
        
        from("mina:tcp://localhost:6123?textline=true&sync=true")
            .to("mock:mock");

        from("mina:tcp://localhost:6124?sync=true")
            .unmarshal().hl7()
            .to("mock:mock");
        
        from("mina:tcp://localhost:6125?sync=true&codec=mllpStoreCodec")
        	.to("mock:mock");

        from("mina:tcp://localhost:6126?sync=true&codec=mllpStoreCodec")
            .unmarshal().hl7()
            .to("mock:mock");
        
        from("mina:tcp://localhost:6127?sync=true&codec=mllpStoreCodec")
            .unmarshal().hl7()
            .marshal().hl7()
            .to("mina:tcp://localhost:6126?sync=true&codec=mllpStoreCodec");

        from("mina:tcp://localhost:6128?sync=true&codec=mllpStoreCodec")
            .to("mina:tcp://localhost:6126?sync=true&codec=mllpStoreCodec");
    }
}
