/*
 * Copyright 2008 the original author or authors.
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

import groovy.util.Node;
import groovy.util.XmlNodePrinter;
import groovy.util.XmlParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.xml.SimpleSaxErrorHandler;

/**
 * @author Martin Krasser
 * @author Christian Ohr
 */
public class GnodeDataFormat extends AbstractXmlDataFormat {

    private static Log LOG = LogFactory.getLog(GnodeDataFormat.class);

    private boolean namespaceAware;

    public GnodeDataFormat() {
        super();
    }

    public GnodeDataFormat(boolean namespaceAware) {
        super(namespaceAware);
    }

    public GnodeDataFormat(String schemaResource) {
        super(schemaResource);
    }

    public GnodeDataFormat(String schemaResource, boolean namespaceAware) {
        super(schemaResource, namespaceAware);
    }

    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream)
            throws Exception {
        printer(stream).print((Node) graph);
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream)
            throws Exception {
        return parser().parse(stream);
    }

    private XmlParser parser() throws Exception {
        XmlParser xmlParser = new XmlParser(saxParser());
        xmlParser.setErrorHandler(new SimpleSaxErrorHandler(LOG));
        return xmlParser;
    }

    private XmlNodePrinter printer(OutputStream stream) throws Exception {
        XmlNodePrinter xmlNodePrinter = new XmlNodePrinter(new PrintWriter(
                stream));
        xmlNodePrinter.setNamespaceAware(namespaceAware);
        return xmlNodePrinter;
    }

}
