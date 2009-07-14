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
package org.openehealth.ipf.platform.camel.core.dataformat;

import groovy.xml.FactorySupport;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.camel.spi.DataFormat;
import org.openehealth.ipf.commons.xml.LSResourceResolverImpl;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.xml.sax.SAXException;

/**
 * Common attributes and methods for XmlParser and XmlSlurper usage
 * in IPF routes.
 * 
 * @author Christian Ohr
 */
public abstract class AbstractXmlDataFormat implements DataFormat {

    private boolean namespaceAware;
    private String schemaResource;
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    
    public AbstractXmlDataFormat() {
        this(true);
    }

    public AbstractXmlDataFormat(boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }

    public AbstractXmlDataFormat(String schemaResource) {
        this.schemaResource = schemaResource;
    }

    public AbstractXmlDataFormat(String schemaResource, boolean namespaceAware) {
        this.schemaResource = schemaResource;
        this.namespaceAware = namespaceAware;
    }


    protected SAXParser saxParser() throws Exception {
        SAXParserFactory factory = FactorySupport.createSaxParserFactory();
        factory.setNamespaceAware(namespaceAware);
        factory.setValidating(false);
        factory.setSchema(schema());
        return factory.newSAXParser();
    }

    private Schema schema() throws IOException, SAXException {
        Source source = null;
        if (schemaResource != null) {
            Resource r = resourceLoader.getResource(schemaResource);
            if (r != null) {
                if (r.getURL() != null) {
                    source = new StreamSource(r.getInputStream(), r.getURL()
                            .toExternalForm());
                } else {
                    source = new StreamSource(r.getInputStream());
                }
                SchemaFactory factory = SchemaFactory
                        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                factory.setResourceResolver(new LSResourceResolverImpl());
                return factory.newSchema(source);
            } else {
                throw new IllegalArgumentException(
                        "Schema not specified properly");
            }
        } else {
            return null;
        }
    }

}
