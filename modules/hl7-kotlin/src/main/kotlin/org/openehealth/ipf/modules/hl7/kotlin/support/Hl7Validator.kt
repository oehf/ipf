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

package org.openehealth.ipf.modules.hl7.kotlin.support

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.validation.DefaultValidator
import ca.uhn.hl7v2.validation.ValidationContext
import org.openehealth.ipf.commons.core.modules.api.ValidationException
import org.openehealth.ipf.commons.core.modules.api.Validator

/**
 * @author Christian Ohr
 */
class Hl7Validator<R> : Validator<Message, ValidationContext> {

    override fun validate(message: Message?, profile: ValidationContext?) {
        try {
            DefaultValidator<R>(profile).validate(message)
        } catch (e: HL7Exception) {
            throw ValidationException(e)
        }
    }
}