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
import ca.uhn.hl7v2.model.v26.datatype.EI;
import org.openehealth.ipf.modules.hl7.validation.model.AbstractCompositeTypeRule;

import ca.uhn.hl7v2.model.v26.datatype.SN;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * @author Mitko Kolev
 * @author Christian Ohr
 */
public class SNRule extends AbstractCompositeTypeRule<SN> {

    public SNRule() {
        super(SN.class);
    }
    
    
    @Override
    public String getSectionReference() {
        return "Continua Design Guidelines 2010, Section K.3.6";
    }

    @Override
    public ValidationException[] validate(SN sn, Location location) {
        Collection<ValidationException> violations = new ArrayList<ValidationException>();
        if (!isEmpty(sn, 2) && !isEmpty(sn, 4)) {
            validate(enforce(not(empty()), sn, 3), location, violations);
        }
        validate(enforce(in(">", "<", ">=", "<=", "=", "<>"), sn, 1), location, violations);
        validate(enforce(in("-", "+", "/", ".", ":"), sn, 3), location, violations);

        return violations.toArray(new ValidationException[violations.size()]);
    }
}
