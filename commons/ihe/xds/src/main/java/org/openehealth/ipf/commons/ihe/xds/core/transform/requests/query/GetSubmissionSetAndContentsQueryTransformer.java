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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetSubmissionSetAndContentsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between a {@link GetSubmissionSetAndContentsQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class GetSubmissionSetAndContentsQueryTransformer extends GetByIDAndCodesQueryTransformer<GetSubmissionSetAndContentsQuery> {

    @Getter
    private static final GetSubmissionSetAndContentsQueryTransformer instance = new GetSubmissionSetAndContentsQueryTransformer();

    /**
     * Constructs the transformer.
     */
    private GetSubmissionSetAndContentsQueryTransformer() {
        super(SUBMISSION_SET_UUID, 
                SUBMISSION_SET_UNIQUE_ID, 
                DOC_ENTRY_FORMAT_CODE, 
                DOC_ENTRY_FORMAT_CODE_SCHEME, 
                DOC_ENTRY_CONFIDENTIALITY_CODE,
                DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME);
    }

    @Override
    protected void toEbXML(GetSubmissionSetAndContentsQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromDocumentEntryType(DOC_ENTRY_TYPE, query.getDocumentEntryTypes());
        slots.fromInteger(METADATA_LEVEL, query.getMetadataLevel());
    }

    @Override
    protected void fromEbXML(GetSubmissionSetAndContentsQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE));
        query.setMetadataLevel(slots.toInteger(METADATA_LEVEL));
    }
}
