/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Simple auto-converter
 * 
 * @author Stefan Ivanov
 * 
 */
public class DocumentContentHelper {
    private static final Log LOG = LogFactory.getLog(DocumentContentHelper.class.getName());
    
    @SuppressWarnings("unchecked")
    public static <T> T convert(Map<Class<?>, Object> content, Class<T> clazz) {
        if (String.class == clazz) {
            return (T) convertToString(content);
        } else if (org.w3c.dom.Document.class == clazz) {
            return (T) convertToDocument(content);
        } else {
            return null;
        }
    }
    
    public static String convertToString(Map<Class<?>, Object> content) {
        String result = null;
        DataHandler data = (DataHandler) content.get(DataHandler.class);
        try {
            result = IOUtils.toString(data.getInputStream());
            LOG.trace("Converted data to string: " + data + " -> " + result);
        } catch (IOException ex) {
            LOG.error("Coldn't complete conversion: " + ex.getMessage());
        }
        return result;
    }
    
    /**
     * Parses the content to DOM Document if possible. Otherwise returns 'null'
     * 
     * @param content
     * @return
     */
    public static org.w3c.dom.Document convertToDocument(Map<Class<?>, Object> content) {
        Document result = null;
        try {
            if (!content.containsKey(String.class))
                convertToString(content);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            result = builder.parse(new ByteArrayInputStream(((String) content
                .get(String.class)).getBytes()));
        } catch (IOException ex) {
            LOG.error("Coldn't complete conversion: " + ex.getMessage());
        } catch (ParserConfigurationException ex) {
            LOG.error("Parsing failed conversion: " + ex.getMessage());
        } catch (SAXException ex) {
            LOG.error("SAX Parse exception: " + ex.getMessage());
        }
        return result;
    }

}
