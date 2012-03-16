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

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetRelatedDocumentsQuery;

/**
 * Transforms between a {@link GetDocumentsQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class GetRelatedDocumentsQueryTransformer extends GetFromDocumentQueryTransformer<GetRelatedDocumentsQuery> {
    @Override
    public void toEbXML(GetRelatedDocumentsQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        super.toEbXML(query, ebXML);
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        slots.fromAssociationType(ASSOCIATION_TYPE, query.getAssociationTypes());
        slots.fromDocumentEntryType(DOC_ENTRY_TYPE, query.getDocumentEntryTypes());
    }
    
    @Override
    public void fromEbXML(GetRelatedDocumentsQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        super.fromEbXML(query, ebXML);
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);        
        query.setAssociationTypes(slots.toAssociationType(ASSOCIATION_TYPE));
        query.setDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE));
    }
}
