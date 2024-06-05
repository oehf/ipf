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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti119;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

/**
 *
 */
public class Iti119TestRouteBuilder extends RouteBuilder {

    private ResponseCase responseCase = ResponseCase.OK;

    public void setResponse(ResponseCase responseCase) {
        this.responseCase = responseCase;
    }

    @Override
    public void configure() {

        from("direct:input")
                .toF("pdqm-iti119:localhost:%d?fhirContext=#fhirContext", FhirTestContainer.DEMO_APP_PORT);

        from("pdqm-iti119:translation?audit=true&fhirContext=#fhirContext")
                .errorHandler(noErrorHandler())
                .transform(new Iti119Responder(responseCase));
    }


}
