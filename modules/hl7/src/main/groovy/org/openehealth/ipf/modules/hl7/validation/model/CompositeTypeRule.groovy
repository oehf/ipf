/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.validation.model;

import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.validation.PrimitiveTypeRule;
import ca.uhn.hl7v2.validation.Rule;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * Rule for validating {@link Composite} types.
 * FIXME declared as abstract class to work arround compile problems in Eclipse
 * 
 * @author Mitko Kolev
 * 
 * @see PrimitiveTypeRule
 */
public abstract class CompositeTypeRule<T extends Composite> implements Rule {

    /**
     * Tests the given (fully populated) message against the criteria defined by
     * this rule class.
     * 
     * @return a list of exceptions indicating points at which the given message
     *         failed to validate (empty if validation succeeds;
     */
    public abstract ValidationException[] test(T composite, int fieldIndex, int repetition, String path);

    /**
     * Returns if the rule should check the given class.
     * 
     * @return a class instance.
     */
    public abstract boolean appliesFor(Class<? extends Composite> clazz);
    
}
