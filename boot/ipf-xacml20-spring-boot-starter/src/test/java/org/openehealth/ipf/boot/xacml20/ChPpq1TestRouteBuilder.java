/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.boot.xacml20;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.model.EprConstants.StatusCode;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse;
import org.springframework.stereotype.Component;

/**
 * A CH:PPQ-1 consumer, i.e. a route as a user of this starter would write it. Whether it can be started
 * at all is what the test is about: it requires the XACML 2.0 Camel component to be on the classpath, the
 * CXF bus of the Spring Boot application to publish the web service, and the audit context contributed by
 * the ATNA starter to be present.
 */
@Component
public class ChPpq1TestRouteBuilder extends RouteBuilder {

    public static final String SERVICE = "ch-ppq1-service";

    @Override
    public void configure() {
        from("ch-ppq1:" + SERVICE)
                .process(exchange -> {
                    var response = new EprPolicyRepositoryResponse();
                    response.setStatus(StatusCode.SUCCESS);
                    exchange.getMessage().setBody(response);
                });
    }
}
