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
package org.openehealth.ipf.modules.hl7.validation.support

import org.openehealth.ipf.commons.core.modules.api.Validator
import ca.uhn.hl7v2.validation.ValidationContext
import ca.uhn.hl7v2.validation.MessageValidator
import ca.uhn.hl7v2.model.Message

/**
 * Adapter class between {@link Validator} interface and HAPI's
 * {@link MessageValidator}
 * 
 * @author Christian Ohr
 */
public class HL7Validator implements Validator {

     /**
      * Validates a message.
      * @param msg HAPI message
      * @param context HAPI ValidationContext
      * @throws HL7Exception (or subclass thereof) if validation has failed
      */
    void validate(Object msg, Object context) {
        new MessageValidator(context, true).validate(msg)
    }
    
}
