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

import ca.uhn.hl7v2.model.v26.datatype.XTN;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * @author Mitko Kolev
 * @author Christian Ohr
 * 
 */
public class XTNRule extends AbstractCompositeTypeRule<XTN> {

    public XTNRule() {
        super(XTN.class);
    }

    @Override
    public String getSectionReference() {
        return "PCD Rev. 2, Vol. 2 App. C.8";
    }

    @Override
    public ValidationException[] validate(XTN xtn, Location location) {
        Collection<ValidationException> violations = new ArrayList<ValidationException>();
        validate(enforce(allOf(not(empty()), in("PRN", "NET")), xtn, 2), location, violations);
        validate(enforce(not(empty()), xtn, 3), location, violations);
        if (isEqual("NET", xtn, 2)) {
            validate(enforce(in("X.400", "Internet"), xtn, 3), location, violations);
        }
        return violations.toArray(new ValidationException[violations.size()]);
    }
}
