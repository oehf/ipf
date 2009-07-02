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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetByIDAndCodesQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;

/**
 * Base class of transformers for {@link GetByIDAndCodesQuery}.
 * @author Jens Riemschneider
 */
public abstract class GetByIDAndCodesQueryTransformer<T extends GetByIDAndCodesQuery> extends GetByIDQueryTransformer<T> {
    private final QueryParameter formatCodeParam;
    private final QueryParameter confCodeParam; 
    
    public GetByIDAndCodesQueryTransformer(QueryParameter uuidParam, QueryParameter uniqueIdParam, QueryParameter formatCodeParam, QueryParameter confCodeParam) {
        super(uuidParam, uniqueIdParam);
        notNull(formatCodeParam, "formatCodeParam cannot be null");
        notNull(confCodeParam, "confCodeParam cannot be null");
        this.formatCodeParam = formatCodeParam;
        this.confCodeParam = confCodeParam;
    }

    @Override
    protected void toEbXML(T query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        
        slots.fromCode(formatCodeParam, query.getFormatCodes());
        slots.fromCode(confCodeParam, query.getConfidentialityCodes());
    }

    @Override
    protected void fromEbXML(T query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
    
        slots.toCode(formatCodeParam, query.getFormatCodes());
        slots.toCode(confCodeParam, query.getConfidentialityCodes());
    }

}