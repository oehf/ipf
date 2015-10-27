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
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * Resolve external resources, such as external XML Schema, by looking in the
 * classpath. This enables to e.g. split a XML schema into several files that
 * depend on each other.
 * <p>
 * The resolver is NOT capable of resolving relative resources, e.g.
 * ../../schema/subschema.xsd
 * 
 * @author Christian Ohr
 */
public class LSResourceResolverImpl implements LSResourceResolver {

    private final static Logger LOG = LoggerFactory.getLogger(LSResourceResolverImpl.class);

    /**
     * @see org.w3c.dom.ls.LSResourceResolver#resolveResource(String, String,
     *      String, String, String)
     */
    @Override
    public LSInput resolveResource(String type, String namespaceURI,
            String publicId, String systemId, String baseURI) {


        LSInput lsInput = null;
        try {
            URL resource = new URL(systemId);
            InputStream is = resource.openStream();
            lsInput = new LSInputImpl(is);
        } catch (IOException e) {
            LOG.debug("Referenced external file {} could not be found. Falling back to "
                    + "default resolution.", systemId);
        }
        return lsInput;
    }
}
