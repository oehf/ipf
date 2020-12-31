/*
 * Copyright 2015 the original author or authors.
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

import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.xml.svrl.FailedAssert;
import org.openehealth.ipf.commons.xml.svrl.SchematronOutput;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

/**
 * Validation wrapper around the {@link SchematronTransmogrifier}. The
 * result is scanned for <code>svrl:failed-assert</code> elements, and
 * error text and details (if available) are put into the {@link ValidationException}'s message.
 * <p>
 * The Validator accepts a {@link Source} as input, and a {@link SchematronProfile}
 * as validation profile parameter.
 *
 * @author Christian Ohr
 */
public class SchematronValidator implements Validator<Source, SchematronProfile> {

    private final SchematronTransmogrifier<SchematronOutput> schematronTransmogrifier =
            new SchematronTransmogrifier<>(SchematronOutput.class);

    @Override
    public void validate(Source message, SchematronProfile profile) {
        var svrl = schematronTransmogrifier.zap(message, profile);
        List<ValidationException> exceptions = new ArrayList<>();
        svrl.getActivePatternAndFiredRuleAndFailedAssert().stream()
                .filter(o -> o instanceof FailedAssert)
                .forEach(o -> {
                    var failedAssert = (FailedAssert) o;
                    exceptions.add(new ValidationException(message(failedAssert)));
                });
        if (!exceptions.isEmpty()) {
            throw new SchematronValidationException(exceptions, svrl);
        }
    }

    private static String message(FailedAssert failedAssert) {
        var sb = new StringBuilder()
                .append("Validation error at ")
                .append(failedAssert.getLocation())
                .append(" : ")
                .append(failedAssert.getText());

        if (!failedAssert.getDiagnosticReference().isEmpty()) {
            sb.append("\nDetail:");
            for (var diagnosticReference : failedAssert.getDiagnosticReference()) {
                sb.append('\n').append(diagnosticReference.getText());
            }
        }

        return sb.toString();
    }

}
