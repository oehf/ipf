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

import static org.apache.commons.lang3.Validate.notNull;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValueValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Query parameter validation for parameters that are Number-based.
 * @author Jens Riemschneider
 */
public class NumberValidation implements QueryParameterValidation {
    private final QueryParameter param;
    private final ValueValidator validator;

    /**
     * Constructs a validation object.
     * @param param
     *          parameter to validate.
     * @param validator
     *          validator to use on the parameter value.
     */
    public NumberValidation(QueryParameter param, ValueValidator validator) {
        notNull(param, "param cannot be null");
        notNull(validator, "validator cannot be null");
        
        this.param = param;
        this.validator = validator;
    }

    @Override
    public void validate(EbXMLAdhocQueryRequest request) throws XDSMetaDataException {
        QuerySlotHelper slots = new QuerySlotHelper(request);
        String value = slots.toNumber(param);
        if (value != null) {
            validator.validate(value);
        }
    }
}
