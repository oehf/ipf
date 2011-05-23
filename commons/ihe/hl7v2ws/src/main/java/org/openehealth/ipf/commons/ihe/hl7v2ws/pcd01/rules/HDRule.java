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

import java.util.Collection;

import org.openehealth.ipf.modules.hl7.validation.model.AbstractCompositeTypeRule;

import ca.uhn.hl7v2.model.v26.datatype.HD;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * @author Mitko Kolev
 * 
 */
public class HDRule extends AbstractCompositeTypeRule<HD> {

    private static final long serialVersionUID = -9037983867502164173L;
    private static String REFERENCE = "PCD Rev. 2, Vol. 2 App C.6";

    public HDRule() {
        super(HD.class);
    }

    @Override
    public void validate(HD hd, String path, Collection<ValidationException> violations) {
        // Either HD-1 or both HD-2 and HD-3 shall be non-empty
        if (isEmpty(hd, 1)) {
            mustBeNonEmpty(hd, 2, path, violations);
            mustBeNonEmpty(hd, 3, path, violations);
        }
        if (isEmpty(hd, 3) || isEmpty(hd, 4)) {
            mustBeNonEmpty(hd, 1, path, violations);
        }
        
        if (isEqual("ISO", hd, 3)) {
            mustMatchIsoOid(hd, 2, path, violations);
        }
        if (isEqual("EUI-64", hd, 3)) {
            mustMatchEui64(hd, 2, path, violations);
        }
    }

    @Override
    public String getSectionReference() {
        return REFERENCE;
    }
}
