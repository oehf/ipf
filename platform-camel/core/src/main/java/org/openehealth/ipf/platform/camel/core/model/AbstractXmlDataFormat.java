/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.model;

import groovy.xml.FactorySupport;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.support.service.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.SAXParser;

/**
 * @author Christian Ohr
 */
public abstract class AbstractXmlDataFormat extends ServiceSupport implements DataFormat {

    private static final Logger log = LoggerFactory.getLogger(AbstractXmlDataFormat.class);
    private static final ErrorHandler DEFAULT_HANDLER = new DefaultErrorHandler();

    private boolean namespaceAware;
    private boolean keepWhitespace;
    private final ErrorHandler errorHandler = DEFAULT_HANDLER;

    public AbstractXmlDataFormat(boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }

    protected SAXParser newSaxParser() throws Exception {
        var factory = FactorySupport.createSaxParserFactory();
        factory.setNamespaceAware(namespaceAware);
        factory.setValidating(false);
        return factory.newSAXParser();
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public boolean isNamespaceAware() {
        return namespaceAware;
    }

    public void setNamespaceAware(boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }

    public boolean isKeepWhitespace() {
        return keepWhitespace;
    }

    public void setKeepWhitespace(boolean keepWhitespace) {
        this.keepWhitespace = keepWhitespace;
    }

    @Override
    protected void doStart() {
    }

    @Override
    protected void doStop() {
    }

    private static class DefaultErrorHandler implements ErrorHandler {

        @Override
        public void warning(SAXParseException exception) {
            log.warn("Warning occurred during parsing", exception);
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            throw new SAXException(exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw new SAXException(exception);
        }

    }
}
