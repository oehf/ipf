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
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValueValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Query parameter validation for parameters that are StringList-based. 
 * @author Jens Riemschneider
 */
public class StringListValidation implements QueryParameterValidation {
    private static final Pattern PATTERN =
            Pattern.compile("\\(\\s*'.*'(\\s*,\\s*'.*')*\\s*\\)");

    private final QueryParameter param;
    private final ValueValidator validator;

    /**
     * Constructs a validation object.
     * @param param
     *          parameter to validate.
     * @param validator
     *          validator used on each of the string in the string list. 
     */
    public StringListValidation(QueryParameter param, ValueValidator validator) {
        notNull(param, "param cannot be null");
        notNull(validator, "validator cannot be null");
        this.param = param;
        this.validator = validator;
    }

    @Override
    public void validate(EbXMLAdhocQueryRequest request) throws XDSMetaDataException {
        List<String> slotValues = request.getSlotValues(param.getSlotName());
        for (String slotValue : slotValues) {
            metaDataAssert(slotValue != null, MISSING_REQUIRED_QUERY_PARAMETER, param);
            metaDataAssert(PATTERN.matcher(slotValue).matches(),
                    PARAMETER_VALUE_NOT_STRING_LIST, param);
        }

        QuerySlotHelper slots = new QuerySlotHelper(request);
        List<String> list = slots.toStringList(param);
        
        if (list != null) {
            for (String value : list) {
                metaDataAssert(value != null, INVALID_QUERY_PARAMETER_VALUE, param);
                validator.validate(value);
            }
        }
    }
}
