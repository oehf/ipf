/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.PharmacyDocumentsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Base transformations for all PHARM-1 queries.
 * @author Quentin Ligier
 * @since 3.7
 */
abstract class PharmacyDocumentsQueryTransformer<T extends PharmacyDocumentsQuery> extends AbstractStoredQueryTransformer<T> {

    @Override
    protected void toEbXML(T query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromInteger(METADATA_LEVEL, query.getMetadataLevel());
        slots.fromString(DOC_ENTRY_PATIENT_ID, Hl7v2Based.render(query.getPatientId()));
        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatus());
    }

    @Override
    protected void fromEbXML(T query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setMetadataLevel(slots.toInteger(METADATA_LEVEL));
        query.setPatientId(Identifiable.parse(slots.toString(DOC_ENTRY_PATIENT_ID)));
        query.setStatus(slots.toStatus(DOC_ENTRY_STATUS));
    }
}
