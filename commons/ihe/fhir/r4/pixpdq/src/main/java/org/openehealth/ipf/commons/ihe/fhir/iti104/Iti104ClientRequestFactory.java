/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti104;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;

import java.util.Map;

/**
 * Request factory for the Patient Identity Feed FHIR [ITI-104] transaction. A Patient in the body is
 * sent as conditional update, anything else is taken as identifier of the patient to be removed and
 * sent as conditional delete. The identifier of the condition is determined as described in
 * {@link Iti104Utils#patientIdentifier(Object, Map)}.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class Iti104ClientRequestFactory implements ClientRequestFactory {

    @Override
    public IClientExecutable<?, MethodOutcome> getClientExecutable(
        IGenericClient client,
        Object requestData,
        Map<String, Object> parameters) {
        var identifier = Iti104Utils.patientIdentifier(requestData, parameters)
            .orElseThrow(() -> new IllegalArgumentException(
                "Could not determine the patient identifier of the ITI-104 request, set the "
                    + Iti104Constants.ITI104_PATIENT_IDENTIFIER_HEADER + " header"));
        var condition = Patient.IDENTIFIER.exactly().systemAndIdentifier(identifier.getSystem(), identifier.getValue());
        if (requestData instanceof Patient patient) {
            return client.update()
                .resource(patient)
                .conditional()
                .where(condition);
        }
        return client.delete()
            .resourceConditionalByType(Patient.class)
            .where(condition);
    }
}
