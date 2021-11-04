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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
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

    /**
     * Transforms the query into its ebXML representation.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>.
     * @param query
     *          the query. Can be <code>null</code>.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     */
    @Override
    public void toEbXML(T query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.toEbXML(query, ebXML);
        var slots = new QuerySlotHelper(ebXML);

        slots.fromInteger(METADATA_LEVEL, query.getMetadataLevel());
        slots.fromString(DOC_ENTRY_PATIENT_ID, Hl7v2Based.render(query.getPatientId()));
        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatus());
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
    public void fromEbXML(T query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.fromEbXML(query, ebXML);
        var slots = new QuerySlotHelper(ebXML);

        query.setMetadataLevel(slots.toInteger(METADATA_LEVEL));
        query.setPatientId(Hl7v2Based.parse(slots.toString(DOC_ENTRY_PATIENT_ID), Identifiable.class));
        query.setStatus(slots.toStatus(DOC_ENTRY_STATUS));
    }
}
