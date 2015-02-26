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

import groovy.util.XmlSlurper;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Krasser
 * @author Christian Ohr
 */
public class GpathDataFormat extends AbstractXmlDataFormat {

    private final static Logger LOG = LoggerFactory.getLogger(GnodeDataFormat.class);

    public GpathDataFormat() {
        super();
    }

    public GpathDataFormat(boolean namespaceAware) {
        super(namespaceAware);
    }

    public GpathDataFormat(String schemaResource) {
        super(schemaResource);
    }

    public GpathDataFormat(String schemaResource, boolean namespaceAware) {
        super(schemaResource, namespaceAware);
    }

    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream)
            throws Exception {
        throw new UnsupportedOperationException("marshalling not supported");
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream)
            throws Exception {
        return slurper().parse(stream);
    }

    private XmlSlurper slurper() throws Exception {
        XmlSlurper slurper = new XmlSlurper(saxParser());
        slurper.setErrorHandler(new SimpleSaxErrorHandler(LOG));
        return slurper;
    }


}
