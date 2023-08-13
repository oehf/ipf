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

package org.openehealth.ipf.platform.camel.ihe.fhir.chppqm.chppq4;

import org.apache.camel.builder.RouteBuilder;
import org.hl7.fhir.r4.model.Bundle;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;

import java.util.UUID;

import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.*;

public class ChPpq4TestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("ch-ppq4:stub")
                .setHeader(ValidatorAdapter.NEED_VALIDATION_HEADER_NAME, constant(true))
                .setHeader(VALIDATION_MODE, constant(MODEL))
                .process(itiRequestValidator())
                .process(exchange -> {
                    Bundle request = exchange.getMessage().getMandatoryBody(Bundle.class);
                    Bundle response = new Bundle();
                    response.setId(UUID.randomUUID().toString());
                    response.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
                    for (Bundle.BundleEntryComponent requestEntry : request.getEntry()) {
                        Bundle.BundleEntryComponent responseEntry = new Bundle.BundleEntryComponent();
                        responseEntry.setFullUrl("http://ipf.ipf/fhir/Consent/" + UUID.randomUUID());
                        responseEntry.getResponse().setStatus("201 Created");
                        response.getEntry().add(responseEntry);
                    }
                    exchange.getMessage().setBody(response);
                })
                .process(itiResponseValidator());
    }

}
