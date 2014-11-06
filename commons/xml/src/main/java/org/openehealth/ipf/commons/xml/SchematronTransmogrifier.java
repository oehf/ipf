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

import java.io.StringReader;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts a XML document into a Schematron validation report by applying
 * Schematron rules. The rules to be used are passed in with the
 * {@link #zap(Source, Object...)} call, however, the resulting Xslt Template
 * object is cached for subsequent transformations using this stylesheet.
 * <p>
 * The following standard Schematron parameters can be passed in as Map
 * parameter:
 * <ul>
 * <li>phase : NMTOKEN | "#ALL" (default) Select the phase for validation
 * <li>allow-foreign : "true" | "false" (default) Pass non-Schematron elements
 * and rich markup to the generated stylesheet
 * <li>diagnose : "true" (default) | "false" Add the diagnostics to the
 * assertion test in reports
 * <li>property : "true" (default) | "false" Experimental: Add properties to the
 * assertion test in reports
 * <li>generate-paths : "true" (default) | "false" Generate the @location
 * attribute with XPaths
 * <li>sch.exslt.imports : semi-colon delimited string of filenames for some
 * EXSLT implementations
 * <li>optimize : "visit-no-attributes" Use only when the schema has no
 * attributes as the context nodes
 * <li>generate-fired-rule: "true" (default) | "false" Generate fired-rule
 * elements
 * </ul>
 * <p>
 * 
 * @author Christian Ohr
 */
public class SchematronTransmogrifier<T> extends XsltTransmogrifier<T> {

    private final static Logger LOG = LoggerFactory.getLogger(SchematronTransmogrifier.class);

    private final XsltTransmogrifier<String> xsltTransmogrifier;

    @SuppressWarnings("unchecked")
    public SchematronTransmogrifier() {
        this((Class<T>) String.class);
    }

    public SchematronTransmogrifier(Class<T> outputFormat) {
        this(new XsltTransmogrifier<>(String.class), outputFormat);
    }
    
    public SchematronTransmogrifier(Class<T> outputFormat,
            Map<String, Object> staticParams) {
        this(new XsltTransmogrifier<>(String.class), outputFormat, staticParams);
    }

    public SchematronTransmogrifier(XsltTransmogrifier<String> t, Class<T> clazz) {
        super(clazz);
        xsltTransmogrifier = t;
    }
    
    public SchematronTransmogrifier(XsltTransmogrifier<String> t, Class<T> clazz, Map<String, Object> staticParams) {
        super(clazz, staticParams);
        xsltTransmogrifier = t;
    }

    @Override
    protected Templates doCreateTemplate(Object... params) {
        try {
            LOG.debug("Creating new Schematron stylesheet");
            Source rules = source(resource(params));
            Map<String, Object> parameters = parameters(params);
            LOG.debug("step 1 of 3");
            Source source = step(xsltTransmogrifier, rules,
                    "schematron/iso_dsdl_include.xsl", parameters);
            LOG.debug("step 2 of 3");
            source = step(xsltTransmogrifier, source,
                    "schematron/iso_abstract_expand.xsl", parameters);
            LOG.debug("step 3 of 3");
            source = step(xsltTransmogrifier, source,
                    "schematron/iso_svrl_for_xslt2.xsl", parameters);
            Templates template = getFactory().newTemplates(source);
            LOG.debug("done!");
            return template;
        } catch (Exception e) {
            throw new IllegalArgumentException("The schematron rules resource "
                    + resource(params) + " is not valid");
        }
    }

    private static Source step(XsltTransmogrifier<String> t, Source input,
            String stylesheet, Map<String, Object> params) {
        String s = t.zap(input, stylesheet, params);
        return new StreamSource(new StringReader(s));
    }

}
