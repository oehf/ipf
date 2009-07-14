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

import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

/**
 * URIResolver used to correctly resolve xsl:import commands if the imported
 * stylesheet can be found somewhere in the classloader's classpath. If it can't
 * be found, the default URIResolver is used.
 * 
 * @author Christian Ohr
 */
class ClasspathUriResolver implements URIResolver {

    private URIResolver standardResolver;
    
    private static Log LOG = LogFactory.getLog(ClasspathUriResolver.class);
    

    public ClasspathUriResolver(TransformerFactory factory) {
        super();
        standardResolver = factory.getURIResolver();
    }

    /**
     * Resolve by searching the classpath, fallback to default resolution
     * strategy.
     * 
     */
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        ClassLoader cl = getClass().getClassLoader();
        URL url = cl.getResource(href);
        if (url != null) {
            SAXSource saxSource = new SAXSource();
            saxSource.setInputSource(new InputSource(url.toString()));
            saxSource.setSystemId(url.toString());
            return saxSource;
        } else {
            return standardResolver.resolve(href, base);
        }
    }

}
