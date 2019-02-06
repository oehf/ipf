/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.pcc44;

import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionOptionsProvider;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;

/**
 * @author Christian Ohr
 */
public class Pcc44OptionsProvider implements FhirTransactionOptionsProvider<FhirQueryAuditDataset, Pcc44Options> {

    @Override
    public Class<Pcc44Options> getTransactionOptionsType() {
        return Pcc44Options.class;
    }

    @Override
    public Pcc44Options getDefaultOption() {
        return Pcc44Options.ALL;
    }

    @Override
    public AuditStrategy<FhirQueryAuditDataset> getAuditStrategy(boolean serverSide) {
        return new Pcc44AuditStrategy(serverSide);
    }
}
