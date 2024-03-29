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

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.platform.camel.core.AbstractRouteTest;


/**
 * @author Martin Krasser
 */
public class PredicateRouteTest extends AbstractRouteTest {

    @Test
    public void testPredicate1() throws InterruptedException {
        mock.expectedBodiesReceived("test");
        producerTemplate.sendBody("direct:predicate-test-1", "test");
        mock.assertIsSatisfied();
    }

    @Test
    public void testPredicate2() throws InterruptedException {
        mock.expectedMessageCount(0);
        producerTemplate.sendBodyAndHeader("direct:predicate-test-2", "body", "foo", "wrong");
        mock.assertIsSatisfied(); // no need to wait (route is synchronous)
    }

}
