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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetDocumentsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between a {@link GetDocumentsQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class GetDocumentsQueryTransformer extends GetByIDQueryTransformer<GetDocumentsQuery> {

    @Getter
    private static final GetDocumentsQueryTransformer instance = new GetDocumentsQueryTransformer();

    /**
     * Constructs the transformer.
     */
    private GetDocumentsQueryTransformer() {
        super(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID);
    }

    @Override
    protected void toEbXML(GetDocumentsQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromStringList(DOC_ENTRY_LOGICAL_ID, query.getLogicalUuid());
        slots.fromInteger(METADATA_LEVEL, query.getMetadataLevel());
    }

    @Override
    protected void fromEbXML(GetDocumentsQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setLogicalUuid(slots.toStringList(DOC_ENTRY_LOGICAL_ID));
        query.setMetadataLevel(slots.toInteger(METADATA_LEVEL));
    }

}
