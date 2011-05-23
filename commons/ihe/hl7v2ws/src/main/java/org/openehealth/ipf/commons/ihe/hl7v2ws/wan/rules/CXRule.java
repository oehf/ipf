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

import ca.uhn.hl7v2.model.v26.datatype.CX;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * @author Mitko Kolev
 * 
 */
class CXRule extends AbstractCompositeTypeRule<CX> {

    private static final long serialVersionUID = -2458721983593718344L;

    public CXRule() {
        super(CX.class);
    }

    @Override
    public void validate(CX cx, String path, Collection<ValidationException> violations) {
        mustBeNonEmpty(cx, 1, path, violations);
        mustBeNonEmpty(cx, 4, path, violations);
    }

    @Override
    public String getSectionReference() {
        return "Continua Design Guidelines 2010, Section K.3.1";
    }
}
