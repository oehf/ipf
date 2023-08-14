/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq3;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Resource;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils;

import java.util.Map;

public class ChPpq3RequestFactory implements ClientRequestFactory<IClientExecutable<?, ?>> {

    @Override
    public IClientExecutable getClientExecutable(
            IGenericClient client,
            Object requestData,
            Map<String, Object> parameters)
    {
        String method = (String) parameters.get(Constants.HTTP_METHOD);
        switch (method) {
            case "POST":
                return client.create().resource((Resource) requestData);
            case "PUT":
                Consent consent = (Consent) requestData;
                String consentId = ChPpqmUtils.extractConsentId(consent, ChPpqmUtils.ConsentIdTypes.POLICY_SET_ID);
                return client.update()
                        .resource(consent)
                        .conditional()
                        .where(Consent.IDENTIFIER.exactly().identifier(consentId));
            case "DELETE":
                return client.delete()
                        .resourceConditionalByType(Consent.class)
                        .where(Consent.IDENTIFIER.exactly().identifier(ChPpqmUtils.extractResourceIdForDelete(requestData)));
            default:
                throw new RuntimeException("Unknown method: " + method);
        }
    }

}
