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
package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.fhir.FhirObject;
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixmRequest;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirAuditStrategy;

/**
 *
 */
public abstract class Iti83AuditStrategy extends FhirAuditStrategy<Iti83AuditDataset> {

    protected Iti83AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public Iti83AuditDataset createAuditDataset() {
        return new Iti83AuditDataset(isServerSide());
    }

    @Override
    public Iti83AuditDataset enrichAuditDatasetFromRequest(Iti83AuditDataset auditDataset, FhirObject msg, Exchange exchange) {
        return auditDataset;
    }

    @Override
    public Iti83AuditDataset enrichAuditDatasetFromResponse(Iti83AuditDataset auditDataset, FhirObject msg) {
        return auditDataset;
    }

    

}
