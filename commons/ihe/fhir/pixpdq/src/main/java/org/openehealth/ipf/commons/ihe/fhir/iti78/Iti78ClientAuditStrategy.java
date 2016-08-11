/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti78;

import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditDataset;

import java.util.stream.Collectors;

/**
 * Strategy for auditing ITI-78 transactions on the client side
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class Iti78ClientAuditStrategy extends Iti78AuditStrategy {

    private static class LazyHolder {
        private static final Iti78ClientAuditStrategy INSTANCE = new Iti78ClientAuditStrategy();
    }

    public static Iti78ClientAuditStrategy getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Iti78ClientAuditStrategy() {
        super(false);
    }

    /**
     * Enrich patients IDs. Response is either a Bundle or a Patient
     *
     * @param auditDataset audit dataset
     * @param response response resource
     * @return audit dataset enriched with patient resource IDs
     */
    @Override
    public boolean enrichAuditDatasetFromResponse(FhirQueryAuditDataset auditDataset, Object response) {
        boolean result = super.enrichAuditDatasetFromResponse(auditDataset, response);
        /* Pending https://github.com/oehf/ipf/issues/124
        if (result) {
            if (response instanceof Patient) {
                Patient patient = (Patient) response;
                auditDataset.getPatientIds().add(patient.getIdElement().getValue());
            } else if (response instanceof Bundle) {
                Bundle bundle = (Bundle) response;
                auditDataset.getPatientIds().addAll(
                        bundle.getEntry().stream()
                                .map(Bundle.BundleEntryComponent::getResource)
                                .map(r -> r.getIdElement().getValue())
                                .collect(Collectors.toList()));
            }
        }
        */
        return result;
    }
}
