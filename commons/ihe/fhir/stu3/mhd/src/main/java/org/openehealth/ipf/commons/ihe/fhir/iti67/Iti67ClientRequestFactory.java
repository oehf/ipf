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

package org.openehealth.ipf.commons.ihe.fhir.iti67;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.DocumentReference;
import org.openehealth.ipf.commons.ihe.fhir.QueryClientRequestFactory;

/**
 * Request Factory for ITI-67 requests returning a bundle of document references
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti67ClientRequestFactory extends QueryClientRequestFactory<Bundle> {

    public Iti67ClientRequestFactory() {
        super(DocumentReference.class, Bundle.class);
    }
}
