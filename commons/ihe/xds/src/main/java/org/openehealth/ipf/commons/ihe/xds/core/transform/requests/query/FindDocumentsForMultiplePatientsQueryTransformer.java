/*
 * Copyright 2012 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsForMultiplePatientsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between a {@link FindDocumentsForMultiplePatientsQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Michael Ottati
 */
public class FindDocumentsForMultiplePatientsQueryTransformer extends DocumentsQueryTransformer<FindDocumentsForMultiplePatientsQuery> {

    @Override
    public void toEbXML(FindDocumentsForMultiplePatientsQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.toEbXML(query, ebXML);

        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        slots.fromPatientIdList(DOC_ENTRY_PATIENT_ID, query.getPatientIds());
        slots.fromDocumentEntryType(DOC_ENTRY_TYPE, query.getDocumentEntryTypes());
        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatus());
    }


    public void fromEbXML(FindDocumentsForMultiplePatientsQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.fromEbXML(query, ebXML);
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);

        query.setPatientIds(slots.toPatientIdList(DOC_ENTRY_PATIENT_ID));
        query.setDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE));
        query.setStatus(slots.toStatus(DOC_ENTRY_STATUS));
    }
}
