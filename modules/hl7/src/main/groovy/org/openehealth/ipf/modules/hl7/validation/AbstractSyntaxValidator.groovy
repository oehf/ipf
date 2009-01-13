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
package org.openehealth.ipf.modules.hl7.validation

import ca.uhn.hl7v2.conf.check.DefaultValidator
import ca.uhn.hl7v2.conf.spec.message.Seg
import ca.uhn.hl7v2.conf.ProfileException
import ca.uhn.hl7v2.conf.spec.message.StaticDef
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.Message

/**
 * Subclass of HAPI's conformance validator, that stops checking at the segment level, i.e. the
 * existence and cardinality of fields are not checked.
 * 
 * @author Christian Ohr
 */
public class AbstractSyntaxValidator extends DefaultValidator {

     public HL7Exception[] validate(Message message, StaticDef profile) throws ProfileException, HL7Exception {
         super.validate(message, profile)
     }
     
     /**
      * Always succeeds, because the fields of a segment are not validated in this implementation
      */
     public HL7Exception[] testSegment(ca.uhn.hl7v2.model.Segment segment, Seg profile, String profileID) throws ProfileException {
         new HL7Exception[0]
     }
    
}
