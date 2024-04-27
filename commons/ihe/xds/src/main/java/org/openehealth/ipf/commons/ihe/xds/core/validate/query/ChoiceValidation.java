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

import org.openehealth.ipf.commons.ihe.xds.core.XdsRuntimeException;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.Arrays;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_REQUIRED_QUERY_PARAMETER;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.QUERY_PARAMETERS_CANNOT_BE_SET_TOGETHER;

/**
 * Query parameter validation to ensure that only one of the given parameters is specified.
 * Also has the "either ... or ... " check to avoid the case that all parameters haven't a
 * value set if it's not marked as optional.
 * @author Jens Riemschneider
 */
public class ChoiceValidation implements QueryParameterValidation {
    private final QueryParameter[] params;
    private final boolean optional;

    /**
     * Constructs a validation object.
     * @param params
     *          parameters to validate.
     * @param optional
     *          whether all parameters are optional ({@code true}) or one parameter must be specified ({@code false}).
     */
    public ChoiceValidation(boolean optional, QueryParameter... params) {
        this.params = requireNonNull(params, "params cannot be null");
        this.optional = optional;
    }

    @Override
    public void validate(EbXMLAdhocQueryRequest<AdhocQueryRequest> request) throws XDSMetaDataException {
        var paramSlotNames = Arrays.stream(params)
                .map(QueryParameter::getSlotName)
                .toList();

        var count = paramSlotNames.stream()
                .map(request::getSingleSlotValue)
                .filter(Objects::nonNull)
                .count();

        if (!this.optional && count == 0L) {
            throw new XDSMetaDataException(MISSING_REQUIRED_QUERY_PARAMETER, "one of " + paramSlotNames);
        }
        if (count > 1L) {
            throw new XdsRuntimeException(
                    ErrorCode.STORED_QUERY_PARAM_NUMBER,
                    String.format(QUERY_PARAMETERS_CANNOT_BE_SET_TOGETHER.getText(), paramSlotNames),
                    Severity.ERROR,
                    null);
        }
    }
}
