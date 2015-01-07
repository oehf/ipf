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
package org.openehealth.ipf.commons.xml;

import java.io.IOException;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Validation of XML documents based on a XML Schema. Before using this class
 * consider using a validating Parser class.
 * 
 * @author Christian Ohr
 */
public class XsdValidator extends AbstractCachingXmlProcessor<Schema> implements Validator<Source, String> {
    private static final Logger LOG = LoggerFactory.getLogger(XsdValidator.class);

    private static final Map<String, Schema> XSD_CACHE = new HashMap<>();
    private static final LSResourceResolverImpl RESOURCE_RESOLVER = new LSResourceResolverImpl();

    @Getter @Setter private String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    public XsdValidator() {
        super(null);
    }

    public XsdValidator(ClassLoader classloader) {
        super(classloader);
    }

    @Override
    protected Map<String, Schema> getCache() {
        return XSD_CACHE;
    }

    @Override
    public void validate(Source message, String schema) {
        List<ValidationException> exceptions = doValidate(message, schema);
        if (! exceptions.isEmpty()) {
            throw new ValidationException(exceptions);
        }
    }

    /**
     * @param message
     *            the message to be validated
     * @param schemaResource
     *            the XML schema to validate against
     * @return an array of validation exceptions
     */
    protected List<ValidationException> doValidate(Source message, String schemaResource) {
        try {
            LOG.debug("Validating XML message");
            Schema schema = resource(schemaResource);
            javax.xml.validation.Validator validator = schema.newValidator();
            CollectingErrorHandler errorHandler = new CollectingErrorHandler();
            validator.setErrorHandler(errorHandler);
            validator.validate(message);
            List<ValidationException> exceptions = errorHandler.getExceptions();
            if (! exceptions.isEmpty()) {
                LOG.debug("Message validation found {} problems", exceptions.size());
            } else {
                LOG.debug("Message validation successful");
            }
            return exceptions;
        } catch (Exception e) {
            return Collections.singletonList(new ValidationException(
                    "Unexpected validation failure because " + e.getMessage(), e));
        }
    }

    @Override
    protected Schema createResource(Object... params) throws IOException, SAXException {
        // SchemaFactory is neither thread-safe nor reentrant
        SchemaFactory factory = SchemaFactory.newInstance(getSchemaLanguage());

        // Register resource resolver to resolve external XML schemas
        factory.setResourceResolver(RESOURCE_RESOLVER);
        return factory.newSchema(resourceContent(params));
    }


    /**
     * Error handler that collects {@link SAXParseException}s and provides them
     * wrapped in an {@link ValidationException} array.
     * 
     * @author Christian Ohr
     */
    private static class CollectingErrorHandler implements ErrorHandler {

        private final List<ValidationException> exceptions = new ArrayList<>();

        @Override
        public void error(SAXParseException exception) throws SAXException {
            add(exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            add(exception);
        }

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            // TODO LOG some message
        }

        private void add(SAXParseException exception) {
            exceptions.add(new ValidationException(exception));
        }

        public List<ValidationException> getExceptions() {
            return exceptions;
        }
    }
}
