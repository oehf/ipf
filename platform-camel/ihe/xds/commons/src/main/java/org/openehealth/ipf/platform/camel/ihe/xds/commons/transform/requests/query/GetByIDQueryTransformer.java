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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetByIDQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;

/**
 * Base class of transformers for {@link GetByIDQuery}.
 * @author Jens Riemschneider
 */
public abstract class GetByIDQueryTransformer<T extends GetByIDQuery> extends GetByUUIDQueryTransformer<T> {
    private final QueryParameter uniqueIdParam;

    protected GetByIDQueryTransformer(QueryParameter uuidParam, QueryParameter uniqueIdParam) {
        super(uuidParam);
        notNull(uniqueIdParam, "uniqueIdParam cannot be null");
        this.uniqueIdParam = uniqueIdParam;
    }

    @Override
    protected void toEbXML(T query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromStringList(uniqueIdParam, query.getUniqueIDs());
    }

    @Override
    protected void fromEbXML(T query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        slots.toStringList(uniqueIdParam, query.getUniqueIDs());
    }
}