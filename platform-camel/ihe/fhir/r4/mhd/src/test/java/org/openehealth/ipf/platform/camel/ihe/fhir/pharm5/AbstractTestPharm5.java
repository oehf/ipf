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
package org.openehealth.ipf.platform.camel.ihe.fhir.pharm5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Bundle;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.pharm5.Pharm5ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.pharm5.Pharm5Operations;
import org.openehealth.ipf.commons.ihe.fhir.pharm5.Pharm5SearchParameters;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import java.util.HashMap;

/**
 *
 */
abstract class AbstractTestPharm5 extends FhirTestContainer {


    public static void startServer(String contextDescriptor) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, "FhirServlet");
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT));
    }

    protected Pharm5SearchParameters findMedicationTreatmentPlanParameters() {
        return new Pharm5SearchParameters(
                new TokenParam("urn:oid:1.2.3", "1234"),
                null,
                null,
                null,
                null,
                new TokenOrListParam(null, "current"),
                null,
                null,
                null,
                null,
                null,
                null,
                Pharm5Operations.FIND_MEDICATION_TREATMENT_PLANS,
                null,
                null,
                FhirContext.forR4()
        );
    }

    protected Pharm5SearchParameters findMedicationListParameters() {
        return new Pharm5SearchParameters(
                new TokenParam("urn:oid:1.2.3", "abc"),
                null,
                null,
                null,
                null,
                new TokenOrListParam(null, "current"),
                null,
                null,
                null,
                null,
                null,
                null,
                Pharm5Operations.FIND_MEDICATION_LIST,
                null,
                null,
                FhirContext.forR4()
        );
    }

    protected Bundle sendManually(Pharm5SearchParameters requestData) {
        final var inParams = new HashMap<String, Object>(1);
        inParams.put(Constants.FHIR_REQUEST_PARAMETERS, requestData);
        return (new Pharm5ClientRequestFactory()).getClientExecutable(client, null, inParams)
                .encodedXml()
                .execute();
    }

    protected Bundle nextPage(Bundle bundle) {
        return client.loadPage()
                .next(bundle)
                .execute();
    }

    protected Bundle previousPage(Bundle bundle) {
        return client.loadPage()
                .previous(bundle)
                .execute();
    }


}
