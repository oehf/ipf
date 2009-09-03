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
package org.openehealth.ipf.commons.ihe.xds.core.validate.query;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import java.util.Arrays;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Query parameter validation to ensure that only one of the given parameters is specified. 
 * @author Jens Riemschneider
 */
public class ChoiceValidation implements QueryParameterValidation {
    private final QueryParameter[] params;

    /**
     * Constructs a validation object.
     * @param params
     *          parameters to validate.
     */
    public ChoiceValidation(QueryParameter... params) {
        notNull(params, "params cannot be null");        
        this.params = params;
    }

    public void validate(EbXMLAdhocQueryRequest request) throws XDSMetaDataException {
        boolean defined = false;
        for (QueryParameter param : params) {
            String value = request.getSingleSlotValue(param.getSlotName());
            metaDataAssert(value == null || !defined, QUERY_PARAMETERS_CANNOT_BE_SET_TOGETHER, Arrays.asList(params));
            defined |= value != null;
        }
    }
}
