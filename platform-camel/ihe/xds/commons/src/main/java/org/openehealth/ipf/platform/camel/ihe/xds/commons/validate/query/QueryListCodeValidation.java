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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.query;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidationMessage.INVALID_QUERY_PARAMETER_VALUE;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidationMessage.MISSING_REQUIRED_QUERY_PARAMETER;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidationMessage.PARAMETER_VALUE_NOT_STRING_LIST;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidatorAssertions.metaDataAssert;

import java.util.List;
import java.util.regex.Pattern;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryList;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.query.QuerySlotHelper;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.XDSMetaDataException;

/**
 * Query parameter validation for parameters that are QueryList-based. 
 * @author Jens Riemschneider
 */
public class QueryListCodeValidation implements QueryParameterValidation {
    private final QueryParameter param;
    private final QueryParameter schemeParam;

    /**
     * Constructs a validation object.
     * @param param
     *          parameter of the code to validate.
     * @param schemeParam
     *          parameter of the scheme to validate.
     */
    public QueryListCodeValidation(QueryParameter param, QueryParameter schemeParam) {
        notNull(param, "param cannot be null");
        notNull(schemeParam, "schemeParam cannot be null");
        
        this.param = param;
        this.schemeParam = schemeParam;
    }

    public void validate(EbXMLAdhocQueryRequest request) throws XDSMetaDataException {
        List<String> slotValues = request.getSlotValues(param.getSlotName());
        for (String slotValue : slotValues) {
            metaDataAssert(slotValue != null, MISSING_REQUIRED_QUERY_PARAMETER, param);
            metaDataAssert(Pattern.matches("\\(\\s*'.*'(\\s*,\\s*'.*')*\\s*\\)", slotValue), 
                    PARAMETER_VALUE_NOT_STRING_LIST, param);
        }

        QuerySlotHelper slots = new QuerySlotHelper(request);
        QueryList<Code> codes = slots.toCodeQueryList(param, schemeParam);
        
        if (codes != null) {
            QueryList<String> schemes = slots.toStringQueryList(schemeParam);
            if (schemes != null) {
                metaDataAssert(codes.getOuterList().size() == schemes.getOuterList().size(), INVALID_QUERY_PARAMETER_VALUE, schemeParam);
                for (int idx = 0; idx < codes.getOuterList().size(); ++idx) {
                    metaDataAssert(codes.getOuterList().get(idx).size() == schemes.getOuterList().get(idx).size(), INVALID_QUERY_PARAMETER_VALUE, schemeParam);
                }
            }

            for (List<Code> innerList : codes.getOuterList()) {
                for (Code code : innerList) {
                    metaDataAssert(code != null, INVALID_QUERY_PARAMETER_VALUE, param);
                    metaDataAssert(code.getCode() != null, INVALID_QUERY_PARAMETER_VALUE, param);
                }
            }
        }
        else {
            QueryList<String> schemes = slots.toStringQueryList(schemeParam);
            metaDataAssert(schemes == null, INVALID_QUERY_PARAMETER_VALUE, schemeParam);
        }
    }
}
