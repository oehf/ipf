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
package org.openehealth.ipf.platform.camel.ihe.xds.telemetry;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;

/**
 * An ITI-18 consumer with CXF's server side observation feature attached, observed with OpenTelemetry.
 *
 * @author Christian Ohr
 */
public class Iti18OtelObservationRouteBuilder extends RouteBuilder {

    public static final String SERVICE = "iti18-otel-observed";

    @Override
    public void configure() {
        from("xds-iti18:" + SERVICE + "?features=#observationFeature")
                .process(exchange -> {
                    // what is in scope while the consumer routes is the point of the test
                    OpenTelemetryObservations.recordConsumerTraceId(OpenTelemetryObservations.currentTraceId());
                    exchange.getMessage().setBody(SampleData.createQueryResponseWithLeafClass());
                });
    }
}
