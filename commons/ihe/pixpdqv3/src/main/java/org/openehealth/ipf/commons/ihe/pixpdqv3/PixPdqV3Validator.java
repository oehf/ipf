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
package org.openehealth.ipf.commons.ihe.pixpdqv3;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang.Validate;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Validator for HL7 v3 messages.
 * @author Dmytro Rud
 */
public class PixPdqV3Validator implements Validator<String, Collection<String>> {

    private static final Pattern ROOT_ELEMENT_PATTERN = Pattern.compile(
        "(?:\\s*<\\!--.*?-->)*"                             +  // optional comments before prolog (are they allowed?)
        "(?:\\s*<\\?xml.+?\\?>(?:\\s*<\\!--.*?-->)*)?"      +  // optional prolog and comments after it 
        "\\s*<(?:[\\w\\.-]+?:)?([\\w\\.-]+)(?:\\s|(?:/?>))"    // open tag of the root element
    );

    private static final SchemaFactory schemaFactory = 
        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    
    private static final Map<String, Schema> schemas = new HashMap<String, Schema>();

    private static final String XSD_PATH = "schema/HL7V3/NE2008/multicacheschemas/";
    
    
    /**
     * Returns path to XML Schema document which contains the definition
     * of the XML element with the given name. 
     */
    private static String getResourceForElement(String elementName) {
        StringBuilder sb = new StringBuilder(XSD_PATH);
        int pos1 = elementName.indexOf('_');
        int pos2 = elementName.indexOf('_', pos1 + 1);
        sb.append((pos2 > 0) ? elementName.substring(0, pos2) : elementName);
        return sb.append(".xsd").toString();
    }
    
    
    /**
     * Returns schema instance for the given root XML element name, 
     * creates it when necessary. 
     * @param rootElementName
     *      name of the root XML element.
     * @return
     *      XML schema instance.
     * @throws SAXException
     */
    synchronized private static Schema getSchema(String rootElementName) throws Exception {
        Schema schema = schemas.get(rootElementName);
        if(schema == null) {
            String resourceName = getResourceForElement(rootElementName);
            URL resource = PixPdqV3Validator.class.getClassLoader().getResource(resourceName);
            if(resource == null) {
                throw new RuntimeException("Cannot load resource '" + resourceName + "'");
            }
            schema = schemaFactory.newSchema(resource);
            schemas.put(rootElementName, schema);
        }
        return schema;
    }

    
    /**
     * Validates the given HL7 v3 message.
     */
    public void validate(String message, Collection<String> validRootElementNames) throws ValidationException {
        Validate.notNull(message, "message");
        Validate.notEmpty(validRootElementNames, "list of valid root element names");

        // search for the root element (which corresponds to the schema name)
        Matcher matcher = ROOT_ELEMENT_PATTERN.matcher(message);
        if(( ! matcher.find()) || (matcher.start() != 0)) {
            throw new ValidationException("Cannot extract root element, probably bad XML");
        }

        String rootElementName = matcher.group(1);
        if( ! validRootElementNames.contains(rootElementName)) {
            throw new ValidationException("Invalid root element '" + rootElementName + "'");
        }

        // validate against XML Schema
        List<SAXParseException> exceptions;
        try {
            Source source = new StreamSource(new ByteArrayInputStream(message.getBytes()));
            javax.xml.validation.Validator validator = getSchema(rootElementName).newValidator();
            ErrorCollector errorCollector = new ErrorCollector();
            validator.setErrorHandler(errorCollector);
            validator.validate(source);
            exceptions = errorCollector.getExceptions();
        } catch (Exception e) {
            throw new ValidationException(e);
        }
        
        if(exceptions.size() > 0) {
            throw new ValidationException(exceptions);
        }
    }
    

    /**
     * Collector of validation errors.
     */
    private class ErrorCollector implements ErrorHandler {
        private final List<SAXParseException> exceptions = new ArrayList<SAXParseException>(); 
        
        public List<SAXParseException> getExceptions() {
            return exceptions;
        }
        
        public void error(SAXParseException exception) throws SAXException {
            exceptions.add(exception);
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            exceptions.add(exception);
        }

        public void warning(SAXParseException exception) throws SAXException {
            // nop
        }
        
    }
}
