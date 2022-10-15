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

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindMedicationListQuery;

/**
 * Transforms between {@link FindMedicationListQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Quentin Ligier
 * @since 3.7
 */
public class FindMedicationListQueryTransformer extends PharmacyDocumentsQueryTransformer<FindMedicationListQuery> {

    /**
     * Transforms the query into its EbXML representation.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>.
     * @param query
     *          the query to transform.
     * @param ebXML
     *          the EbXML representation.
     */
    @Override
    public void toEbXML(FindMedicationListQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.toEbXML(query, ebXML);
        var slots = new QuerySlotHelper(ebXML);

        slots.fromCode(DOC_ENTRY_FORMAT_CODE, query.getFormatCodes());
        slots.fromDocumentEntryType(DOC_ENTRY_TYPE, query.getDocumentEntryTypes());

        slots.fromTimestamp(DOC_ENTRY_SERVICE_START_FROM, query.getServiceStart().getFrom());
        slots.fromTimestamp(DOC_ENTRY_SERVICE_START_TO, query.getServiceStart().getTo());

        slots.fromTimestamp(DOC_ENTRY_SERVICE_END_FROM, query.getServiceEnd().getFrom());
        slots.fromTimestamp(DOC_ENTRY_SERVICE_END_TO, query.getServiceEnd().getTo());
    }

    /**
     * Transforms the ebXML representation of a query into a query object.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>.
     * @param query
     *          the query. Can be <code>null</code>.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     */
    @Override
    public void fromEbXML(FindMedicationListQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.fromEbXML(query, ebXML);
        var slots = new QuerySlotHelper(ebXML);

        query.setFormatCodes(slots.toCodeList(DOC_ENTRY_FORMAT_CODE));
        query.setDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE));

        query.getServiceStart().setFrom(slots.toTimestamp(DOC_ENTRY_SERVICE_START_FROM));
        query.getServiceStart().setTo(slots.toTimestamp(DOC_ENTRY_SERVICE_START_TO));

        query.getServiceEnd().setFrom(slots.toTimestamp(DOC_ENTRY_SERVICE_END_FROM));
        query.getServiceEnd().setTo(slots.toTimestamp(DOC_ENTRY_SERVICE_END_TO));
    }
}
