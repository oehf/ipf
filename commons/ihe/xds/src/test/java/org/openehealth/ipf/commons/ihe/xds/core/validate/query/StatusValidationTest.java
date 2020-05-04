/*
 * Copyright 2018 the original author or authors.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_QUERY_PARAMETER_VALUE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_REQUIRED_QUERY_PARAMETER;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

public class StatusValidationTest {
    private static final StatusValidation validator = new StatusValidation(QueryParameter.DOC_ENTRY_STATUS);

    @Test
    public void validateStatusSuccess() {
        validator.validate(validQueryRequestWithStatus());
    }

    @Test
    public void ignoreInvalidContentIfValidIsAlsoPresent() {
        validator.validate(
                invalidQueryRequestWithStatus("('invalid', '" + AvailabilityStatus.APPROVED.getQueryOpcode() + "')"));
    }

    @Test
    public void onlyInvalidContentIsPresent() {
        expectValidationError(INVALID_QUERY_PARAMETER_VALUE, invalidQueryRequestWithStatus("('invalid')"));
    }

    @Test
    public void noStatusIsPresent() {
        expectValidationError(MISSING_REQUIRED_QUERY_PARAMETER, invalidQueryRequestWithStatus(null));
    }

    private void expectValidationError(ValidationMessage expectedError, EbXMLAdhocQueryRequest request) {
        try {
            validator.validate(request);
            fail("XDSMetaDataException expected");
        } catch (XDSMetaDataException e) {
            assertEquals(0, e.getValidationMessage().compareTo(expectedError));
        }
    }

    private EbXMLAdhocQueryRequest invalidQueryRequestWithStatus(String statusQuery) {
        var createFindDocumentsQuery = SampleData.createFindDocumentsQuery();
        var ebXML = new QueryRegistryTransformer().toEbXML(createFindDocumentsQuery);
        var slotValues = ebXML.getSlotValues(QueryParameter.DOC_ENTRY_STATUS.getSlotName());
        slotValues.clear();
        if (statusQuery != null)
            slotValues.add(statusQuery);
        return ebXML;
    }
    private EbXMLAdhocQueryRequest validQueryRequestWithStatus() {
        var createFindDocumentsQuery = SampleData.createFindDocumentsQuery();
        return new QueryRegistryTransformer().toEbXML(createFindDocumentsQuery);
    }
}
