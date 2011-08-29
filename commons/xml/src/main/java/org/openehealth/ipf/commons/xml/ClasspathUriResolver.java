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
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;

/**
 * URIResolver used to correctly resolve import commands in xslt or xquery
 * content. The referenced resource (stylesheet or document) can be found
 * somewhere in the classloader's classpath. If it can't be found, the default
 * URIResolver is used.
 * 
 * @author Christian Ohr
 */
class ClasspathUriResolver implements URIResolver {

    private final URIResolver standardResolver;
    
    public ClasspathUriResolver(URIResolver resolver) {
        super();
        standardResolver = resolver;
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
