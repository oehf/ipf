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

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.xml.sax.SAXException;

import javax.xml.transform.dom.DOMResult;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Martin Krasser
 * @author Stefan Ivanov
 */
@ContextConfiguration(locations = { "/context-core-extend-transmogrifier.xml" })
public class TransmogrifierExtensionTest extends AbstractExtensionTest {

    @Test
    public void testReply() {
        var exchange = producerTemplate.request("direct:reply", exchange1 -> exchange1.getIn().setBody("abc"));
        assertEquals("abcxyz", exchange.getMessage().getBody());
    }

    @Test
    public void testClosureOneParam() throws InterruptedException {
        mockOutput.expectedBodiesReceived("bulb");
        producerTemplate.sendBody("direct:input1", "blub");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testClosureTwoParams() throws InterruptedException {
        mockOutput.expectedBodiesReceived("blah-blub");
        producerTemplate.sendBodyAndHeader("direct:input2", "blah", "foo",
                "blub");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testCustomInputExpression() throws InterruptedException {
        mockOutput.expectedBodiesReceived("knio");
        producerTemplate.sendBodyAndHeader("direct:input3", "blah", "foo",
                "oink");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testCustomInputClosure() throws InterruptedException {
        mockOutput.expectedBodiesReceived("knioknio");
        producerTemplate.sendBodyAndHeader("direct:input4", "blah", "foo",
                "oink");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testCustomParamsExpression() throws InterruptedException {
        mockOutput.expectedBodiesReceived("dcba");
        producerTemplate.sendBodyAndHeader("direct:input5", "blah", "foo",
                "abcd");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testCustomParamsClosure() throws InterruptedException {
        mockOutput.expectedBodiesReceived("dcba");
        producerTemplate.sendBodyAndHeader("direct:input6", "blah", "foo",
                "abcd");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testCustomParamsStatic() throws InterruptedException {
        mockOutput.expectedBodiesReceived("blahabc");
        producerTemplate.sendBody("direct:input7", "blah");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testTransmogrifierObject() throws InterruptedException {
        mockOutput.expectedBodiesReceived("b");
        producerTemplate.sendBody("direct:input8", "a");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testTransmogrifierBean() throws InterruptedException {
        mockOutput.expectedBodiesReceived("g");
        producerTemplate.sendBody("direct:input9a", "f");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testTransmogrifierBeanFromContextProperty()
            throws InterruptedException {
        mockOutput.expectedBodiesReceived("g");
        producerTemplate.sendBody("direct:input9b", "f");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testCustomInputOrdering() throws InterruptedException {
        mockOutput.expectedBodiesReceived("kniooink");
        producerTemplate.sendBodyAndHeader("direct:input10", "blah", "foo",
                "oink");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testXmlClosureBuilderParams() throws InterruptedException {
        mockOutput.expectedBodiesReceived("<result>very</result>");
        producerTemplate.sendBody("direct:input11", "very");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testXmlClosureHeadersAndBuilderParams()
            throws InterruptedException {
        mockOutput.expectedBodiesReceived("<result>magic</result>");
        producerTemplate
                .sendBodyAndHeader("direct:input12", "ma", "foo", "gic");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testXmlBeanBuilderParams() throws InterruptedException {
        mockOutput.expectedBodiesReceived("<result>bean</result>");
        producerTemplate.sendBody("direct:input13", "bean");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testXmlObjectBuilderParams() throws InterruptedException {
        mockOutput.expectedBodiesReceived("<result>object</result>");
        producerTemplate.sendBody("direct:input14", "object");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testDedicatedXsltTransmogrifier() throws InterruptedException,
            IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input15", xsltInput());
        assertXsltOutput("P", "T");
    }

    @Test
    public void testXsltTransmogrifier() throws InterruptedException,
            IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input16", xsltInput());
        assertXsltOutput("P", "T");
    }

    @Test
    public void testXsltTransmogrifierReturningInputStream()
            throws InterruptedException, IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input17", xsltInput());
        mockOutput.assertIsSatisfied();
        var result = (InputStream) mockOutput.getExchanges().get(0)
                .getIn().getBody();
        assertNotNull(result);
    }

    @Test
    public void testXsltTransmogrifierWithXsltParams()
            throws InterruptedException, IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input18", xsltInput());
        assertXsltOutput("D", "I");
    }

    @Test
    public void testXsltTransmogrifierWithStylesheetInParams()
            throws InterruptedException, IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input19", xsltInput());
        assertXsltOutput("D", "I");
    }

    @Test
    public void testXsltTransmogrifierWithStylesheetInHeader()
            throws InterruptedException, IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input20", xsltInput());
        assertXsltOutput("P", "T");
    }

    @Test
    public void testXsltTransmogrifierWithStylesheetAndParamsInHeaders()
            throws InterruptedException, IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input21", xsltInput());
        assertXsltOutput("D", "I");
    }

    @Test
    public void testSchematronTransmogrifier() throws InterruptedException,
            IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input23", content("/schematron/schematron-test.xml"));
        mockOutput.assertIsSatisfied();
        var result = (String)mockOutput.getExchanges().get(0).getIn().getBody();
        assertFalse(result.contains("svrl:failed-assert"));
    }
    
    @Test
    public void testInvalidSchematronTransmogrifier() throws InterruptedException,
            IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input23", content("/schematron/schematron-test-fail.xml"));
        mockOutput.assertIsSatisfied();
        var result = (String)mockOutput.getExchanges().get(0).getIn().getBody();
        assertTrue(result.contains("svrl:failed-assert"));
    }
    
    @Test
    public void testXqueryDedicatedTransmogrifier()
            throws InterruptedException,
            IOException, SAXException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input24", content("/xquery/labreport.xml"));
        assertXqueryOutput("someid");
    }

    @Test
    public void testXqueryTransmogrifier() throws InterruptedException,
            IOException, SAXException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input25", content("/xquery/labreport.xml"));
        assertXqueryOutput("headerId");
    }

    @Test
    public void testXqueryTransmogrifierReturningInputStream()
            throws InterruptedException, IOException, SAXException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input26", content("/xquery/labreport.xml"));
        mockOutput.assertIsSatisfied();
        var result = mockOutput.getExchanges().get(0).getIn()
                .getBody(InputStream.class);
        assertNotNull(result);
    }
    
    @Test
    public void testXqueryTransmogrifierReturningDOMResult()
            throws InterruptedException, IOException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input27", content("/xquery/labreport.xml"));
        mockOutput.assertIsSatisfied();
        var result = mockOutput.getExchanges().get(0).getIn()
                .getBody(DOMResult.class);
        assertNotNull(result);
    }

    @Test
    public void testXqueryTransmogrifierExternalRessource()
            throws InterruptedException, IOException, SAXException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input28", content("/xquery/labreport.xml"));
        assertXqueryOutput("mapid");
    }

    @Test
    public void testXqueryTransmogrifierExternalSourceParam()
            throws InterruptedException, IOException, SAXException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input29", content("/xquery/labreport.xml"));
        assertXqueryOutput("externalid");
    }

    @Test
    public void testXqueryWithHeaderParams()
            throws InterruptedException, IOException, SAXException {
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input30", content("/xquery/labreport.xml"));
        mockOutput.assertIsSatisfied();
    }

    
    private String xsltInput() throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream("/xslt/createPatient.xml"), Charset.defaultCharset());
    }

    private void assertXqueryOutput(String resultXml) throws InterruptedException {
        mockOutput.assertIsSatisfied();
        var result = mockOutput.getExchanges().get(0).getIn()
                .getBody(String.class);
        assertNotNull(result);
        assertTrue(result.contains("<item name=\"pid\">" + resultXml + "</item>"));
    }

    private void assertXsltOutput(String processingCode, String processingMode) throws InterruptedException {
        mockOutput.assertIsSatisfied();
        var result = (String)mockOutput.getExchanges().get(0).getIn().getBody();
        assertNotNull(result);
        assertTrue(result.contains("<processingCode code=\"" + processingCode + "\""));
        assertTrue(result.contains("<processingModeCode code=\"" + processingMode + "\""));
    }

    private String content(String path) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(path), Charset.defaultCharset());
    }

}
