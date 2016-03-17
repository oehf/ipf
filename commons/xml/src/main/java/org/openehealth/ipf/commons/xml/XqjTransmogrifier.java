/*
 * Copyright 2011 the original author or authors.
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

import com.saxonica.xqj.SaxonXQConnection;
import com.saxonica.xqj.SaxonXQDataSource;
import lombok.Getter;
import lombok.Setter;
import net.sf.saxon.Configuration;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.xquery.*;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * XQuery transformer similar to the XsltTransmogrifier
 * 
 * @author Stefan Ivanov
 * 
 * @param <T>
 *     output type
 */
public class XqjTransmogrifier<T> extends AbstractCachingXmlProcessor<XQPreparedExpression> implements Transmogrifier<Source, T> {
    private final static Logger LOG = LoggerFactory.getLogger(XqjTransmogrifier.class);

    private static final ConcurrentMap<String, Loader<XQPreparedExpression>> XQUERY_CACHE = new ConcurrentHashMap<>();
    private static final Configuration XQUERY_GLOBAL_CONFIG;
    private static final SaxonXQDataSource DATA_SOURCE;
    static {
        XQUERY_GLOBAL_CONFIG = new Configuration();
        XQUERY_GLOBAL_CONFIG.setURIResolver(new ClasspathUriResolver(XQUERY_GLOBAL_CONFIG.getURIResolver()));
        DATA_SOURCE = new SaxonXQDataSource(XQUERY_GLOBAL_CONFIG);
    }

    private final Class<T> outputFormat;
    private SaxonXQConnection connection;

    @Getter @Setter private Map<String, Object> staticParams;


    @SuppressWarnings("unchecked")
    public XqjTransmogrifier() {
        this((Class<T>) String.class);
    }

    /**
     * @param outputFormat
     *            currently supported: String, Writer, OutputStream
     */
    public XqjTransmogrifier(Class<T> outputFormat) {
        this(outputFormat, null, null);
    }

    /**
     * @param outputFormat
     *            currently supported: String, Writer, OutputStream
     * @param classLoader
     *            class loader for resource retrieval, may be <code>null</code>
     */
    public XqjTransmogrifier(Class<T> outputFormat, ClassLoader classLoader) {
        this(outputFormat, classLoader, null);
    }

    /**
     * @param outputFormat
     *            currently supported: String, Writer, OutputStream
     * @param globalParams
     *            static XQuery parameters
     */
    public XqjTransmogrifier(Class<T> outputFormat, Map<String, Object> globalParams) {
        this(outputFormat, null, globalParams);
    }

    /**
     * @param outputFormat
     *            currently supported: String, Writer, OutputStream
     * @param classLoader
     *            class loader for resource retrieval, may be <code>null</code>
     * @param globalParams
     *            static XQuery parameters
     */
    public XqjTransmogrifier(
            Class<T> outputFormat,
            ClassLoader classLoader,
            Map<String, Object> globalParams)
    {
        super(classLoader);
        this.outputFormat = outputFormat;

        if (globalParams != null) {
            for (Entry<String, Object> entry : globalParams.entrySet()) {
                XQUERY_GLOBAL_CONFIG.setConfigurationProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    protected ConcurrentMap<String, Loader<XQPreparedExpression>> getCache() {
        return XQUERY_CACHE;
    }

    /**
     * Converts a Source into a typed result using a XQuery processor.
     * <p>
     * The XQ resource location is mandatory in the first extra parameter. Other
     * parameters may be passed as a Map in the second parameter.
     * 
     * @see Transmogrifier#zap(java.lang.Object, java.lang.Object[])
     */
    @Override
    public T zap(Source source, Object... params) {
        ResultHolder<T> accessor = ResultHolderFactory.create(outputFormat);
        if (accessor == null) throw new IllegalArgumentException("Format " + outputFormat.getClass() + " is not supported");
        Result result = accessor.createResult();
        doZap(source, result, params);
        return accessor.getResult();
    }

    private void doZap(Source source, Result result, Object... params) {
        XQResultSequence seq = null;
        try {
            XQPreparedExpression expression = resource(params);
            expression.bindDocument(XQConstants.CONTEXT_ITEM, source, null);
            bindExpressionContext(expression, staticParams);
            bindExpressionContext(expression, resourceParameters(params));
            seq = expression.executeQuery();
            seq.writeSequenceToResult(result);
        } catch (Exception e) {
            throw new RuntimeException("XQuery processing failed", e);
        } finally {
            if (seq != null && !seq.isClosed()) {
                try {
                    seq.close();
                } catch (XQException e) {
                    LOG.trace("XQLTransmogrifier didn't return a value.", e);
                }
            }
        }
    }

    private void bindExpressionContext(XQDynamicContext exp, Map<String, Object> params) throws XQException {
        if (params == null) {
            return;
        }
        for (Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(RESOURCE_LOCATION)) {
                continue;
            }
            Object value = entry.getValue();
            if (value instanceof java.lang.String) {
                exp.bindString(new QName(entry.getKey()), (String) value, null);
            } else if (value instanceof javax.xml.transform.Source) {
                exp.bindDocument(new QName(entry.getKey()), (Source) entry.getValue(), null);
            } else if (value instanceof Boolean) {
                exp.bindBoolean(new QName(entry.getKey()), (Boolean) entry.getValue(), null);
            } else {
                exp.bindAtomicValue(new QName(entry.getKey()), (String) entry.getValue(), null);
            }
        }
    }

    /**
     * This method had to be overridden because {@link XQPreparedExpression} objects
     * are not thread-safe, thus an additional replication step is necessary.
     */
    @Override
    protected XQPreparedExpression resource(Object... params) throws Exception {
        XQPreparedExpression expression = super.resource(params);
        return getConnection().copyPreparedExpression(expression);
    }

    @Override
    public XQPreparedExpression createResource(Object... params) {
        String resourceLocation = resourceLocation(params);
        LOG.debug("Create new template for {}", resourceLocation);
        try {
            InputStream stream = resourceContent(params).getInputStream();
            return getConnection().prepareExpression(stream);
        } catch (Exception e) {
            throw new IllegalArgumentException("The resource "
                    + resourceLocation + " is not valid", e);
        }
    }

    synchronized private SaxonXQConnection getConnection() throws XQException {
        if (connection == null) {
            connection = (SaxonXQConnection) DATA_SOURCE.getConnection();
        }
        return connection;
    }

}
