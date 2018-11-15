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

package org.openehealth.ipf.platform.camel.ihe.fhir.core.custom;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

/**
 * @author Christian Ohr
 * @since 3.1
 */
public class CustomFhirComponent<AuditDatasetType extends FhirAuditDataset> extends FhirComponent<AuditDatasetType>
        implements FhirInteractionId<AuditDatasetType> {

    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private boolean query;
    @Getter @Setter
    private FhirTransactionConfiguration<AuditDatasetType> fhirTransactionConfiguration;

    public CustomFhirComponent() {
        super(null);
        setFhirInteractionId(this);
    }

    public CustomFhirComponent(FhirTransactionConfiguration<AuditDatasetType> fhirTransactionConfiguration) {
        this();
        this.fhirTransactionConfiguration = fhirTransactionConfiguration;
    }

    @Override
    protected FhirEndpoint<?, ?> doCreateEndpoint(String uri, FhirEndpointConfiguration<AuditDatasetType> config) {
        return new CustomFhirEndpoint(uri, this, config);
    }

}
