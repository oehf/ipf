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
package org.openehealth.ipf.platform.camel.core.adapter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.AbstractRouteTest;
import org.openehealth.ipf.platform.camel.test.domain.Cat;
import org.openehealth.ipf.platform.camel.test.domain.Dog;


/**
 * @author Martin Krasser
 */
public class TransmogrifierRouteTest extends AbstractRouteTest {

    @Test
    public void testTransmogrifier1() throws InterruptedException {
        Cat cat = (Cat) producerTemplate.sendBody("direct:transmogrifier-test-1",
                ExchangePattern.InOut, new Dog("Willi"));
        assertEquals(new Cat("Willi"), cat);
    }

    @Test
    public void testTransmogrifier2() throws InterruptedException {
        Cat cat = (Cat) producerTemplate.sendBody("direct:transmogrifier-test-2",
                ExchangePattern.InOut, new Dog("Willi"));
        assertEquals(new Cat("Willi eats mice"), cat);
    }

    @Test
    public void testTransmogrifier3() throws InterruptedException {
        Cat cat = (Cat) producerTemplate.sendBodyAndHeader("direct:transmogrifier-test-3", "wrong", "foo",
                new Dog("Fritz"));
        assertEquals(new Cat("Fritz likes fish"), cat);
    }

    @Test
    public void testTransmogrifier4() throws InterruptedException {
        Exchange exchange = producerTemplate.request("direct:transmogrifier-test-4", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader("x", "y");
                exchange.getIn().setBody(new Dog("Willi"));
            }
        });
        assertNotNull(exchange.getOut(false));
        assertEquals("y", exchange.getOut(false).getHeader("x"));
    }

}
