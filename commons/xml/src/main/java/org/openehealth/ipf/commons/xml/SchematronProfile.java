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

/**
 * Validation profile exclusively used by {@link SchematronValidator}. The
 * following standard Schematron parameters can be passed in as Map parameter:
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
 * 
 * @author Christian Ohr
 * @see SchematronValidator
 */
public class SchematronProfile {

    public SchematronProfile() {
        super();
    }

    public SchematronProfile(String rules) {
        super();
        this.rules = rules;
    }

    public SchematronProfile(String rules, Map<String, Object> parameters) {
        super();
        this.rules = rules;
        this.parameters = parameters;
    }

    private String rules;

    private Map<String, Object> parameters;

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

}
