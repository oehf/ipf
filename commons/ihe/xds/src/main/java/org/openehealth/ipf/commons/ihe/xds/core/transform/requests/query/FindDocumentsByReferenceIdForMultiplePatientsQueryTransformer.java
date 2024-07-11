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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsByReferenceIdForMultiplePatientsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between a {@link FindDocumentsByReferenceIdForMultiplePatientsQuery} and {@link EbXMLAdhocQueryRequest}.
 */
public class FindDocumentsByReferenceIdForMultiplePatientsQueryTransformer extends DocumentsQueryTransformer<FindDocumentsByReferenceIdForMultiplePatientsQuery> {

    @Getter
    private static final FindDocumentsByReferenceIdForMultiplePatientsQueryTransformer instance = new FindDocumentsByReferenceIdForMultiplePatientsQueryTransformer();

    @Override
    protected void toEbXML(FindDocumentsByReferenceIdForMultiplePatientsQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromPatientIdList(DOC_ENTRY_PATIENT_ID, query.getPatientIds());
        slots.fromDocumentEntryType(DOC_ENTRY_TYPE, query.getDocumentEntryTypes());
        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatus());
        slots.fromStringList(DOC_ENTRY_REFERENCE_IDS, query.getReferenceIds());
    }

    @Override
    protected void fromEbXML(FindDocumentsByReferenceIdForMultiplePatientsQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setPatientIds(slots.toPatientIdList(DOC_ENTRY_PATIENT_ID));
        query.setDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE));
        query.setStatus(slots.toStatus(DOC_ENTRY_STATUS));
        query.setReferenceIds(slots.toStringQueryList(DOC_ENTRY_REFERENCE_IDS));
    }
}
