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
package org.openehealth.ipf.platform.camel.core.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Validation of XML documents based on a XML Schema. Before using this class
 * consider using a validating Parser class.
 * 
 * @author Christian Ohr
 */
public class XsdValidator implements Validator<Source, String> {

    private static Log LOG = LogFactory.getLog(XsdValidator.class);
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    @Override
    public void validate(Source message, String schema) {
        List<ValidationException> exceptions = doValidate(message, schema);
        if (exceptions != null && exceptions.size() > 0) {
            throw new ValidationException(exceptions);
        }
    }

    /**
     * @param message
     *            the message to be validated
     * @param schemaSource
     *            the XML schema to validate against
     * @return an array of validation exceptions
     */
    protected List<ValidationException> doValidate(Source message,
            String schemaResource) {
        try {
            LOG.debug("Validating XML message");
            // SchemaFactory is neither thread-safe nor reentrant
            SchemaFactory factory = SchemaFactory
                    .newInstance(getSchemaLanguage());

            // Register resource resolver to resolve external XML schemas
            factory.setResourceResolver(new LSResourceResolverImpl());

            Schema schema = factory.newSchema(schemaSource(schemaResource));
            javax.xml.validation.Validator validator = schema.newValidator();
            CollectingErrorHandler errorHandler = new CollectingErrorHandler();
            validator.setErrorHandler(errorHandler);
            validator.validate(message);
            List<ValidationException> exceptions = errorHandler.getExceptions();
            if (exceptions.size() > 0) {
                LOG.debug("Message validation found " + exceptions.size()
                        + " problems");
            } else {
                LOG.debug("Message validation successful");
            }
            return exceptions;
        } catch (SAXException e) {
            return Arrays
                    .asList(new ValidationException[] { new ValidationException(
                            "Unexpected validation failure because "
                                    + e.getMessage(), e) });
        } catch (IOException e) {
            return Arrays
                    .asList(new ValidationException[] { new ValidationException(
                            "Unexpected validation failure because "
                                    + e.getMessage(), e) });
        }
    }

    public Source schemaSource(String resource) throws IOException {
        Resource r = resourceLoader.getResource(resource);
        if (r != null) {
            if (r.getURL() != null) {
                return new StreamSource(r.getInputStream(), r.getURL()
                        .toExternalForm());
            } else {
                return new StreamSource(r.getInputStream());
            }
        } else {
            throw new IllegalArgumentException("Schema not specified properly");
        }
    }

    public String getSchemaLanguage() {
        return schemaLanguage;
    }

    public void setSchemaLanguage(String schemaLanguage) {
        this.schemaLanguage = schemaLanguage;
    }

    /**
     * Error handler that collects {@link SAXParseException}s and provides them
     * wrapped in an {@link ValidationException} array.
     * 
     * @author Christian Ohr
     */
    class CollectingErrorHandler implements ErrorHandler {

        private List<SAXParseException> exceptions;

        public void error(SAXParseException exception) throws SAXException {
            if (exceptions == null) {
                exceptions = new ArrayList<SAXParseException>();
            }
            exceptions.add(exception);
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            if (exceptions == null) {
                exceptions = new ArrayList<SAXParseException>();
            }
            exceptions.add(exception);
        }

        public void warning(SAXParseException exception) throws SAXException {
            // TODO LOG some message
        }

        public List<ValidationException> getExceptions() {
            List<ValidationException> validationExceptions = new ArrayList<ValidationException>();
            if (exceptions != null) {
                for (SAXParseException exception : exceptions) {
                    validationExceptions
                            .add(new ValidationException(exception));
                }
            }
            return validationExceptions;

        }

        public void reset() {
            if (exceptions != null) {
                exceptions.clear();
            }
        }
    }
}