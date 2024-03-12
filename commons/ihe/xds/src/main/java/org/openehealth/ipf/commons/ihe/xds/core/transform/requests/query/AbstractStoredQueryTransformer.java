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
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.StoredQuery;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;

/**
 * Base transformations for all stored queries.
 *
 * @author Dmytro Rud
 */
abstract class AbstractStoredQueryTransformer<T extends StoredQuery> {

    /**
     * Transforms the query into its ebXML representation.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>.
     *
     * @param query the query. Can be <code>null</code>.
     * @param ebXML the ebXML representation. Can be <code>null</code>.
     */
    public void toEbXML(T query, EbXMLAdhocQueryRequest<AdhocQueryRequest> ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        ebXML.setId(query.getType().getId());
        ebXML.setHome(query.getHomeCommunityId());

        toEbXML(query, new QuerySlotHelper(ebXML));
    }


    /**
     * Transforms the ebXML representation of a query into a query object.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>.
     *
     * @param query the query. Can be <code>null</code>.
     * @param ebXML the ebXML representation. Can be <code>null</code>.
     */
    public void fromEbXML(T query, EbXMLAdhocQueryRequest<AdhocQueryRequest> ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        query.setHomeCommunityId(ebXML.getHome());
        fromEbXML(query, new QuerySlotHelper(ebXML));
    }

    /**
     * Called by {@link #toEbXML(StoredQuery, EbXMLAdhocQueryRequest)} to
     * transform slots.
     *
     * @param query the query to transform.
     * @param slots the slots to be filled.
     */
    protected void toEbXML(T query, QuerySlotHelper slots) {
        query.getExtraParameters().forEach(slots::fromStringList);
    }

    /**
     * Called by {@link #fromEbXML(StoredQuery, EbXMLAdhocQueryRequest)} to
     * transform slots.
     *
     * @param query the target query.
     * @param slots the slots to transform.
     */
    protected void fromEbXML(T query, QuerySlotHelper slots) {
        slots.getSlots().stream()
                .map(EbXMLSlot::getName)
                .filter(slotName -> (QueryParameter.valueOfSlotName(slotName) == null) && (!query.getExtraParameters().containsKey(slotName)))
                .forEach(slotName -> {
                    var queryList = slots.toStringQueryList(slotName);
                    if (queryList != null) {
                        query.getExtraParameters().put(slotName, queryList);
                    }
                });
    }
}
