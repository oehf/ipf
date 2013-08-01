/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils.extractNonEmptyElement;
import static org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils.extractSoapBody;

public class TestSoapUtils {
    
    private final String contents =
        "<ns2:ImportantQuestion>" +
        "Camel rules as cigarettes as well.  Do Apache Indians smoke it?" +
        "</ns2:ImportantQuestion>";

    private final String envelopeWithNamespacePrefixes =
        "<soap:Envelope><soap:Header>" +
        "    <thirdns:header value=\"12345\">some text</thirdns:header></soap:Header>" +
        "    <soap:Body>" +
        contents +
        "</soap:Body></soap:Envelope>";

    private final String envelopeWithoutNamespacePrefixes =
        "<Envelope><Header>" +
        "    <thirdns:header value=\"12345\">some text</thirdns:header></Header>" +
        "    <Body>" +
        contents +
        "</Body></Envelope>";

    private final String emptyEnvelopeWithNamespacePrefixes =
        "<soap:Envelope><soap:Header>" +
        "    <thirdns:header value=\"12345\">some text</thirdns:header></soap:Header>" +
        "    <soap:Body xmlns:prefix=\"uri\"></soap:Body></soap:Envelope>";

    private final String emptyEnvelopeWithoutNamespacePrefixes =
        "<Envelope><Header>" +
        "    <thirdns:header value=\"12345\">some text</thirdns:header></Header>" +
        "    <Body></Body></Envelope>";

    private final String emptyEnvelopeWithNamespacePrefixesShort =
        "<soap:Envelope><soap:Header>" +
        "    <thirdns:header value=\"12345\">some text</thirdns:header></soap:Header>" +
        "    <soap:Body attrib=\"value\" /></soap:Envelope>";

    private final String emptyEnvelopeWithoutNamespacePrefixesShort =
        "<Envelope><Header>" +
        "    <thirdns:header value=\"12345\">some text</thirdns:header></Header>" +
        "    <Body /></Envelope>";

    private final String totallyBad = "12345";


    @Test
    public void testExctractSoapBody() {
        assertEquals(contents, extractSoapBody(envelopeWithNamespacePrefixes));
        assertEquals(contents, extractSoapBody(envelopeWithoutNamespacePrefixes));

        assertEquals("", extractSoapBody(emptyEnvelopeWithNamespacePrefixes));
        assertEquals("", extractSoapBody(emptyEnvelopeWithoutNamespacePrefixes));
        assertEquals("", extractSoapBody(emptyEnvelopeWithNamespacePrefixesShort));
        assertEquals("", extractSoapBody(emptyEnvelopeWithoutNamespacePrefixesShort));
        
        assertNull(extractSoapBody(null));
        assertEquals(totallyBad, extractSoapBody(totallyBad));
    }


    @Test
    public void testExtractNamedElement() {
        final String elementName = "ImportantQuestion";
        
        assertEquals(contents, extractNonEmptyElement(envelopeWithNamespacePrefixes, elementName));
        assertEquals(contents, extractNonEmptyElement(envelopeWithoutNamespacePrefixes, elementName));

        assertEquals(null, extractNonEmptyElement(emptyEnvelopeWithNamespacePrefixes, elementName));
        assertEquals(null, extractNonEmptyElement(emptyEnvelopeWithoutNamespacePrefixes, elementName));
        assertEquals(null, extractNonEmptyElement(emptyEnvelopeWithNamespacePrefixesShort, elementName));
        assertEquals(null, extractNonEmptyElement(emptyEnvelopeWithoutNamespacePrefixesShort, elementName));

        assertNull(extractNonEmptyElement(null, elementName));
        assertNull(extractNonEmptyElement(totallyBad, elementName));
    }
}
