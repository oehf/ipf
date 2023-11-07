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

import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetByUuidQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;

import static java.util.Objects.requireNonNull;

/**
 * Base class of transformers for {@link GetByUuidQuery}.
 *
 * @param <T> type of the query.
 * @author Jens Riemschneider
 */
public abstract class GetByUUIDQueryTransformer<T extends GetByUuidQuery> extends AbstractStoredQueryTransformer<T> {
    private final QueryParameter uuidParam;

    /**
     * Constructs the transformer.
     *
     * @param uuidParam the parameter name of the UUID parameter.
     */
    protected GetByUUIDQueryTransformer(QueryParameter uuidParam) {
        this.uuidParam = requireNonNull(uuidParam, "uuidParam cannot be null");
    }

    /**
     * Transforms the query into its ebXML representation.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>.
     * @param query
     *          the query. Can be <code>null</code>.
     * @param slots
     *          the ebXML representation. Can be <code>null</code>.
     */
    @Override
    protected void toEbXML(T query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromStringList(uuidParam, query.getUuids());
    }

    /**
     * Transforms the ebXML representation of a query into a query object.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>. 
     * @param query
     *          the query. Can be <code>null</code>.
     * @param slots
     *          the ebXML representation. Can be <code>null</code>.
     */
    @Override
    protected void fromEbXML(T query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setUuids(slots.toStringList(uuidParam));
    }
}
