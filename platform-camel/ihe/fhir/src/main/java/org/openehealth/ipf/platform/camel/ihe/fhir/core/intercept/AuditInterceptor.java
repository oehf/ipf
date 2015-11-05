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
package org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept;

import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirAuditStrategy;

/**
 * Interface for FHIR auditing interceptors.
 *
 * @author Christian Ohr
 */
public interface AuditInterceptor<
        AuditDatasetType extends FhirAuditDataset>
        extends FhirInterceptor<AuditDatasetType> {

    /**
     * Returns the audit strategy instance configured for this interceptor.
     */
    FhirAuditStrategy<AuditDatasetType> getAuditStrategy();
}
