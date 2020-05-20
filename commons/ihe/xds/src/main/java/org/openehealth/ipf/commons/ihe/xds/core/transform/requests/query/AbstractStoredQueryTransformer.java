/*
 * Copyright 2013 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.StoredQuery;

import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;

/**
 * Base transformations for all stored queries.
 * @author Dmytro Rud
 */
abstract class AbstractStoredQueryTransformer<T extends StoredQuery> {

    /**
     * Transforms the query into its ebXML representation.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>. 
     * @param query
     *          the query. Can be <code>null</code>.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     */
    public void toEbXML(T query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        ebXML.setId(query.getType().getId());
        ebXML.setHome(query.getHomeCommunityId());

        var slotHelper = new QuerySlotHelper(ebXML);
        for (var entry : query.getExtraParameters().entrySet()) {
            slotHelper.fromStringList(entry.getKey(), entry.getValue());
        }
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
    public void fromEbXML(T query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        query.setHomeCommunityId(ebXML.getHome());

        var slotHelper = new QuerySlotHelper(ebXML);
        for (var slot : ebXML.getSlots()) {
            var slotName = slot.getName();
            if ((QueryParameter.valueOfSlotName(slotName) == null) && (! query.getExtraParameters().containsKey(slotName))) {
                var queryList = slotHelper.toStringQueryList(slotName);
                if (queryList != null) {
                    query.getExtraParameters().put(slotName, queryList);
                }
            }
        }
    }
}
