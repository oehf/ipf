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

import java.util.Collection;

import org.openehealth.ipf.modules.hl7.validation.model.AbstractCompositeTypeRule;

import ca.uhn.hl7v2.model.v26.datatype.XTN;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * @author Mitko Kolev
 * 
 */
class XTNRule extends AbstractCompositeTypeRule<XTN> {

    private static final long serialVersionUID = -2689901635637437625L;

    public XTNRule() {
        super(XTN.class);
    }

    @Override
    public void validate(XTN xtn, String path, Collection<ValidationException> violations) {
        mustBeNonEmpty(xtn, 2, path, violations);
        mustBeNonEmpty(xtn, 3, path, violations);

        String[] allowed2 = new String[] { "PRN", "NET" };
        mustBeOneOf(allowed2, xtn, 2, path, violations);

        if (isEqual(allowed2[0], xtn, 2)) {
            mustBeEqualTo("PH", xtn, 3, path, violations);
        }
        if (isEqual(allowed2[1], xtn, 2)) {
            mustBeOneOf(new String[] { "X.400", "Internet" }, xtn, 3, path, violations);
        }

        if (!isEmpty(xtn, 4)) {
            String val = val(xtn, 4).toString();
            if (!val.contains("@")) {
                violations.add(new ValidationException(msg("expected valid email address, found "
                                + String.valueOf(val), xtn, 4, path)));
            }
        }
    }

    @Override
    public String getSectionReference() {
        return "Continua Design Guidelines 2010, Section K.3.7";
    }
}
