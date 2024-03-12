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

import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetFromDocumentQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_UNIQUE_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_UUID;

/**
 * Base class for transformers based on {@link GetFromDocumentQuery}.
 * @param <T>
 *          the actual query type that is transformed by an extending subclass.
 * @author Jens Riemschneider
 */
public abstract class GetFromDocumentQueryTransformer<T extends GetFromDocumentQuery> extends AbstractStoredQueryTransformer<T> {

    @Override
    protected void toEbXML(T query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromString(DOC_ENTRY_UUID, query.getUuid());
        slots.fromString(DOC_ENTRY_UNIQUE_ID, query.getUniqueId());
    }

    @Override
    protected void fromEbXML(T query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setUniqueId(slots.toString(DOC_ENTRY_UNIQUE_ID));
        query.setUuid(slots.toString(DOC_ENTRY_UUID));
    }
}
