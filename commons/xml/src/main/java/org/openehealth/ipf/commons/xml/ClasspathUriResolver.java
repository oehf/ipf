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

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

/**
 * URIResolver used to correctly resolve import commands in xslt or xquery content. The referenced resource (stylesheet
 * or document) can be found somewhere in the classloader's classpath. If it can't be found, the default URIResolver is
 * used.
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
     * Resolve by searching the classpath, fallback to default resolution strategy.
     * <p>
     * Avoid using {@link SAXSource} because Saxon's {@link net.sf.saxon.lib.EntityResolverWrappingResourceResolver}
     * doesn't support it.
     */
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        final var inputStream = getClass().getClassLoader().getResourceAsStream(href);
        if (inputStream != null) {
            return new StreamSource(inputStream);
        } else {
            final var source = standardResolver.resolve(href, base);
            if (source instanceof SAXSource) {
                return new StreamSource(((SAXSource) source).getInputSource().getByteStream());
            } else {
                return source;
            }
        }
    }

}
