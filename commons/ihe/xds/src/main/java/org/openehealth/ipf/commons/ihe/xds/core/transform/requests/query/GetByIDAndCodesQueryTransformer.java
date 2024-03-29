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

import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetByIdAndCodesQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;

import static java.util.Objects.requireNonNull;

/**
 * Base class of transformers for {@link GetByIdAndCodesQuery}.
 * @param <T>
 *          the actual query type that is transformed by an extending subclass.
 * @author Jens Riemschneider
 */
public abstract class GetByIDAndCodesQueryTransformer<T extends GetByIdAndCodesQuery> extends AbstractStoredQueryTransformer<T> {
    private final QueryParameter formatCodeParam;
    private final QueryParameter confCodeParam;
    private final QueryParameter confCodeSchemeParam;
    private final QueryParameter uniqueIdParam;
    private final QueryParameter uuidParam; 
    
    /**
     * Constructs the transformer.
     * @param uuidParam
     *          the parameter name of the UUID parameter.
     * @param uniqueIdParam
     *          the parameter name of the unique ID parameter.
     * @param formatCodeParam
     *          the parameter name of the format code.
     * @param formatCodeSchemeParam
     *          the parameter name of the format code scheme.
     * @param confCodeParam
     *          the parameter name of the confidentiality code.
     * @param confCodeSchemeParam
     *          the parameter name of the confidentiality code scheme.
     */
    protected GetByIDAndCodesQueryTransformer(QueryParameter uuidParam, QueryParameter uniqueIdParam, QueryParameter formatCodeParam, QueryParameter formatCodeSchemeParam, QueryParameter confCodeParam, QueryParameter confCodeSchemeParam) {
        requireNonNull(uniqueIdParam, "uniqueIdParam cannot be null");
        requireNonNull(uuidParam, "uuidParam cannot be null");
        requireNonNull(formatCodeParam, "formatCodeParam cannot be null");
        requireNonNull(formatCodeSchemeParam, "formatCodeSchemeParam cannot be null");
        requireNonNull(confCodeParam, "confCodeParam cannot be null");
        requireNonNull(confCodeSchemeParam, "confCodeSchemeParam cannot be null");
        
        this.formatCodeParam = formatCodeParam;
        this.confCodeParam = confCodeParam;
        this.confCodeSchemeParam = confCodeSchemeParam;
        this.uniqueIdParam = uniqueIdParam;
        this.uuidParam = uuidParam;
    }

    @Override
    protected void toEbXML(T query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromCode(formatCodeParam, query.getFormatCodes());
        slots.fromCode(confCodeParam, query.getConfidentialityCodes());
        slots.fromString(uuidParam, query.getUuid());
        slots.fromString(uniqueIdParam, query.getUniqueId());
    }

    @Override
    protected void fromEbXML(T query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setFormatCodes(slots.toCodeList(formatCodeParam));
        query.setConfidentialityCodes(slots.toCodeQueryList(confCodeParam, confCodeSchemeParam));
        query.setUniqueId(slots.toString(uniqueIdParam));
        query.setUuid(slots.toString(uuidParam));
    }
}
