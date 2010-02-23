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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This package private SchematronTransmogrifier works exactly like its
 * superclass, {@link SchematronTransmogrifier} except for its expectations
 * towards the parameters for the
 * {@link #zap(javax.xml.transform.Source, Object...)} method. There must be
 * exactly one parameter of type {@link SchematronProfile}, whoch contains both
 * rules resource location and optioal Schematron parameters.
 * 
 * @author Christian Ohr
 */
class ValidatingSchematronTransmogrifier<T> extends SchematronTransmogrifier<T> {

    private final static Log LOG = LogFactory
            .getLog(ValidatingSchematronTransmogrifier.class);

    public ValidatingSchematronTransmogrifier() {
        super();
    }

    public ValidatingSchematronTransmogrifier(Class<T> outputFormat,
            Map<String, Object> staticParams) {
        super(outputFormat, staticParams);
    }

    public ValidatingSchematronTransmogrifier(Class<T> outputFormat) {
        super(outputFormat);
    }

    public ValidatingSchematronTransmogrifier(XsltTransmogrifier<String> t,
            Class<T> clazz, Map<String, Object> staticParams) {
        super(t, clazz, staticParams);
    }

    public ValidatingSchematronTransmogrifier(XsltTransmogrifier<String> t,
            Class<T> clazz) {
        super(t, clazz);
    }

    @Override
    protected String resource(Object... params) {
        SchematronProfile p = (SchematronProfile) params[0];
        LOG.debug("Schematron rules are : " + p.getRules());
        return p.getRules();
    }

    @Override
    protected Map<String, Object> parameters(Object... params) {
        SchematronProfile p = (SchematronProfile) params[0];
        LOG.debug("Schematron parameters are : " + p.getParameters());
        return p.getParameters();
    }

}
