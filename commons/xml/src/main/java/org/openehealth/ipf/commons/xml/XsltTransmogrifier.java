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

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Xslt Processor transforming a {@link Source} into an object of type T. The stylesheet to be used is passed with the
 * {@link #zap(Source, Object...)} call, however, the Xslt Template object is cached for subsequent transformations
 * using this stylesheet.
 * <p>
 * Xslt parameters can be passed in as Map parameter.
 * <p>
 * Note: This class is thread-safe and reentrant.
 * 
 * @author Christian Ohr
 */
public class XsltTransmogrifier<T> extends AbstractCachingXmlProcessor<Templates> implements Transmogrifier<Source, T> {
    private static final Logger LOG = LoggerFactory.getLogger(XsltTransmogrifier.class);

    private static final ConcurrentMap<String, Loader<Templates>> XSLT_CACHE = new ConcurrentHashMap<>();

    @Getter @Setter private Map<String, Object> staticParams;
    private final TransformerFactory factory;
    private final URIResolver resolver;
    private final Class<T> outputFormat;

    @SuppressWarnings("unchecked")
    public XsltTransmogrifier() {
        this((Class<T>) String.class, null, null);
    }

    public XsltTransmogrifier(Class<T> outputFormat) {
        this(outputFormat, null, null);
    }

    public XsltTransmogrifier(Class<T> outputFormat, ClassLoader classLoader) {
        this(outputFormat, classLoader, null);
    }

    public XsltTransmogrifier(Class<T> outputFormat, Map<String, Object> staticParams) {
        this(outputFormat, null, staticParams);
    }

    public TransformerFactory getFactory() {
        return factory;
    }

    @Override
    protected ConcurrentMap<String, Loader<Templates>> getCache() {
        return XSLT_CACHE;
    }

    /**
     * @param outputFormat
     *            currently supported: String, Writer, OutputStream
     * @param classLoader
     *            class loader for resource retrieval, may be <code>null</code>
     * @param staticParams
     *            static Xslt parameters
     */
    public XsltTransmogrifier(
            Class<T> outputFormat,
            ClassLoader classLoader,
            Map<String, Object> staticParams)
    {
        super(classLoader);
        factory = TransformerFactory.newInstance();
        // Wrap the default resolver
        resolver = new ClasspathUriResolver(factory.getURIResolver());
        factory.setURIResolver(resolver);
        this.outputFormat = outputFormat;
        this.staticParams = staticParams;
    }

    /**
     * Converts a Source into a Result using a XSL processor.
     * <p>
     * The XSL stylesheet resource location is mandatory in the first extra parameter.
     * XSL Parameters may be passed as a Map in the second parameter.
     * 
     * @see Transmogrifier#zap(java.lang.Object, java.lang.Object[])
     */
    @Override
    public T zap(Source source, Object... params) {
        ResultHolder<T> accessor = ResultHolderFactory.create(outputFormat);
        if (accessor == null) throw new IllegalArgumentException("Format " + outputFormat + " is not supported");
        Result result = accessor.createResult();
        doZap(source, result, params);
        return accessor.getResult();
    }

    private void doZap(Source source, Result result, Object... params) {
        if (params.length == 0) {
            throw new IllegalArgumentException("Expected XSL location in first parameter");
        }
        try {
            Templates template = resource(params);
            Transformer transformer = template.newTransformer();
            transformer.setURIResolver(resolver);
            setXsltParameters(transformer, staticParams);
            setXsltParameters(transformer, resourceParameters(params));
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new RuntimeException("XSLT processing failed", e);
        }

    }

    /**
     * Sets the parameter to the {@link Transformer} object
     */
    protected void setXsltParameters(Transformer transformer, Map<String, Object> param) {
        if (param == null) {
            return;
        }
        for (Entry<String, Object> entry : param.entrySet()) {
            LOG.debug("Add new parameter for transformer: {}", entry.getKey());
            transformer.setParameter(entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected Templates createResource(Object... params) {
        try {
            return factory.newTemplates(resourceContent(params));
        } catch (TransformerConfigurationException e) {
            throw new IllegalArgumentException("Could not initialize XSLT template", e);
        }
    }

}
