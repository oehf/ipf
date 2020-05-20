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
package org.openehealth.ipf.commons.ihe.ws.cxf.payload;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * @author Dmytro Rud
 */
public class InNamespaceMergeInterceptorTest {

    private static DocumentBuilder builder;    
    
    private static final String SOAP_STRING =
        "<soap:Envelope " +
                "xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" " + 
                "xmlns:urn=\"urn:hl7-org:v3\" " +
                "myattribute=\"12345\" " +
                "xmlns:ns3=\"urn:ihe\" " +
                "xmlns:xmlnsqq=\"urn:dummy:xmlnsns\" " +
                "xmlnsqq:kpss=\"abcd\"> " +
            "<soap:Header/> " +
            "<soap:Body xmlns:ns3=\"body-ns3\" xmlns:internal=\"internal\">garbage</soap:Body> " + 
        "</soap:Envelope>";
    
    private static Document SOURCE;

    
    @BeforeClass
    public static void setUpClass() throws Exception {
        var factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();
        SOURCE = parse(SOAP_STRING);
    }
    
    /**
     * Parses string into DOM document.
     */
    private static Document parse(String document) throws Exception {
        InputStream stream = new ByteArrayInputStream(document.getBytes());
        return builder.parse(stream);
    }

    /**
     * Merges prefixes of the globally defined source document with the ones
     * declared in the given string XML payload.  
     * Returns the prefix-to-uri map. 
     */
    private static Map<String, String> merge(String payload) throws Exception {
        var target = InNamespaceMergeInterceptor.enrichNamespaces(SOURCE, payload);
        var element = parse(target).getDocumentElement();
        Map<String, String> result = new HashMap<>();
        var attributes = element.getAttributes();
        for (var i = 0; i < attributes.getLength(); ++i) {
            var attribute = attributes.item(i);
            if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attribute.getNamespaceURI())) {
                result.put(attribute.getLocalName(), attribute.getTextContent());
            }
        }
        return result;
    }
    
    
    @Test
    public void testMergeNamespaces() throws Exception {
        Map<String, String> prefixes;
        
        // empty target element, short form  
        prefixes = merge("<element/>");
        assertTrue(prefixes.size() == 5);

        // target element without NS declarations
        prefixes = merge("<element>garbage</element>");
        assertTrue(prefixes.size() == 5);
        
        // target element with redeclared "soap" prefix
        prefixes = merge("<element xmlns:soap=\"12345\">garbage</element>");
        assertTrue(prefixes.size() == 5);
        assertEquals("12345", prefixes.get("soap"));

        // target element with declared prefix that begins with "xmlns"
        prefixes = merge("<element xmlns:xmlns1=\"uri\" xmlns1:soap=\"12345\">garbage</element>");
        assertTrue(prefixes.size() == 6);
        assertEquals("http://www.w3.org/2003/05/soap-envelope", prefixes.get("soap"));

        // target element with redeclared prefix "internal" which has been  
        // initially declared in the SOAP Body
        prefixes = merge("<element xmlns:internal=\"uri\">garbage</element>");
        assertTrue(prefixes.size() == 5);
        assertEquals("uri", prefixes.get("internal"));
        
        // declaration from SOAP Body have higher priority than the ones from SOAP Envelope
        assertEquals("body-ns3", prefixes.get("ns3"));
    }
}
