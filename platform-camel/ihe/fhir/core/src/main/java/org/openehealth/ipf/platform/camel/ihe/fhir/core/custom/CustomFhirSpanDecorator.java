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
package org.openehealth.ipf.platform.camel.ihe.fhir.core.custom;

import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirSpanDecorator;

/**
 * Span decorator for the fhir endpoints, i.e. for FHIR transactions defined by the application rather
 * than by IPF. The metadata is taken from the transaction configuration the application supplies, so
 * such transactions are traced like the predefined ones.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class CustomFhirSpanDecorator extends FhirSpanDecorator {

    @Override
    public String getComponent() {
        return "fhir";
    }
}
