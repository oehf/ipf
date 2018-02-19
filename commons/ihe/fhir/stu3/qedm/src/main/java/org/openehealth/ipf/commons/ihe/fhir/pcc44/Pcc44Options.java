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


import org.openehealth.ipf.commons.ihe.fhir.AbstractResourceProvider;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionOptions;

import java.util.Arrays;
import java.util.List;


/**
 * @author Christian Ohr
 * @since 3.5
 */
public enum Pcc44Options implements FhirTransactionOptions {

    Observations(ObservationResourceProvider.class),
    Allergies(AllergyIntoleranceResourceProvider.class),
    Conditions(ConditionResourceProvider.class),
    DiagnosticReports(DiagnosticReportResourceProvider.class),
    Medications(MedicationStatementResourceProvider.class, MedicationRequestResourceProvider.class),
    Immunizations(ImmunizationResourceProvider.class),
    Procedures(ProcedureResourceProvider.class),
    Encounters(EncounterResourceProvider.class);

    private List<Class<? extends AbstractResourceProvider>> resourceProviders;

    Pcc44Options(Class<? extends AbstractResourceProvider>... resourceProviders) {
        this.resourceProviders = Arrays.asList(resourceProviders);
    }

    @Override
    public List<Class<? extends AbstractResourceProvider>> getSupportedThings() {
        return resourceProviders;
    }


}
