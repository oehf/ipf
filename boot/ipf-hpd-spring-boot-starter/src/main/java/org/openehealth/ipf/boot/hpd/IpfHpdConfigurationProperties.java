/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.boot.hpd;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@ConfigurationProperties(prefix = "ipf.hpd")
public class IpfHpdConfigurationProperties {


    /**
     * Whether CXF's observation features shall be provided as beans, so that the SOAP calls of this
     * transaction family can be observed -- yielding metrics, and spans if a tracing bridge is present.
     * Attach them to the endpoints with the {@code features} endpoint option:
     * <pre>
     * from("hpd-iti58:my-service?features=#observationFeature")
     *     .to("hpd-iti58://peer:8080/service?features=#observationClientFeature");
     * </pre>
     * Requires {@code cxf-integration-tracing-micrometer} on the classpath and an
     * {@code ObservationRegistry} bean, which Spring Boot provides.
     *
     * @see org.openehealth.ipf.boot.ws.CxfObservationConfigurationSupport
     */
    @Getter
    @Setter
    private boolean observing;
}
