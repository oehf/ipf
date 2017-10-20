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

import ca.uhn.hl7v2.validation.PrimitiveTypeRule
import ca.uhn.hl7v2.validation.ValidationException


/**
 * Rule class for validating Primitive value. The actual validation is executed
 * by means of a Groovy closure, which is expected to return a value that can
 * evaluate to a ValidationException[].
 *
 * @author Christian Ohr
 */
class ClosurePrimitiveTypeRule extends ClosureRuleSupport implements PrimitiveTypeRule {

    boolean omitLeadingWhitespace
    boolean omitTrailingWhitespace

    static Closure PASS = { true }

    ClosurePrimitiveTypeRule(Closure testClosure) {
        this("", "", testClosure)
    }

    ClosurePrimitiveTypeRule(String description, String sectionReference, Closure testClosure) {
        super(description, sectionReference, testClosure)
    }

    /**
     * @see ca.uhn.hl7v2.validation.PrimitiveTypeRule#correct(java.lang.String)
     */
    String correct(String s) {
        if (omitLeadingWhitespace)
            s = (s =~ /^\s+/).replaceFirst('')
        if (omitTrailingWhitespace)
            s = (s =~ /\s+$/).replaceFirst('')
        s
    }

    /**
     * @see ca.uhn.hl7v2.validation.PrimitiveTypeRule#test(java.lang.String)
     */
    boolean test(String s) {
        ValidationException[] exceptions = apply(s)
        return exceptions == null || exceptions.length == 0
    }

    @Override
    ValidationException[] apply(String value) {
        testClosure.call(correct(value))
    }
}
