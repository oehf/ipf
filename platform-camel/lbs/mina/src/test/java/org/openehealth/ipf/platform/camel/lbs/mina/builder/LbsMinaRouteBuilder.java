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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;

/**
 * @author Jens Riemschneider
 */
public class LbsMinaRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        errorHandler(defaultErrorHandler().maximumRedeliveries(2).redeliveryDelay(0));
        
        from("mina:tcp://localhost:6123?textline=true&sync=true")
            .to("mock:mock");

        from("mina:tcp://localhost:6124?textline=true&sync=true")
            .unmarshal().hl7()
            .to("mock:mock");
        
        from("mina:tcp://localhost:6125?sync=true&codec=#mllpStoreCodec")
            .process(new MyProcessor(1))
            .to("mock:mock");

        from("mina:tcp://localhost:6126?sync=true&codec=#mllpStoreCodec")
            .process(new MyProcessor(2))
            .unmarshal().hl7()
            .process(new MyProcessor(3))
            .to("mock:mock");
        
        from("mina:tcp://localhost:6127?sync=true&codec=#mllpStoreCodec")
            .process(new MyProcessor(4))
            .unmarshal().hl7()
            .process(new MyProcessor(5))
            .marshal().hl7()
            .process(new MyProcessor(6))
            .to("mina:tcp://localhost:6126?sync=true&codec=#mllpStoreCodec");

        from("mina:tcp://localhost:6128?sync=true&codec=#mllpStoreCodec")
            .process(new MyProcessor(7))
            .to("mina:tcp://localhost:6126?sync=true&codec=#mllpStoreCodec");
    }


    private static class MyProcessor implements Processor {
        final int x;

        private MyProcessor(int x) {
            this.x = x;
        }

        @Override
        public void process(Exchange exchange) throws Exception {
             System.out.println(x);
        }
    }
}
