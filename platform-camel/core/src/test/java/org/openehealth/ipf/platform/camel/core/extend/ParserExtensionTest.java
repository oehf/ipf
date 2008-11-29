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
package org.openehealth.ipf.platform.camel.core.extend;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-parser.xml" })
public class ParserExtensionTest extends AbstractExtensionTest {
    
    @Test
    public void testParserObject() throws InterruptedException {
        mockOutput.expectedBodiesReceived("string: blah");
        producerTemplate.sendBody("direct:input1", "blah");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testParserBean() throws InterruptedException {
        mockOutput.expectedBodiesReceived("string: blub");
        producerTemplate.sendBody("direct:input2", "blub");
        mockOutput.assertIsSatisfied();
    }
    
}
