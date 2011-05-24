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

import ca.uhn.hl7v2.model.v26.datatype.EI;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * @author Mitko Kolev
 * 
 */
public class EIRule extends AbstractCompositeTypeRule<EI> {
    private static final long serialVersionUID = -3955263429919793537L;

    public EIRule() {
        super(EI.class);
    }

    @Override
    public void validate(EI ei, String path, Collection<ValidationException> violations) {
        mustBeNonEmpty(ei, 1, path, violations);

        // Either EI-2 or both EI-3 and EI-4 shall be non-empty.
        if (isEmpty(ei, 2)) {
            mustBeNonEmpty(ei, 3, path, violations);
            mustBeNonEmpty(ei, 4, path, violations);
        }
        if (isEmpty(ei, 3) || isEmpty(ei, 4)) {
            mustBeNonEmpty(ei, 2, path, violations);
        }
        
        if (isEqual("ISO", ei, 4)) {
            mustMatchIsoOid(ei, 3, path, violations);
        }
        if (isEqual("EUI-64", ei, 4)) {
            mustMatchEui64(ei, 3, path, violations);
        }
    }
   
    @Override
    public String getSectionReference() {
        return "PCD Rev. 2, Vol. 2 App. C.5 ";
    }
}
