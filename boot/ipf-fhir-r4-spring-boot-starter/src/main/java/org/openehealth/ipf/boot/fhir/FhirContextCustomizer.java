/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.boot.fhir;

import ca.uhn.fhir.context.FhirContext;

/**
 * Customizer that can be used to manipulate auto-configured {@link FhirContext} beans,
 * e.g. to assign different HTTP client factories, Narrative generators, etc.
 *
 * @author Christian Ohr
 */
public interface FhirContextCustomizer {

    void customizeFhirContext(FhirContext fhirContext);

    FhirContextCustomizer NOOP = fhirContext -> {
    };
}
