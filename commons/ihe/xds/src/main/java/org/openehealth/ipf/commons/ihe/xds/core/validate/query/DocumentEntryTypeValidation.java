/*
 * Copyright 2012 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.List;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_QUERY_PARAMETER_VALUE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_REQUIRED_QUERY_PARAMETER;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Query parameter validation for $XDSDocumentEntryType.
 * @author Dmytro Rud
 */
public class DocumentEntryTypeValidation implements QueryParameterValidation {
    private final QueryParameter param = QueryParameter.DOC_ENTRY_TYPE;

    @Override
    public void validate(EbXMLAdhocQueryRequest request) throws XDSMetaDataException {
        List<String> slotValues = request.getSlotValues(param.getSlotName());
        for (String slotValue : slotValues) {
            metaDataAssert(slotValue != null, MISSING_REQUIRED_QUERY_PARAMETER, param);
        }

        QuerySlotHelper slots = new QuerySlotHelper(request);
        List<DocumentEntryType> list = slots.toDocumentEntryType(param);

        if (list != null) {
            for (DocumentEntryType type : list) {
                metaDataAssert(type != null, INVALID_QUERY_PARAMETER_VALUE, param);
            }
        }
    }
}
