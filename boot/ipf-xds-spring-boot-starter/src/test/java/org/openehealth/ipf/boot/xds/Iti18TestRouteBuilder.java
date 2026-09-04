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

package org.openehealth.ipf.boot.xds;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.springframework.stereotype.Component;

/**
 * An ITI-18 consumer, i.e. a route as a user of this starter would write it. Whether it can be started
 * at all is what the test is about: it requires the XDS Camel component to be on the classpath, the CXF
 * bus of the Spring Boot application to publish the web service, and the audit context contributed by
 * the ATNA starter to be present.
 */
@Component
public class Iti18TestRouteBuilder extends RouteBuilder {

    public static final String SERVICE = "xds-iti18-service";

    @Override
    public void configure() {
        from("xds-iti18:" + SERVICE)
                .process(exchange -> exchange.getMessage().setBody(SampleData.createQueryResponseWithLeafClass()));
    }
}
