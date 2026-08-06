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

package org.openehealth.ipf.boot.hl7v3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@ConfigurationProperties(prefix = "ipf.hl7v3")
public class IpfHl7v3ConfigurationProperties {

    @Getter
    @Setter
    private boolean caching;


    /**
     * Whether the CXF interceptors that create observations for the SOAP calls of this transaction
     * family shall be made available as Spring beans, so that they can be attached to endpoints with
     * the {@code features} endpoint option:
     * <pre>
     * from("pixv3-iti44:my-service?features=#observationFeature")
     *     .to("pixv3-iti44://peer:8080/service?features=#observationClientFeature");
     * </pre>
     * Requires {@code cxf-integration-tracing-micrometer} on the classpath and an
     * {@link io.micrometer.observation.ObservationRegistry} bean, which Spring Boot provides.
     * <p>
     * Trace context is propagated by these interceptors, not by IPF: the spans that
     * {@code camel-telemetry} creates for the transaction join the context they establish.
     */
    @Getter
    @Setter
    private boolean observing;
}
