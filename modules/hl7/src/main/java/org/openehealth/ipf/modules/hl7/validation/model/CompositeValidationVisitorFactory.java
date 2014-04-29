/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.validation.model;

import java.util.List;

import ca.uhn.hl7v2.model.MessageVisitorFactory;

/**
 *
 */
public class CompositeValidationVisitorFactory implements MessageVisitorFactory<CompositeValidationVisitor> {

    private List<CompositeTypeRule> compositeRules;

    public CompositeValidationVisitorFactory(List<CompositeTypeRule> compositeRules) {
        this.compositeRules = compositeRules;
    }

    @Override
    public CompositeValidationVisitor create() {
        return new CompositeValidationVisitor(compositeRules);
    }
}
