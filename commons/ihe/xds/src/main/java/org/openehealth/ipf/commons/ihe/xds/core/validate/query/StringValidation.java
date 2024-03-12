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

import static java.util.Objects.requireNonNull;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValueValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.regex.Pattern;

/**
 * Query parameter validation for parameters that are String-based.
 * @author Jens Riemschneider
 */
public class StringValidation implements QueryParameterValidation {
    private static final Pattern PATTERN = Pattern.compile("'.*'");

    private final QueryParameter param;
    private final ValueValidator validator;
    private final boolean optional;

    /**
     * Constructs a validation object.
     * @param param
     *          parameter to validate.
     * @param validator
     *          validator to use on the parameter value.
     * @param optional
     *          <code>true</code> if this parameter is optional.
     */
    public StringValidation(QueryParameter param, ValueValidator validator, boolean optional) {
        this.param = requireNonNull(param, "param cannot be null");
        this.validator = requireNonNull(validator, "validator cannot be null");

        this.optional = optional;
    }

    @Override
    public void validate(EbXMLAdhocQueryRequest<AdhocQueryRequest> request) throws XDSMetaDataException {
        var slotValues = request.getSlotValues(param.getSlotName());
        metaDataAssert(optional || !slotValues.isEmpty(), MISSING_REQUIRED_QUERY_PARAMETER, param);
        metaDataAssert(optional || slotValues.size() == 1, TOO_MANY_VALUES_FOR_QUERY_PARAMETER, param);
        
        if (!slotValues.isEmpty()) {
            var slotValue = slotValues.get(0);
            metaDataAssert(slotValue != null || optional, MISSING_REQUIRED_QUERY_PARAMETER, param);
            metaDataAssert(PATTERN.matcher(slotValue).matches(), PARAMETER_VALUE_NOT_STRING, param);
        }

        var slots = new QuerySlotHelper(request);
        var value = slots.toString(param);
        metaDataAssert(value != null || optional, MISSING_REQUIRED_QUERY_PARAMETER, param);
        if (value != null) {
            validator.validate(value);
        }
    }
}
