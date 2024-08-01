/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.audit;

import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.util.Map;

/**
 * Interface for FHIR ATNA audit dataset enrichers.
 * Each implementing class shall have a default constructor and be thread-safe.
 *
 * @author Dmytro Rud
 */
public interface FhirAuditDatasetEnricher extends org.openehealth.ipf.commons.audit.FhirAuditDatasetEnricher {

    void enrichAuditDataset(AuditDataset auditDataset, Object request, Map<String, Object> parameters);

}
