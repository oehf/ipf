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

package org.openehealth.ipf.platform.camel.ihe.fhir.core.custom;

import org.apache.camel.spi.UriEndpoint;
import org.openehealth.ipf.commons.ihe.fhir.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirConsumer;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

/**
 *
 */
@UriEndpoint(scheme = "fhir", title = "Generic FHIR", syntax = "fhir:host:port", consumerClass = FhirConsumer.class, label = "http")
public class CustomFhirEndpoint<AuditDatasetType extends FhirAuditDataset>
        extends FhirEndpoint<AuditDatasetType, CustomFhirComponent<AuditDatasetType>> {

    public CustomFhirEndpoint(String uri, CustomFhirComponent<AuditDatasetType> fhirComponent, FhirEndpointConfiguration<AuditDatasetType> config) {
        super(uri, fhirComponent, config);
    }

}
