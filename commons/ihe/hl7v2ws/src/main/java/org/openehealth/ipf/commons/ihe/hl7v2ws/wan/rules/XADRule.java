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
package org.openehealth.ipf.commons.ihe.hl7v2ws.wan.rules;

import java.util.ArrayList;
import java.util.Collection;

import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.v26.datatype.CWE;
import org.openehealth.ipf.modules.hl7.validation.model.AbstractCompositeTypeRule;

import ca.uhn.hl7v2.model.v26.datatype.XAD;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * @author Mitko Kolev
 * @author Christian Ohr
 * @deprecated
 */
public class XADRule extends AbstractCompositeTypeRule<XAD> {

    public XADRule() {
        super(XAD.class);
    }

    @Override
    public ValidationException[] validate(XAD xad, Location location) {
        Collection<ValidationException> violations = new ArrayList<ValidationException>();
        validate(enforce(not(empty()), xad, 1), location, violations);
        validate(enforce(not(empty()), xad, 3), location, violations);
        validate(enforce(not(empty()), xad, 4), location, violations);
        validate(enforce(not(empty()), xad, 5), location, violations);
        validate(enforce(not(empty()), xad, 6), location, violations);
        return violations.toArray(new ValidationException[violations.size()]);
    }

    @Override
    public String getSectionReference() {
        return "Continua Design Guidelines 2010, Section K.2.6";
    }
}
