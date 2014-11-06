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

import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.validation.PrimitiveTypeRule;
import ca.uhn.hl7v2.validation.Rule;
import ca.uhn.hl7v2.validation.ValidationException;
import ca.uhn.hl7v2.validation.builder.BuilderSupport;

/**
 * Rule for validating {@link Composite} types.
 * 
 * @author Mitko Kolev
 * 
 * @see PrimitiveTypeRule
 * @deprecated
 */
public abstract class CompositeTypeRule<T extends Composite> extends BuilderSupport implements Rule<Composite>  {


    /**
     * Returns if the rule should check the given class.
     * 
     * @return a class instance.
     */
    public abstract boolean matches(Class<? extends Composite> clazz);

    public abstract ValidationException[] apply(T composite, Location location);

}
