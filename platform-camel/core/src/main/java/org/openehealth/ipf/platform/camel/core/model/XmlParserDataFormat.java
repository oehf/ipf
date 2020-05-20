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

import groovy.util.Node;
import groovy.xml.XmlNodePrinter;
import groovy.xml.XmlParser;
import org.apache.camel.Exchange;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Christian Ohr
 */
public class XmlParserDataFormat extends AbstractXmlDataFormat {

    public XmlParserDataFormat() {
        this(true);
    }

    public XmlParserDataFormat(boolean namespaceAware) {
        super(namespaceAware);
    }

    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream) {
        newPrinter(stream).print((Node) graph);
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        var xmlParser = new XmlParser(newSaxParser());
        xmlParser.setErrorHandler(getErrorHandler());
        xmlParser.setTrimWhitespace(!isKeepWhitespace());
        return xmlParser.parse(stream);
    }

    private XmlNodePrinter newPrinter(OutputStream stream) {
        var xmlNodePrinter = new XmlNodePrinter(new PrintWriter(stream));
        xmlNodePrinter.setNamespaceAware(isNamespaceAware());
        return xmlNodePrinter;
    }

}
