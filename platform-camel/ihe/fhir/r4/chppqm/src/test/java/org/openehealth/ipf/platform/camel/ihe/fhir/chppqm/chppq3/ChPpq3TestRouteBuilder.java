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

package org.openehealth.ipf.platform.camel.ihe.fhir.chppqm.chppq3;

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.apache.camel.builder.RouteBuilder;
import org.hl7.fhir.r4.model.IdType;
import static org.junit.jupiter.api.Assertions.*;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.*;

public class ChPpq3TestRouteBuilder extends RouteBuilder {

    public static final String TRACE_CONTEXT_ID = "00-0af7651916cd43dd8448eb211c80319c-1111111111111111-01";

    @Override
    public void configure() throws Exception {
        from("ch-ppq3:stub")
                .setHeader(ValidatorAdapter.NEED_VALIDATION_HEADER_NAME, constant(true))
                .setHeader(VALIDATION_MODE, constant(MODEL))
                .process(itiRequestValidator())
                .process(exchange -> {
                    Map<String, List<String>> httpHeaders = (Map<String, List<String>>) exchange.getMessage().getHeader(Constants.HTTP_INCOMING_HEADERS);
                    assertEquals(1, httpHeaders.get("Authorization").size());
                    assertEquals(3, httpHeaders.get("Header2").size());

                    //Consent request = exchange.getMessage().getMandatoryBody(Consent.class);
                    log.info("Method = {}", exchange.getMessage().getHeader(Constants.HTTP_METHOD));
                    exchange.getMessage().setBody(new MethodOutcome(new IdType(UUID.randomUUID().toString())));
                    exchange.getMessage().setHeader(Constants.HTTP_OUTGOING_HEADERS, Map.of("TraceParent", List.of(TRACE_CONTEXT_ID)));
                })
                .process(itiResponseValidator());
    }

}
