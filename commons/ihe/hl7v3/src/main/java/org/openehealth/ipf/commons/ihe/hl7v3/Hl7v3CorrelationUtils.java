/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3;

import java.io.ByteArrayInputStream;
import java.util.Collections;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.cxf.helpers.MapNamespaceContext;
import org.xml.sax.InputSource;

/**
 * Helper methods for async correlation support in HL7v3-based transactions.
 * @author Dmytro Rud
 */
public class Hl7v3CorrelationUtils {
    
    private static final XPathExpression REQUEST_MESSAGE_ID_EXPRESSION;
    private static final XPathExpression RESPONSE_MESSAGE_ID_EXPRESSION;
    
    static {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        xpath.setNamespaceContext(new MapNamespaceContext(
                Collections.singletonMap("hl7", Hl7v3Utils.HL7V3_NSURI)));
        
        try {
            REQUEST_MESSAGE_ID_EXPRESSION = xpath.compile(
                    idExpresison("/hl7:*[1]/hl7:id[1]"));
            RESPONSE_MESSAGE_ID_EXPRESSION = xpath.compile(
                    idExpresison("/hl7:*[1]/hl7:acknowledgement[1]/hl7:targetMessage[1]/hl7:id[1]"));
        } catch (XPathExpressionException e) {
            // should not occur
            throw new RuntimeException(e);
        }
    }
    

    /**
     * Constructs XPath expression for ID string generation.
     * <p>
     * Of internal use only.
     */
    private static final String idExpresison(String idXPath) {
        return new StringBuilder()
            .append("concat(")
            .append(idXPath)
            .append("/@extension, '@', ")
            .append(idXPath)
            .append("/@root)")
            .toString();
    }
    

    /**
     * Returns message ID of the given HL7 v3 message in the form "root@extension".
     */
    public static String getHl7v3MessageId(String message, boolean isRequest) {
        try {
            InputSource source = new InputSource(new ByteArrayInputStream(message.getBytes()));
            XPathExpression expression = 
                isRequest ? REQUEST_MESSAGE_ID_EXPRESSION : RESPONSE_MESSAGE_ID_EXPRESSION;
            synchronized (expression) {
                return expression.evaluate(source);
            }
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

}
