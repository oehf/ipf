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

import static org.openehealth.ipf.commons.xml.ParametersHelper.parameters;
import static org.openehealth.ipf.commons.xml.ParametersHelper.resource;
import static org.openehealth.ipf.commons.xml.ParametersHelper.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;

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
public class XsltTransmogrifier<T> implements Transmogrifier<Source, T> {
    private final static Logger LOG = LoggerFactory.getLogger(XsltTransmogrifier.class);

    private final Map<Object, Templates> templateCache = new HashMap<Object, Templates>();

    private Map<String, Object> staticParams;

    private final TransformerFactory factory;
    private final URIResolver resolver;
    private final Class<T> outputFormat;

    @SuppressWarnings("unchecked")
    public XsltTransmogrifier() {
        this((Class<T>) String.class);
    }

    /**
     * @param outputFormat
     *            currently supported: String, Writer, OutputStream
     */
    public XsltTransmogrifier(Class<T> outputFormat) {
        super();
        factory = TransformerFactory.newInstance();
        // Wrap the default resolver
        resolver = new ClasspathUriResolver(factory.getURIResolver());
        factory.setURIResolver(resolver);
        this.outputFormat = outputFormat;
    }

    /**
     * @param outputFormat
     *            currently supported: String, Writer, OutputStream
     * @param staticParams
     *            static Xslt parameters
     */
    public XsltTransmogrifier(Class<T> outputFormat,
            Map<String, Object> staticParams) {
        this(outputFormat);
        this.staticParams = staticParams;
    }

    protected Map<Object, Templates> getTemplateCache() {
        return templateCache;
    }

    protected TransformerFactory getFactory() {
        return factory;
    }

    public Map<String, Object> getStaticParams() {
        return staticParams;
    }

    public void setStaticParams(Map<String, Object> staticParams) {
        this.staticParams = staticParams;
    }

    /**
     * 
     * Converts a Source into a Result using a XSL processor.
     * <p>
     * The XSL stylesheet resource location is mandatory in the first extra parameter. XSL Parameters may be passed as a
     * Map in the second parameter.
     * 
     * @see org.openehealth.ipf.commons.core.modules.api.Transmogrifier#zap(java.lang.Object, java.lang.Object[])
     */
    @Override
    public T zap(Source source, Object... params) {
        ResultHolder<T> accessor = ResultHolderFactory.create(outputFormat);
        Result result = accessor.createResult();
        doZap(source, result, params);
        return accessor.getResult();
    }

    private void doZap(Source source, Result result, Object... params) {
        if (params.length == 0) {
            throw new IllegalArgumentException(
                    "Expected XSL location in first parameter");
        }
        try {
            Templates template = template(params);
            Transformer transformer = template.newTransformer();
            transformer.setURIResolver(resolver);
            setXsltParameters(transformer, staticParams);
            setXsltParameters(transformer, parameters(params));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException("XSLT processing failed", e);
        }

    }

    /**
     * Sets the parameter to the {@link Transformer} object
     * 
     * @param transformer
     * @param param
     */
    protected void setXsltParameters(Transformer transformer,
            Map<String, Object> param) {
        if (param == null) {
            return;
        }
        for (Entry<?, ?> entry : ((Map<?, ?>) param).entrySet()) {
            LOG.debug("Add new parameter for transformer: {}", entry.getKey().toString());
            transformer.setParameter(entry.getKey().toString(), entry
                    .getValue());
        }
    }

    /**
     * Instantiates a {@link Templates} object according to the parameters. The template is stored in a cache for faster
     * access in subsequent calls.
     * 
     * @param params
     *            params[0] must be of type String and reference the stylesheet resource
     * @return a {@link Templates} object according to the parameters
     * @TODO use an external cache implementation?
     */
    synchronized protected Templates template(Object... params) {
        if (!(templateCache.containsKey(resource(params)))) {
            templateCache.put(resource(params), doCreateTemplate(params));
        }
        return templateCache.get(resource(params));
    }

    /**
     * Creates the Xslt template
     * 
     * @param params
     * @return the Xslt template
     */
    protected Templates doCreateTemplate(Object... params) {
        String resourceLocation = resource(params);
        LOG.debug("Create new template for {}", resourceLocation);
        try {
            return factory.newTemplates(source(resourceLocation));
        } catch (Exception e) {
            throw new IllegalArgumentException("The resource "
                    + resourceLocation + " is not valid", e);
        }
    }

}
