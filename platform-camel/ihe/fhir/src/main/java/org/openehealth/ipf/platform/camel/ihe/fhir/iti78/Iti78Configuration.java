/*
 * Copyright 2015 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti78;

import org.openehealth.ipf.commons.ihe.fhir.iti78.Iti78QueryResourceClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.iti78.Iti78ResourceProvider;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponentConfiguration;

/**
 * Standard Configuration for Iti78Component
 *
 * @since 3.1
 */
public class Iti78Configuration extends FhirComponentConfiguration {

    public Iti78Configuration() {
        super(
                new Iti78ResourceProvider(),                    // Consumer side. accept patient searches
                new Iti78QueryResourceClientRequestFactory());  // Formulate queries
    }
}
