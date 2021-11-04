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

import groovy.xml.XmlSlurper;
import org.apache.camel.Exchange;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Christian Ohr
 */
public class XmlSlurperDataFormat extends AbstractXmlDataFormat {

    public XmlSlurperDataFormat() {
        this(true);
    }

    public XmlSlurperDataFormat(boolean namespaceAware) {
        super(namespaceAware);
    }

    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream) {
        throw new UnsupportedOperationException("XmlSlurper does not support marshalling");
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        var slurper = new XmlSlurper(newSaxParser());
        slurper.setErrorHandler(getErrorHandler());
        slurper.setKeepIgnorableWhitespace(isKeepWhitespace());
        return slurper.parse(stream);
    }


}
