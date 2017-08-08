/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.validation;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.validation.MessageValidator;
import ca.uhn.hl7v2.validation.ValidationContext;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;

/**
 * IPF Adapter that bridges HAPI Validators and ValidationContext to IPF's
 * {@link Validator} interface.
 * @deprecated use {@link org.openehealth.ipf.modules.hl7.validation.Validator}
 */
@Deprecated
public class ValidatorAdapter implements Validator<Message, ValidationContext> {

    /**
     * Validates the message using a validator that fails on the first validation problem
     * that is identified. If the
     *
     * @param message message to be validated
     * @param profile ValidationContext to be validated against or null if the ValidationContext of
     *                the message's {@link ca.uhn.hl7v2.HapiContext} shall be used.
     */
    @Override
    public void validate(Message message, ValidationContext profile) {
        ca.uhn.hl7v2.validation.Validator validator = new MessageValidator(
                profile != null ? profile : message.getParser().getValidationContext(),
                true);
        try {
            validator.validate(message);
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }
    }
}
