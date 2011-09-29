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


import static org.apache.commons.lang3.Validate.notNull;

import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetByIdQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;

/**
 * Base class of transformers for {@link GetByIdQuery}.
 * @param <T>
 *          the actual query type that is transformed by an extending subclass.
 * @author Jens Riemschneider
 */
public abstract class GetByIDQueryTransformer<T extends GetByIdQuery> extends GetByUUIDQueryTransformer<T> {
    private final QueryParameter uniqueIdParam;

    /**
     * Constructs the transformer.
     * @param uuidParam
     *          the parameter name of the UUID parameter.
     * @param uniqueIdParam
     *          the parameter name of the unique ID parameter.
     */
    protected GetByIDQueryTransformer(QueryParameter uuidParam, QueryParameter uniqueIdParam) {
        super(uuidParam);
        notNull(uniqueIdParam, "uniqueIdParam cannot be null");
        this.uniqueIdParam = uniqueIdParam;
    }

    @Override
    protected void toEbXML(T query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromStringList(uniqueIdParam, query.getUniqueIds());
    }

    @Override
    protected void fromEbXML(T query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setUniqueIds(slots.toStringList(uniqueIdParam));
    }
}