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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.query;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter.HOME;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetByUUIDQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;

/**
 * Base class of transformers for {@link GetByUUIDQuery}.
 * @param <T>
 *          type of the query.
 * @author Jens Riemschneider
 */
public abstract class GetByUUIDQueryTransformer<T extends GetByUUIDQuery> {
    private final QueryParameter uuidParam;
    
    protected GetByUUIDQueryTransformer(QueryParameter uuidParam) {
        notNull(uuidParam, "uuidParam cannot be null");
        this.uuidParam = uuidParam;
    }

    public void toEbXML(T query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        
        ebXML.setId(query.getType().getId());
        
        slots.fromStringList(uuidParam, query.getUUIDs());
        slots.fromString(HOME, query.getHomeCommunityID());

        toEbXML(query, slots);
    }

    public void fromEbXML(T query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        
        slots.toStringList(uuidParam, query.getUUIDs());
        query.setHomeCommunityID(slots.toString(HOME));

        fromEbXML(query, slots);
    }

    protected void toEbXML(T query, QuerySlotHelper slots) {}
    protected void fromEbXML(T query, QuerySlotHelper slots) {}
}