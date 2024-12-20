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

package org.openehealth.ipf.commons.ihe.fhir.iti81;

import ca.uhn.fhir.rest.gclient.ICriterion;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.Bundle;
import org.openehealth.ipf.commons.ihe.fhir.QueryClientRequestFactory;

/**
 * Request Factory for ITI-81 requests returning a bundle of audit events based on query criteria of type
 * {@link ICriterion} or String in the request data
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class Iti81ClientRequestFactory extends QueryClientRequestFactory<Bundle> {

    public Iti81ClientRequestFactory() {
        super(AuditEvent.class, Bundle.class);
    }

}
