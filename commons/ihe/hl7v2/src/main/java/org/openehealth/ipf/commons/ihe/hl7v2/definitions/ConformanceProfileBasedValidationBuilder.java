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

package org.openehealth.ipf.commons.ihe.hl7v2.definitions;

import ca.uhn.hl7v2.validation.builder.support.DefaultValidationWithoutTNBuilder;
import org.openehealth.ipf.gazelle.validation.core.CachingGazelleProfileRule;
import org.openehealth.ipf.gazelle.validation.profile.HL7v2Transactions;

/**
 * Simple {@link ca.uhn.hl7v2.validation.builder.ValidationRuleBuilder validation rule builder}
 * that just uses a conformance profile to validate against a message that is allowed for a
 * defined set of {@link HL7v2Transactions HL7v2 based IHE transactions}.
 */
public class ConformanceProfileBasedValidationBuilder extends DefaultValidationWithoutTNBuilder {

    private final HL7v2Transactions transaction;

    public ConformanceProfileBasedValidationBuilder(HL7v2Transactions transaction) {
        this.transaction = transaction;
    }

    /**
     * Configures the matching {@link CachingGazelleProfileRule} to be validated against
     */
    @Override
    protected void configure() {

        super.configure();

        forAllVersions()
                .message()
                .all()
                .test(new CachingGazelleProfileRule(transaction));
    }
}
