/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.iti67;

import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionOptionsProvider;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;

/**
 * @author Christian Ohr
 * @since 4.1
 */
public class Iti67OptionsProvider implements FhirTransactionOptionsProvider<FhirQueryAuditDataset, Iti67Options> {

    @Override
    public Class<Iti67Options> getTransactionOptionsType() {
        return Iti67Options.class;
    }

    @Override
    public Iti67Options getDefaultOption() {
        return Iti67Options.COMPATIBILITY;
    }

    @Override
    public AuditStrategy<FhirQueryAuditDataset> getAuditStrategy(boolean serverSide) {
        return new Iti67AuditStrategy(serverSide);
    }
}
