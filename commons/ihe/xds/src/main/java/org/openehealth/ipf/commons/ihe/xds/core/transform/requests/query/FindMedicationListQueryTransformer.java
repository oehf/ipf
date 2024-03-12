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

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindMedicationListQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between {@link FindMedicationListQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Quentin Ligier
 * @since 3.7
 */
public class FindMedicationListQueryTransformer extends PharmacyDocumentsQueryTransformer<FindMedicationListQuery> {

    @Getter
    private static final FindMedicationListQueryTransformer instance = new FindMedicationListQueryTransformer();

    private FindMedicationListQueryTransformer() {
    }

    @Override
    protected void toEbXML(FindMedicationListQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromCode(DOC_ENTRY_FORMAT_CODE, query.getFormatCodes());
        slots.fromDocumentEntryType(DOC_ENTRY_TYPE, query.getDocumentEntryTypes());
        slots.fromTimestamp(DOC_ENTRY_SERVICE_START_FROM, query.getServiceStart().getFrom());
        slots.fromTimestamp(DOC_ENTRY_SERVICE_START_TO, query.getServiceStart().getTo());
        slots.fromTimestamp(DOC_ENTRY_SERVICE_END_FROM, query.getServiceEnd().getFrom());
        slots.fromTimestamp(DOC_ENTRY_SERVICE_END_TO, query.getServiceEnd().getTo());
    }

    @Override
    protected void fromEbXML(FindMedicationListQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setFormatCodes(slots.toCodeList(DOC_ENTRY_FORMAT_CODE));
        query.setDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE));
        query.getServiceStart().setFrom(slots.toTimestamp(DOC_ENTRY_SERVICE_START_FROM));
        query.getServiceStart().setTo(slots.toTimestamp(DOC_ENTRY_SERVICE_START_TO));
        query.getServiceEnd().setFrom(slots.toTimestamp(DOC_ENTRY_SERVICE_END_FROM));
        query.getServiceEnd().setTo(slots.toTimestamp(DOC_ENTRY_SERVICE_END_TO));
    }
}
