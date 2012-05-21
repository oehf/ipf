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
package org.openehealth.ipf.commons.xml

import javax.xml.transform.Source;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;

/**
 * Validation wrapper around the {@link SchematronTransmogrifier}. The
 * result is scanned for <code>svrl:failed-assert</code> elements, and
 * error text and details (if available) are put into the ValidationExecpion's message.
 * <p>
 * The Validator accepts a {@link Source} as input, and a {@link SchematronProfile}
 * as validation profile parameter.
 * 
 * @author Christian Ohr
 */
public class SchematronValidator implements Validator {

    private SchematronTransmogrifier<String> schematronTransmogrifier

    public SchematronValidator() {
        this.schematronTransmogrifier = new ValidatingSchematronTransmogrifier<String>(String.class)
    }

    public void validate(Object message, Object profile) {
        String s = schematronTransmogrifier.zap((Source)message, profile);
        validateResult(s)
    }

    protected void validateResult(String s) {
        def exceptions = []
        def result = new XmlSlurper()
                .parseText(s)
                .declareNamespace(svrl:'http://purl.oclc.org/dsdl/svrl')

        exceptions = result.'svrl:failed-assert'.collect {
            def location = it.@location?.text()
            def text = it.'svrl:text'?.text()
            def detail = it.'svrl:diagnostic-reference'?.text()
            def message = text + (detail ? "\nDetail:$detail" : "")
            new ValidationException("Validation error at $location : $message")
        }

        if (exceptions.size() > 0) {
            throw new ValidationException("Schematron validation error", exceptions)
        }
    }
}
