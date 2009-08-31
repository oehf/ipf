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
package org.openehealth.ipf.commons.ihe.xds.validate.query;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.validate.ValidatorAssertions.metaDataAssert;

import java.util.List;
import java.util.regex.Pattern;

import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.transform.requests.query.QuerySlotHelper;
import org.openehealth.ipf.commons.ihe.xds.validate.XDSMetaDataException;

/**
 * Query parameter validation for parameters that are Code-based. 
 * @author Jens Riemschneider
 */
public class AssociationValidation implements QueryParameterValidation {
    private final QueryParameter param;

    /**
     * Constructs a validation object.
     * @param param
     *          parameter to validate.
     */
    public AssociationValidation(QueryParameter param) {
        notNull(param, "param cannot be null");        
        this.param = param;
    }

    public void validate(EbXMLAdhocQueryRequest request) throws XDSMetaDataException {
        List<String> slotValues = request.getSlotValues(param.getSlotName());
        for (String slotValue : slotValues) {
            metaDataAssert(slotValue != null, MISSING_REQUIRED_QUERY_PARAMETER, param);
            metaDataAssert(Pattern.matches("\\('.*',\\s('.*')*\\)", slotValue), PARAMETER_VALUE_NOT_STRING_LIST, param);
        }

        QuerySlotHelper slots = new QuerySlotHelper(request);
        List<AssociationType> associationTypes = slots.toAssociationType(param);
        
        for (AssociationType type : associationTypes) {
            metaDataAssert(type != null, INVALID_QUERY_PARAMETER_VALUE, param);
        }
    }
}
