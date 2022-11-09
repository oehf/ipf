/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.kotlin.validation.model

import ca.uhn.hl7v2.validation.PrimitiveTypeRule
import ca.uhn.hl7v2.validation.ValidationException

/**
 * @author Christian Ohr
 * @since 3.5
 */
class LambdaPrimitiveTypeRule(rule: (String?) -> Array<ValidationException>,
                              description: String = "",
                              sectionReference: String = ""
                              ) :
        LambdaRuleSupport<String?>(rule, description, sectionReference),
        PrimitiveTypeRule {

    var omitLeadingWhitespace: Boolean = false
    var omitTrailingWhitespace: Boolean = false

    @Deprecated(message = "Deprecated PrimitiveTypeRule", replaceWith = ReplaceWith("PrimitiveTypeRule.apply()"))
    override fun test(value: String?): Boolean = apply(value).isEmpty()

    override fun apply(value: String?): Array<ValidationException> = rule(correct(value))

    override fun correct(s: String?): String? {
        var corrected = s
        if (omitLeadingWhitespace)
            corrected = corrected?.trimStart()
        if (omitTrailingWhitespace)
            corrected = corrected?.trimEnd()
        return corrected
    }
}