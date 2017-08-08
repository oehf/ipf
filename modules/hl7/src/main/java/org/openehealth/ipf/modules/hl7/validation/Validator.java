/*
 * Copyright 2017 the original author or authors.
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
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.Severity;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.validation.impl.SimpleValidationExceptionHandler;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;

public class Validator {

    /**
     * Validates an HL7v2 message.
     *
     * @param msg     message to be validated.
     * @param context optional HAPI Context referencing validation rules.
     *                When <code>null</code> then the message's own context will be used instead.
     */
    public static void validate(Message msg, HapiContext context) {
        HapiContext ctx = context == null ? msg.getParser().getHapiContext() : context;

        // We could also write an exception handler on top of SimpleValidationExceptionHandler that
        // encapsulates the behavior below, but that may restrict custom validation...
        SimpleValidationExceptionHandler handler = new SimpleValidationExceptionHandler(ctx);
        handler.setMinimumSeverityToCollect(Severity.ERROR);
        try {
            if (ctx.<Boolean>getMessageValidator().validate(msg, handler)) {
                throw new ValidationException("Message validation failed", handler.getExceptions());
            }
        } catch (HL7Exception hl7Exception) {
            throw new ValidationException("Message validation failed", hl7Exception);
        }
    }

}
