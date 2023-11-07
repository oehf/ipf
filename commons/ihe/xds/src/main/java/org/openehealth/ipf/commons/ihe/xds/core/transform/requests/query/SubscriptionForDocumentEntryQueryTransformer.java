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

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SubscriptionForDocumentEntryQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_PATIENT_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_REFERENCE_IDS;

/**
 * Transforms between a {@link SubscriptionForDocumentEntryQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Christian Ohr
 */
public class SubscriptionForDocumentEntryQueryTransformer extends DocumentsQueryTransformer<SubscriptionForDocumentEntryQuery> {

    @Getter
    private static final SubscriptionForDocumentEntryQueryTransformer instance = new SubscriptionForDocumentEntryQueryTransformer();

    private SubscriptionForDocumentEntryQueryTransformer() {
    }

    @Override
    protected void toEbXML(SubscriptionForDocumentEntryQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromString(DOC_ENTRY_PATIENT_ID, Hl7v2Based.render(query.getPatientId()));
        slots.fromStringList(DOC_ENTRY_REFERENCE_IDS, query.getReferenceIds());
    }

    @Override
    protected void fromEbXML(SubscriptionForDocumentEntryQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        var patientId = slots.toString(DOC_ENTRY_PATIENT_ID);
        query.setPatientId(Hl7v2Based.parse(patientId, Identifiable.class));
        query.setReferenceIds(slots.toStringQueryList(DOC_ENTRY_REFERENCE_IDS));
    }
}
