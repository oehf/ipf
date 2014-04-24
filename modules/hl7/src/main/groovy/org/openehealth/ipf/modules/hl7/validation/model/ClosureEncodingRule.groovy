/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.validation.model

import ca.uhn.hl7v2.validation.EncodingRule
import ca.uhn.hl7v2.validation.ValidationException


/**
 * Rule class for validating encoded messages. The actual validation is executed
 * by means of a Groovy closure. The closure is expected to return a
 * (possibly empty) array of {@link ValidationException}s.
 *
 * @author Christian Ohr
 */
public class ClosureEncodingRule extends ClosureRuleSupport implements EncodingRule {

    ClosureEncodingRule(Closure testClosure) {
        super(testClosure)
    }

    ClosureEncodingRule(String description, String sectionReference, Closure testClosure) {
        super(description, sectionReference, testClosure)
    }

    /**
     * @see ca.uhn.hl7v2.validation.EncodingRule#test(java.lang.String)
     */
    public ValidationException[] test(String msg) {
        apply(msg)
    }

    @Override
    ValidationException[] apply(String msg) {
        testClosure.call(msg)
    }
}
