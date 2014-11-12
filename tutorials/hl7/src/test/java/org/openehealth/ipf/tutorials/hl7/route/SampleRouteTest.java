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
package org.openehealth.ipf.tutorials.hl7.route;

import java.io.InputStream;
import java.util.Scanner;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import org.apache.camel.ProducerTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = { "/context.xml" })
public class SampleRouteTest {

    @Autowired
    private ProducerTemplate producerTemplate;
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRoute() throws Exception {
        Resource input = new ClassPathResource("/msg-01.hl7");
        producerTemplate.sendBody("direct:input", input.getInputStream());
        Resource result = new FileSystemResource("target/output/HZL.hl7");
        assertEquals(
                load(getClass().getResourceAsStream("/msg-01.hl7.expected")).toString(),
                load(result.getInputStream()).toString());
    }

    protected static <T extends Message> T load(InputStream is) throws HL7Exception {
        return (T)new PipeParser().parse(
                new Scanner(is).useDelimiter("\\A").next());
    }
}
