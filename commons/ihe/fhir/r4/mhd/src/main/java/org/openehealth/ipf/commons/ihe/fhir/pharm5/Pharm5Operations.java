/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.pharm5;

import java.util.Objects;

/**
 * The different operations of CMPD PHARM-5 transaction.
 *
 * @author Quentin Ligier
 * @since 4.3
 **/
public enum Pharm5Operations {

    FIND_MEDICATION_TREATMENT_PLANS("$find-medication-treatment-plans"),
    FIND_PRESCRIPTIONS("$find-prescriptions"),
    FIND_DISPENSES("$find-dispenses"),
    FIND_MEDICATION_ADMINISTRATIONS("$find-medication-administrations"),
    FIND_PRESCRIPTIONS_FOR_VALIDATION("$find-prescriptions-for-validation"),
    FIND_PRESCRIPTIONS_FOR_DISPENSE("$find-prescriptions-for-dispense"),
    FIND_MEDICATION_LIST("$find-medication-list");

    private final String operation;

    Pharm5Operations(final String operation) {
        this.operation = Objects.requireNonNull(operation);
    }

    public String getOperation() {
        return this.operation;
    }
}
