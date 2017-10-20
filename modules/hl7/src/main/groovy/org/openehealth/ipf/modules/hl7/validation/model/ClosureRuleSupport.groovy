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

import ca.uhn.hl7v2.validation.ValidationException
import ca.uhn.hl7v2.validation.impl.AbstractMessageRule


/**
 * Base class with common attributes for validation rules.
 * 
 * @author Christian Ohr
 */
class ClosureRuleSupport {

    ClosureRuleSupport() {
    }

    ClosureRuleSupport(Closure<ValidationException[]> testClosure) {
         this("", "", testClosure)
     }
     
     ClosureRuleSupport(String description, String sectionReference, Closure<ValidationException[]> testClosure) {
         this.testClosure = testClosure
         this.description = description
         this.sectionReference = sectionReference
     }

     Closure<ValidationException[]> testClosure
     String sectionReference
     String description   
    
}
