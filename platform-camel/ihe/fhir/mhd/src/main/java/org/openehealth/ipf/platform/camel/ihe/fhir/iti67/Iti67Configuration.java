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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti67;

import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67ResourceProvider;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponentConfiguration;

/**
 * Standard Configuration for Iti67Component.  Supports lazy-loading by default.
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class Iti67Configuration extends FhirComponentConfiguration {

    public Iti67Configuration() {
        super(
                new Iti67ResourceProvider(),                    // Consumer side. accept registrations
                new Iti67ClientRequestFactory());               // Formulate requests
        setSupportsLazyLoading(true);
    }
}
