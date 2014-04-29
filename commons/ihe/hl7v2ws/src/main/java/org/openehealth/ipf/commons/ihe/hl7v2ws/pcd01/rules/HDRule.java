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
package org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.rules;

import java.util.ArrayList;
import java.util.Collection;

import ca.uhn.hl7v2.Location;
import org.openehealth.ipf.modules.hl7.validation.model.AbstractCompositeTypeRule;

import ca.uhn.hl7v2.model.v26.datatype.HD;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * @author Mitko Kolev
 * @author Chrustian Ohr
 * 
 */
public class HDRule extends AbstractCompositeTypeRule<HD> {
    private static final long serialVersionUID = -9037983867502164173L;

    public HDRule() {
        super(HD.class);
    }

    @Override
    public ValidationException[] validate(HD hd, Location location) {
        // Either HD-1 or both HD-2 and HD-3 shall be non-empty
        Collection<ValidationException> violations = new ArrayList<ValidationException>();
        if (isEmpty(hd, 1)) {
            potentialViolation(enforce(not(empty()), hd, 2), location, violations);
            potentialViolation(enforce(not(empty()), hd, 3), location, violations);
            if (isEqual("ISO", hd, 3)) {
                potentialViolation(enforce(oid(), hd, 2), location, violations);
            }
            if (isEqual("EUI-64", hd, 3)) {
                potentialViolation(enforce((matches(EUI_64_PATTERN)), hd, 2), location, violations);
            }
        }
        if (isEmpty(hd, 2) || isEmpty(hd, 3)) {
            potentialViolation(enforce(not(empty()), hd, 1), location, violations);
        }
        return violations.toArray(new ValidationException[violations.size()]);
    }

    @Override
    public String getSectionReference() {
        return "PCD Rev. 2, Vol. 2 App C.6";
    }

    @Override
    public String getDescription() {
        return "HD composite type rule";
    }
}
