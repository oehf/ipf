/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.responses;

import net.ihe.gazelle.hl7v3.datatypes.CE;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.MatchedPatient;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.XcpdQueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.XcpdQueryRequestTransformer;

/**
 * Transformer between objects {@link XcpdQueryResponse} and {@link PRPAIN201306UV02Type} (XCPD, ITI-55).
 *
 * @author Christian Ohr
 * @since 6.1
 */
public class XcpdQueryResponseTransformer extends AbstractPatientDemographicsQueryResponseTransformer<XcpdQueryResponse> {

    /**
     * Code system of the Health Data Locator flag in {@code registrationEvent/custodian/assignedEntity/code}.
     */
    public static final String HEALTH_DATA_LOCATOR_CODE_SYSTEM = "1.3.6.1.4.1.19376.1.2.27.2";

    public XcpdQueryResponseTransformer() {
        super(new XcpdQueryRequestTransformer());
    }

    @Override
    protected XcpdQueryResponse newSimpleResponse() {
        return new XcpdQueryResponse();
    }

    @Override
    protected void toPrpaSpecific(final XcpdQueryResponse simpleResponse, final PRPAIN201306UV02Type response) {
        response.getControlActProcess().getReasonOf().addAll(simpleResponse.getReasons());
    }

    @Override
    protected void fromPrpaSpecific(final PRPAIN201306UV02Type response, final XcpdQueryResponse simpleResponse) {
        if (response.getControlActProcess() != null) {
            simpleResponse.getReasons().addAll(response.getControlActProcess().getReasonOf());
        }
    }

    /**
     * XCPD requires the custodian code (Health Data Locator flag); defaults to {@code NotHealthDataLocator}.
     */
    @Override
    protected CE custodianCode(final MatchedPatient patient) {
        return patient.getCustodianCode() != null
                ? patient.getCustodianCode()
                : new CE("NotHealthDataLocator", null, HEALTH_DATA_LOCATOR_CODE_SYSTEM);
    }
}
