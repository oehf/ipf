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

import static junit.framework.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;


public class TestSoapUtils {
    
    @Test
    public void testExctractSoapBody() {
        final String contents = 
            "<ns2:ImportantQuestion>" +
            "Camel rules as cigarettes as well.  Do Apache Indians smoke it?" +
            "</ns2:ImportantQuestion>";

        final String envelopeWithNamespacePrefixes = 
            "<soap:Envelope><soap:Header>" +
            "    <thirdns:header value=\"12345\">some text</thirdns:header></soap:Header>" +
            "    <soap:Body>" + 
            contents + 
            "</soap:Body></soap:Envelope>";

        final String envelopeWithoutNamespacePrefixes = 
            "<Envelope><Header>" +
            "    <thirdns:header value=\"12345\">some text</thirdns:header></Header>" +
            "    <Body>" + 
            contents + 
            "</Body></Envelope>";

        final String emptyEnvelopeWithNamespacePrefixes = 
            "<soap:Envelope><soap:Header>" +
            "    <thirdns:header value=\"12345\">some text</thirdns:header></soap:Header>" +
            "    <soap:Body></soap:Body></soap:Envelope>";

        final String emptyEnvelopeWithoutNamespacePrefixes = 
            "<Envelope><Header>" +
            "    <thirdns:header value=\"12345\">some text</thirdns:header></Header>" +
            "    <Body></Body></Envelope>";

        final String emptyEnvelopeWithNamespacePrefixesShort = 
            "<soap:Envelope><soap:Header>" +
            "    <thirdns:header value=\"12345\">some text</thirdns:header></soap:Header>" +
            "    <soap:Body/></soap:Envelope>";

        final String emptyEnvelopeWithoutNamespacePrefixesShort = 
            "<Envelope><Header>" +
            "    <thirdns:header value=\"12345\">some text</thirdns:header></Header>" +
            "    <Body /></Envelope>";

        final String totallyBad = "12345";
        
        assertEquals(contents, SoapUtils.extractSoapBody(envelopeWithNamespacePrefixes));
        assertEquals(contents, SoapUtils.extractSoapBody(envelopeWithoutNamespacePrefixes));

        assertEquals("", SoapUtils.extractSoapBody(emptyEnvelopeWithNamespacePrefixes));
        assertEquals("", SoapUtils.extractSoapBody(emptyEnvelopeWithoutNamespacePrefixes));
        assertEquals("", SoapUtils.extractSoapBody(emptyEnvelopeWithNamespacePrefixesShort));
        assertEquals("", SoapUtils.extractSoapBody(emptyEnvelopeWithoutNamespacePrefixesShort));
        
        Assert.assertNull(SoapUtils.extractSoapBody(null));
        Assert.assertEquals(totallyBad, SoapUtils.extractSoapBody(totallyBad));
    }

}
