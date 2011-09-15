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

import static org.openehealth.ipf.commons.xml.ParametersHelper.resource;
import static org.openehealth.ipf.commons.xml.ParametersHelper.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQConstants;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQDynamicContext;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.sf.saxon.Configuration;
import net.sf.saxon.xqj.SaxonXQDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;

/**
 * XQuery transformer similar to the XsltTransmogrifier
 * 
 * @author Stefan Ivanov
 * 
 * @param <T>
 */
public class XqjTransmogrifier<T> implements Transmogrifier<Source, T> {
    private final static Log LOG = LogFactory.getLog(XqjTransmogrifier.class);

    private final Map<Object, XQPreparedExpression> cache = new HashMap<Object, XQPreparedExpression>();
    private final XQDataSource ds;
    private XQConnection connection;
    private Map<String, String> staticParams;
    private final Class<T> outputFormat;

    @SuppressWarnings("unchecked")
    public XqjTransmogrifier() {
        this((Class<T>) String.class);
    }

    /**
     * @param outputFormat
     *            currently supported: String, Writer, OutputStream
     * @throws XQException
     */
    public XqjTransmogrifier(Class<T> outputFormat) {
        super();
        Configuration globalConfig = new Configuration();
        globalConfig.setHostLanguage(Configuration.XQUERY);
        globalConfig.setURIResolver(new ClasspathUriResolver(globalConfig.getURIResolver()));
        ds = new SaxonXQDataSource(globalConfig);
        this.outputFormat = outputFormat;
    }

    /**
     * @param outputFormat
     *            currently supported: String
     * @param staticParams
     *            static XQuery parameters
     * @param globalParams
     *            static XQuery parameters
     * @throws XQException
     */
    public XqjTransmogrifier(Class<T> outputFormat, Map<String, Object> globalParams)
            throws XQException {
        this(outputFormat);
        initGlobalConfiguration(globalParams);
    }

    private void initGlobalConfiguration(Map<String, Object> globalParams) {
        if (globalParams == null)
            return;
        Configuration globalConfig = ((SaxonXQDataSource) ds).getConfiguration();
        for (Entry<String, Object> entry : globalParams.entrySet()) {
            globalConfig.setConfigurationProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 
     * Converts a Source into a typed result using a XQuery processor.
     * <p>
     * The XQ resource location is mandatory in the first extra parameter. Other
     * parameters may be passed as a Map in the second parameter.
     * 
     * @see org.openehealth.ipf.commons.core.modules.api.Transmogrifier#zap(java.lang.Object,
     *      java.lang.Object[])
     */
    @Override
    public T zap(Source source, Object... params) {
        ResultHolder<T> accessor = ResultHolderFactory.create(outputFormat);
        Result result = accessor.createResult();
        doZap(source, result, params);
        return accessor.getResult();
    }

    private void doZap(Source source, Result result, Object... params) {
        XQResultSequence seq = null;
        try {
            XQPreparedExpression expression = expression(params);
            expression.bindDocument(XQConstants.CONTEXT_ITEM, source, null);
            if (params.length != 0) {
                bindExpressionContext(expression, ParametersHelper.parameters(params));
            }
            seq = expression.executeQuery();
            seq.writeSequenceToResult(result);
        } catch (XQException e) {
            throw new RuntimeException("XQuery processing failed", e);
        } finally {
            if (seq != null && !seq.isClosed())
                closeQuietly(seq);
        }
    }

    private void bindExpressionContext(XQDynamicContext exp, Map<String, Object> params)
            throws XQException {
        if (params == null) {
            return;
        }
        for (Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(ParametersHelper.RESOURCE_LOCATION))
                continue;
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
     * Instantiates a {@link XQPreparedExpression} object according to the
     * parameters. The expression is stored in a cache for faster access in
     * subsequent calls.
     * 
     * @param params
     *            params[0] must be of type String and reference the stylesheet
     *            resource
     * @return a {@link XQPreparedExpression} object according to the parameters
     * @TODO use an external cache implementation?
     */
    synchronized protected XQPreparedExpression expression(Object... params) {
        if (!(cache.containsKey(resource(params)))) {
            cache.put(resource(params), doPreparedExpression(params));
        }
        return cache.get(resource(params));
    }

    private XQPreparedExpression doPreparedExpression(Object... params) {
        String resourceLocation = resource(params);
        LOG.debug("Create new template for " + resourceLocation);
        try {
            initConnection();
            XQPreparedExpression expression = connection
                    .prepareExpression(source(resource(params)));
            if (staticParams != null) {
                bindExpressionContext(expression, ParametersHelper.parameters(staticParams));
            }
            return expression;
        } catch (Exception e) {
            throw new IllegalArgumentException("The resource "
                    + resourceLocation + " is not valid", e);
        }
    }

    private void initConnection() throws XQException {
        if (connection == null)
            connection = ds.getConnection();
    }

    private void closeQuietly(XQResultSequence seq) {
        try {
            seq.close();
        } catch (XQException ignored) {
            LOG.trace("XQLTransmogrifier didn't return a value.");
        }
    }

    public Map<String, String> getStaticParams() {
        return staticParams;
    }

    public void setStaticParams(Map<String, String> staticParams) {
        this.staticParams = staticParams;
    }

}
