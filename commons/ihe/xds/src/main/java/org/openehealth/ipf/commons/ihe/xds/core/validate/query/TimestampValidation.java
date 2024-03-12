/*
 * Copyright 2022 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;
import org.openehealth.ipf.commons.ihe.xds.core.validate.TimeValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import static java.util.Objects.requireNonNull;

/**
 * Query parameter validation for parameters that are timestamp-based.
 * @author Jens Riemschneider
 */
public class TimestampValidation implements QueryParameterValidation {
    private final QueryParameter param;
    private final TimeValidator validator;


    /**
     * Constructs a validation object.
     * @param param
     *          parameter to validate.
     */
    public TimestampValidation(QueryParameter param) {
        this.param = requireNonNull(param, "param cannot be null");
        this.validator = new TimeValidator();
    }

    @Override
    public void validate(EbXMLAdhocQueryRequest<AdhocQueryRequest> request) throws XDSMetaDataException {
        var slots = new QuerySlotHelper(request);
        var value = slots.toTimestamp(param);
        if (value != null) {
            validator.validate(value);
        }
    }
}
