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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti65;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.ExpressionAdapter;
import org.hl7.fhir.instance.model.Bundle;
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirTestContainer;

/**
 *
 */
public class Iti65TestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:input")
                .toF("mhd-iti65:localhost:%d", FhirTestContainer.DEMO_APP_PORT);

        from("mhd-iti65:stub?audit=true")
                .errorHandler(noErrorHandler())
                .transform(new AcceptExpression());
    }


    private class AcceptExpression extends ExpressionAdapter {

        @Override
        public Object evaluate(Exchange exchange) {
            Bundle requestBundle = exchange.getIn().getBody(Bundle.class);

            Bundle responseBundle = new Bundle().setType(Bundle.BundleType.TRANSACTIONRESPONSE);

            for (Bundle.BundleEntryComponent requestEntry : requestBundle.getEntry()) {

            }

            return responseBundle;
        }

    }


}
