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

import java.io.StringReader;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class SchematronTransmogrifier extends XsltTransmogrifier {

    private static Log LOG = LogFactory.getLog(SchematronTransmogrifier.class);

    private XsltTransmogrifier xsltTransmogrifier;

    public SchematronTransmogrifier() {
        this(new XsltTransmogrifier());
    }

    public SchematronTransmogrifier(XsltTransmogrifier t) {
        super();
        this.xsltTransmogrifier = t;
    }

    /**
     * Sets the schematron rules and automatically compiles a new stylesheet to
     * be used when calling <code>zap</code>.
     * 
     * @param rules
     * @throws Exception
     */
    @Override
    synchronized protected Templates template(Object... params) {
        SchematronProfile p = (SchematronProfile) params[0];
        if (!getTemplateCache().containsKey(p)) {
            try {
                LOG.debug("Creating new Schematron stylesheet");
                Source rules = stylesheetSource(p.getRules());
                Source source = step(xsltTransmogrifier, rules,
                        "schematron/iso_dsdl_include.xsl", p.getParameters());
                LOG.debug("step 1 of 3");
                source = step(xsltTransmogrifier, source,
                        "schematron/iso_abstract_expand.xsl", p.getParameters());
                LOG.debug("step 2 of 3");
                source = step(xsltTransmogrifier, source,
                        "schematron/iso_svrl_for_xslt2.xsl", p.getParameters());
                LOG.debug("step 3 of 3");
                Templates template = getFactory().newTemplates(source);
                LOG.debug("done!");
                getTemplateCache().put(p, template);
                return template;
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "The schematron rules resource " + p.getRules()
                                + " is not valid");
            }
        } else {
            return getTemplateCache().get(p);
        }
    }

    @Override
    protected Map<String, Object> parameters(Object... params) {
        SchematronProfile p = (SchematronProfile) params[0];
        LOG.debug("Schematron parameters are : " + p.getParameters());
        return p.getParameters();
    }

    private static Source step(XsltTransmogrifier t, Source input,
            String stylesheet, Map<String, Object> params) {
        String s = t.zapToString(input, stylesheet, params);
        return new StreamSource(new StringReader(s));
    }

}
