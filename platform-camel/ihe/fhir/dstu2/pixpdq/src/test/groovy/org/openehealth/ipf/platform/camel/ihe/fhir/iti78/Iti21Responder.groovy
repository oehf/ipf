/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti78

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.Message
import org.apache.camel.Exchange
import org.apache.camel.support.ExpressionAdapter
import org.openehealth.ipf.modules.hl7.HL7v2Exception

/**
 *
 */
class Iti21Responder extends ExpressionAdapter {

    private final ResponseCase responseCase

    Iti21Responder(ResponseCase responseCase) {
        this.responseCase = responseCase
    }

    @Override
    Object evaluate(Exchange exchange) {
        try {
            Message request = exchange.getIn().getBody(Message.class)
            return responseCase.populateResponse(request)
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e)
        }

    }
}
