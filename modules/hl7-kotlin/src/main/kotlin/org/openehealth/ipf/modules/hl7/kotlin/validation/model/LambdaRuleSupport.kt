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

import ca.uhn.hl7v2.validation.ValidationException
import ca.uhn.hl7v2.validation.impl.RuleSupport

/**
 * @author Christian Ohr
 */
abstract class LambdaRuleSupport<T>(protected val rule: (T) -> Array<ValidationException>,
                                    private val description: String,
                                    private val sectionReference: String) : RuleSupport<T>() {

    override fun getSectionReference(): String = sectionReference
    override fun getDescription(): String = description

}