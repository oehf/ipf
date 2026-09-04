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
package org.openehealth.ipf.boot.ws;

import io.micrometer.observation.ObservationRegistry;
import org.apache.cxf.tracing.micrometer.ObservationClientFeature;
import org.apache.cxf.tracing.micrometer.ObservationFeature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Makes CXF's observation features available as beans, so that the SOAP calls of the IHE transactions
 * are observed. Micrometer observations yield both metrics and, if a tracing bridge is present, spans;
 * which of the two arise depends on what is registered with the {@link ObservationRegistry}, not on
 * these features. Attach them to the endpoints with the {@code features} endpoint option:
 * <pre>
 * from("xds-iti18:my-registry?features=#observationFeature")
 *     .to("xds-iti18://peer:8080/registry?features=#observationClientFeature");
 * </pre>
 * Where tracing is part of the observation, these features also establish and propagate the trace
 * context over HTTP; the spans that {@code camel-telemetry} creates for the transaction join that
 * context, which is why IPF itself does not propagate anything.
 * <p>
 * The features are deliberately <em>not</em> installed on the CXF bus, even though that would spare
 * the endpoint option: their interceptors tell client from server apart only by the CXF phase they run
 * in, so attaching them per endpoint is what keeps consumer and producer instrumentation separate.
 * <p>
 * Each transaction family starter subclasses this to bind it to its own {@code observing} property; the
 * beans themselves are shared, so enabling it for several families yields one set of features.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public abstract class CxfObservationConfigurationSupport {

    /**
     * @return the feature to attach to consumer endpoints, i.e. to those a route starts from.
     */
    @Bean
    @ConditionalOnMissingBean(name = "observationFeature")
    public ObservationFeature observationFeature(ObservationRegistry observationRegistry) {
        return new ObservationFeature(observationRegistry);
    }

    /**
     * @return the feature to attach to producer endpoints, i.e. to those a route sends to.
     */
    @Bean
    @ConditionalOnMissingBean(name = "observationClientFeature")
    public ObservationClientFeature observationClientFeature(ObservationRegistry observationRegistry) {
        return new ObservationClientFeature(observationRegistry);
    }
}
