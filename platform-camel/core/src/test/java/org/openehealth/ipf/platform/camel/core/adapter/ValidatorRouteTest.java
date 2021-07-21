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

import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.xml.SchematronValidationException;
import org.openehealth.ipf.platform.camel.core.AbstractRouteTest;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Martin Krasser
 * @author Christian Ohr
 */
public class ValidatorRouteTest extends AbstractRouteTest {

    @EndpointInject(value = "mock:error")
    protected MockEndpoint error;

    @AfterEach
    public void tearDownError() throws Exception {
        error.reset();
    }

    @Test
    public void testValidator1() throws InterruptedException {
        var result = (String) producerTemplate.sendBody(
                "direct:validator-test", ExchangePattern.InOut, "correct");
        assertEquals("correct", result);
    }

    @Test
    public void testValidator2() throws InterruptedException {
        error.expectedMessageCount(1);
        var exchange = producerTemplate.send("direct:validator-test",
                ExchangePattern.InOut, exchange1 -> exchange1.getIn().setBody("incorrect"));
        assertEquals(ValidationException.class, exchange.getException().getClass());
        error.assertIsSatisfied(2000);
    }

    @Test
    public void testValidator3() throws InterruptedException, IOException {
        final var xml = IOUtils.toString(getClass().getResourceAsStream("/xsd/test.xml"), Charset.defaultCharset());
        var response = (String)producerTemplate.sendBody(
        		"direct:validator-xml-test", ExchangePattern.InOut, xml);
        assertEquals("passed", response);
     }

    @Test
    public void testValidator4() throws InterruptedException, IOException {
        final var xml = IOUtils.toString(getClass().getResourceAsStream("/xsd/invalidtest.xml"), Charset.defaultCharset());
        error.expectedMessageCount(1);
        var exchange = producerTemplate.send("direct:validator-xml-test",
                ExchangePattern.InOut, exchange1 -> exchange1.getIn().setBody(xml));
        assertEquals(ValidationException.class, exchange.getException()
                .getClass());
        var e = (ValidationException) exchange.getException();
        assertEquals(5, e.getCauses().length);
        error.assertIsSatisfied(2000);
    }

    @Test
    public void testValidator5() throws InterruptedException, IOException {
        final var xml = IOUtils.toString(getClass().getResourceAsStream("/schematron/schematron-test.xml"), Charset.defaultCharset());
        var response = (String)producerTemplate.sendBody(
        		"direct:validator-schematron-test", ExchangePattern.InOut, xml);
        assertEquals("passed", response);
     }

    @Test
    public void testValidator6() throws InterruptedException, IOException {
        final var xml = IOUtils.toString(getClass().getResourceAsStream("/schematron/schematron-test-fail.xml"), Charset.defaultCharset());
        error.expectedMessageCount(1);
        var exchange = producerTemplate.send("direct:validator-schematron-test",
                ExchangePattern.InOut, exchange1 -> exchange1.getIn().setBody(xml));
        assertEquals(SchematronValidationException.class, exchange.getException().getClass());
        var e = (ValidationException) exchange.getException();
        assertEquals(3, e.getCauses().length);
        error.assertIsSatisfied(2000);

    }    
}
