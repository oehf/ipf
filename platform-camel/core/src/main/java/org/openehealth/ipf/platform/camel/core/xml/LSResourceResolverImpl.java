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
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * Resolve external resources, such as external XML Schema, by looking in the
 * classpath. This enables to e.g. split a XML schema into several files that
 * depend on each other.
 * 
 * @author Christian Ohr
 */
public class LSResourceResolverImpl implements LSResourceResolver {

    private static Log LOG = LogFactory.getLog(LSResourceResolverImpl.class);

    /**
     * @see org.w3c.dom.ls.LSResourceResolver#resolveResource(String, String,
     *      String, String, String)
     */
    public LSInput resolveResource(String type, String namespaceURI,
            String publicId, String systemId, String baseURI) {
        ClassPathResource resource = new ClassPathResource(systemId);
        LSInput lsInput = null;
        try {
            InputStream is = resource.getInputStream();
            lsInput = new LSInputImpl(is);
        } catch (IOException e) {
            LOG.warn("Referenced external file " + systemId
                    + " could not be found");
        }
        return lsInput;
    }
}