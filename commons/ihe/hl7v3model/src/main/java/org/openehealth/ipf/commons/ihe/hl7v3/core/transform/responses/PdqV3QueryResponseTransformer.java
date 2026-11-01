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

import net.ihe.gazelle.hl7v3.datatypes.INT;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PdqV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformer;

import java.util.Optional;

/**
 * Transformer between objects {@link PdqV3QueryResponse} and {@link PRPAIN201306UV02Type} (PDQv3, ITI-47).
 *
 * @author Christian Ohr
 * @since 6.1
 */
public class PdqV3QueryResponseTransformer extends AbstractPatientDemographicsQueryResponseTransformer<PdqV3QueryResponse> {

    public PdqV3QueryResponseTransformer() {
        super(new PdqV3QueryRequestTransformer());
    }

    @Override
    protected PdqV3QueryResponse newSimpleResponse() {
        return new PdqV3QueryResponse();
    }

    @Override
    protected void toPrpaSpecific(final PdqV3QueryResponse simpleResponse, final PRPAIN201306UV02Type response) {
        final var queryAck = response.getControlActProcess().getQueryAck();
        if (simpleResponse.getResultTotalQuantity() != null) {
            queryAck.setResultTotalQuantity(new INT(simpleResponse.getResultTotalQuantity()));
        }
        if (simpleResponse.getResultCurrentQuantity() != null) {
            queryAck.setResultCurrentQuantity(new INT(simpleResponse.getResultCurrentQuantity()));
        }
        if (simpleResponse.getResultRemainingQuantity() != null) {
            queryAck.setResultRemainingQuantity(new INT(simpleResponse.getResultRemainingQuantity()));
        }
    }

    @Override
    protected void fromPrpaSpecific(final PRPAIN201306UV02Type response, final PdqV3QueryResponse simpleResponse) {
        Optional.ofNullable(response.getControlActProcess())
                .map(controlActProcess -> controlActProcess.getQueryAck())
                .ifPresent(queryAck -> {
                    simpleResponse.setResultTotalQuantity(intValue(queryAck.getResultTotalQuantity()));
                    simpleResponse.setResultCurrentQuantity(intValue(queryAck.getResultCurrentQuantity()));
                    simpleResponse.setResultRemainingQuantity(intValue(queryAck.getResultRemainingQuantity()));
                });
    }

    private static Integer intValue(final INT value) {
        return value != null ? value.getValue() : null;
    }
}
