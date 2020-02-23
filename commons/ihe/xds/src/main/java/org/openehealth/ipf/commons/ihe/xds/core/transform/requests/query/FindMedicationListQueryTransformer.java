/*
 * Copyright 2009 the original author or authors.
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

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp.toHL7;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindMedicationListQuery;

/**
 * Transforms between {@link FindMedicationListQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Quentin Ligier
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
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);

        slots.fromCode(DOC_ENTRY_FORMAT_CODE, query.getFormatCodes());
        slots.fromCode(DOC_ENTRY_TYPE_CODE, query.getTypeCodes());

        slots.fromNumber(DOC_ENTRY_SERVICE_START_FROM, toHL7(query.getServiceStart().getFrom()));
        slots.fromNumber(DOC_ENTRY_SERVICE_START_TO, toHL7(query.getServiceStart().getTo()));

        slots.fromNumber(DOC_ENTRY_SERVICE_END_FROM, toHL7(query.getServiceEnd().getFrom()));
        slots.fromNumber(DOC_ENTRY_SERVICE_END_TO, toHL7(query.getServiceEnd().getTo()));
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
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);

        query.setFormatCodes(slots.toCodeList(DOC_ENTRY_FORMAT_CODE));
        query.setTypeCodes(slots.toCodeList(DOC_ENTRY_TYPE_CODE));

        query.getServiceStart().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_START_FROM));
        query.getServiceStart().setTo(slots.toNumber(DOC_ENTRY_SERVICE_START_TO));

        query.getServiceEnd().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_END_FROM));
        query.getServiceEnd().setTo(slots.toNumber(DOC_ENTRY_SERVICE_END_TO));
    }
}
