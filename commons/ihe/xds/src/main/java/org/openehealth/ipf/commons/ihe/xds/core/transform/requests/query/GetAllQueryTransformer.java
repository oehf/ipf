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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetAllQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between a {@link GetAllQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class GetAllQueryTransformer extends AbstractStoredQueryTransformer<GetAllQuery> {

    @Getter
    private static final GetAllQueryTransformer instance = new GetAllQueryTransformer();

    private GetAllQueryTransformer() {
    }

    @Override
    protected void toEbXML(GetAllQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromString(PATIENT_ID, Hl7v2Based.render(query.getPatientId()));
        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatusDocuments());
        slots.fromStatus(SUBMISSION_SET_STATUS, query.getStatusSubmissionSets());
        slots.fromStatus(FOLDER_STATUS, query.getStatusFolders());
        slots.fromCode(DOC_ENTRY_FORMAT_CODE, query.getFormatCodes());
        slots.fromCode(DOC_ENTRY_CONFIDENTIALITY_CODE, query.getConfidentialityCodes());
        slots.fromDocumentEntryType(DOC_ENTRY_TYPE, query.getDocumentEntryTypes());
        slots.fromStatus(ASSOCIATION_STATUS, query.getAssociationStatuses());
        slots.fromInteger(METADATA_LEVEL, query.getMetadataLevel());
    }

    @Override
    protected void fromEbXML(GetAllQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        var patientId = slots.toString(PATIENT_ID);
        query.setPatientId(Identifiable.parse(patientId));
        query.setStatusDocuments(slots.toStatus(DOC_ENTRY_STATUS));
        query.setStatusFolders(slots.toStatus(FOLDER_STATUS));
        query.setStatusSubmissionSets(slots.toStatus(SUBMISSION_SET_STATUS));
        query.setConfidentialityCodes(slots.toCodeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME));
        query.setFormatCodes(slots.toCodeList(DOC_ENTRY_FORMAT_CODE));
        query.setDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE));
        query.setAssociationStatuses(slots.toStatus(ASSOCIATION_STATUS));
        query.setMetadataLevel(slots.toInteger(METADATA_LEVEL));
    }
}
