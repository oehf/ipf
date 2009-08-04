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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;


/**
 * @author Jens Riemschneider
 */
@ContextConfiguration(locations = { "/context-core-extend-splitter.xml" })
public class SplitterExtensionTest extends AbstractExtensionTest {
    @Test
    public void testRouteWithClosureExpression() throws Exception {
        mockOutput.expectedBodiesReceived("bla", "blu");
        checkSuccess("direct:split_rule_as_closure", "bla,blu", "bla:blu");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testRouteWithSplitRuleThatReturnsArray() throws Exception {
        mockOutput.expectedBodiesReceived("bla", "blu");
        checkSuccess("direct:split_rule_returns_array", "bla,blu", "bla:blu");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testRouteWithExpression() throws Exception {
        mockOutput.expectedBodiesReceived("bla", "blu");
        checkSuccess("direct:split_rule_as_type", "bla,blu", "bla:blu");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testRouteWithSpecifiedAggregate() throws Exception {
        mockOutput.expectedBodiesReceived("bla", "blu");
        checkSuccess("direct:aggregation_via_closure", "bla,blu", "bla:blu");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testRouteWithDefaultAggregation() throws Exception {
        mockOutput.expectedBodiesReceived("bla", "blu");        
        checkSuccess("direct:split_default_aggr", "bla,blu", "blu");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testRouteWithRuleDefinedViaBean() throws Exception {
        mockOutput.expectedBodiesReceived("bla", "blu");        
        checkSuccess("direct:split_rule_via_bean", "bla,blu", "blu");
        mockOutput.assertIsSatisfied();
    }    
    
    /**
     * Ensures that the iterator returned by a split rule as well as the split
     * rule itself is only used once. This is necessary for streaming usage to
     * avoid reading the stream multiple times 
     */
    @Test
    public void testIteratorOnlyUsedOnce() throws Exception {
        mockOutput.expectedBodiesReceived("bla", "blu");        
        checkSuccess("direct:split_only_once_iterator", "bla,blu", "blu");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testSplitFileLineByLine() throws Exception {
        final File file = File.createTempFile("SplitterExtensionTest", "txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("bla\nblu");            
        writer.close();
        
        try {
            mockOutput.expectedBodiesReceived("bla", "blu");        
            checkSuccess("direct:split_read_file_lines", file.getAbsolutePath(), "blu");
            mockOutput.assertIsSatisfied();
        }
        finally {
            assertTrue(file.delete());
        }
    }
    
    @Test
    public void testRouteWithDefaultUpdate() throws Exception {
        mockOutput.expectedBodiesReceived("bla", "blu");        
        checkSuccess("direct:split_default_update", "bla,blu", "blu");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testComplexRoute() throws Exception {
        mockOutput.expectedBodiesReceived("blu");        
        mockOutput.allMessages().header("foo").isEqualTo("bar");
        mockOutput.allMessages().header("smurf").isEqualTo("blue");
        checkSuccess("direct:split_complex_route", "bla,blu", "blu");
        mockOutput.assertIsSatisfied();
    }

    private final static int NUMBER_OF_LINES = 10000;
    
    @Test
    public void testHugeFile() throws Exception {
        File file = File.createTempFile("testHugeFile", "txt");
        try {
            mockOutput.expectedBodiesReceived("Line 1");        

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (int idx = 1; idx <= NUMBER_OF_LINES; ++idx) {
                writer.write("Line " + idx + ",some other text\n");            
            }
            writer.close();
            
            checkSuccess("direct:split_huge_file", file.getAbsolutePath(), "some other text");
            mockOutput.assertIsSatisfied();
        }
        finally {
            file.delete();
        }
    }

    private void checkSuccess(final String endpoint, 
            final String inBody, 
            final String expectedResult) {
        
        Exchange result = producerTemplate.request(endpoint, new Processor() {
            public void process(Exchange exchange) {
                exchange.getIn().setBody(inBody);
            }
        });
        assertNotNull(result);
        assertEquals(expectedResult, result.getOut().getBody());
    }
}
