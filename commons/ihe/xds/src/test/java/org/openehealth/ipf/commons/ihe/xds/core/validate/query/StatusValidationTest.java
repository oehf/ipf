package org.openehealth.ipf.commons.ihe.xds.core.validate.query;

import static org.junit.Assert.assertEquals;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_QUERY_PARAMETER_VALUE;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

public class StatusValidationTest {
    private static final StatusValidation validator = new StatusValidation(QueryParameter.DOC_ENTRY_STATUS);

    @Test
    public void validateStatusSuccess() {
        validator.validate(validQueryRequestWithStatus());
    }

    @Test
    public void validateStatusForInvalidContent() {
        try {
            validator.validate(invalidQueryRequestWithStatus());
        } catch (XDSMetaDataException e) {
            assertEquals(0, e.getValidationMessage().compareTo(INVALID_QUERY_PARAMETER_VALUE));
        }
    }

    private EbXMLAdhocQueryRequest invalidQueryRequestWithStatus() {
        QueryRegistry createFindDocumentsQuery = SampleData.createFindDocumentsQuery();
        EbXMLAdhocQueryRequest ebXML = new QueryRegistryTransformer().toEbXML(createFindDocumentsQuery);
        ebXML.getSlotValues(QueryParameter.DOC_ENTRY_STATUS.getSlotName()).add("('invalid')");
        return ebXML;
    }

    private EbXMLAdhocQueryRequest validQueryRequestWithStatus() {
        QueryRegistry createFindDocumentsQuery = SampleData.createFindDocumentsQuery();
        return new QueryRegistryTransformer().toEbXML(createFindDocumentsQuery);
    }
}
