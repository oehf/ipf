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

package org.openehealth.ipf.platform.camel.ihe.fhir.chppqm.chppq5;

import org.apache.camel.builder.RouteBuilder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Consent;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq5.ChPpq5SearchParameters;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator.*;
import static org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator.createUuid;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.*;

public class ChPpq5TestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("ch-ppq5:stub")
                .setHeader(ValidatorAdapter.NEED_VALIDATION_HEADER_NAME, constant(true))
                .setHeader(VALIDATION_MODE, constant(MODEL))
                .process(itiRequestValidator())
                .process(exchange -> {
                    ChPpq5SearchParameters request = exchange.getMessage().getHeader(Constants.FHIR_REQUEST_PARAMETERS, ChPpq5SearchParameters.class);

                    List<Consent> consents = List.of(
                            create201Consent(createUuid(), "123456789012345678"),
                            create202Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:access-level:normal"),
                            create203Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:provide-level:restricted"));

                    consents.forEach(consent -> consent.setId(createUuid()));
                    exchange.getMessage().setBody(consents);
                })
                .process(itiResponseValidator());
    }

}
