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
package org.openehealth.ipf.commons.ihe.xds.core.validate.responses;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRetrieveDocumentSetResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.RetrieveDocumentSetResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.ihe.xds.XDS_B.Interactions.ITI_43;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DOC_ID_MUST_BE_SPECIFIED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_STATUS_IN_RESPONSE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MIME_TYPE_MUST_BE_SPECIFIED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_DOCUMENT_FOR_DOC_ENTRY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.REPO_ID_MUST_BE_SPECIFIED;

/**
 * Tests for {@link RetrieveDocumentSetResponseValidator}.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentResponseValidatorTest {
    private RetrieveDocumentSetResponseValidator validator;
    private RetrievedDocumentSet response;
    private RetrieveDocumentSetResponseTransformer transformer;

    @Before
    public void setUp() {
        validator = new RetrieveDocumentSetResponseValidator();
        EbXMLFactory factory = new EbXMLFactory30();
        transformer = new RetrieveDocumentSetResponseTransformer(factory);
        response = SampleData.createRetrievedDocumentSet();
    }

    @Test
    public void testGoodCase() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(response), ITI_43);
    }
    
    @Test
    public void testDelegatesToRegistryResponseValidator() {
        // Test a failure that is detected by the RegistryResponseValidator
        response.setStatus(null);
        expectFailure(INVALID_STATUS_IN_RESPONSE);
    }

    @Test
    public void testDelegatesToRegistryResponseValidatorEmptyInternalRegistryResponse() {
        // Test a failure that is detected by the RegistryResponseValidator
        RetrieveDocumentSetResponseType responseType = new RetrieveDocumentSetResponseType();
        responseType.setRegistryResponse(null);
        expectFailure(INVALID_STATUS_IN_RESPONSE, new EbXMLRetrieveDocumentSetResponse30(responseType));
    }
    
    @Test
    public void testRepoIdMustBeSpecified() {
        DocumentReference requestData = new DocumentReference(null, "doc3", "home3");
        RetrievedDocument doc = new RetrievedDocument();
        doc.setRequestData(requestData);
        doc.setDataHandler(SampleData.createDataHandler());
        doc.setMimeType("text/plain");
        response.getDocuments().add(doc);
        EbXMLRetrieveDocumentSetResponse ebXML = transformer.toEbXML(response);
        expectFailure(REPO_ID_MUST_BE_SPECIFIED, ebXML);
    }
    
    @Test
    public void testDocIdMustBeSpecified() {
        DocumentReference requestData = new DocumentReference("repo3", "", "home3");
        RetrievedDocument doc = new RetrievedDocument();
        doc.setRequestData(requestData);
        doc.setDataHandler(SampleData.createDataHandler());
        doc.setMimeType("text/plain");
        response.getDocuments().add(doc);
        EbXMLRetrieveDocumentSetResponse ebXML = transformer.toEbXML(response);
        expectFailure(DOC_ID_MUST_BE_SPECIFIED, ebXML);
    }
    
    @Test
    public void testMimeTypeMustBeSpecified() {
        DocumentReference requestData = new DocumentReference("repo3", "doc3", "home3");
        RetrievedDocument doc = new RetrievedDocument();
        doc.setRequestData(requestData);
        doc.setDataHandler(SampleData.createDataHandler());
        doc.setMimeType("");
        response.getDocuments().add(doc);
        EbXMLRetrieveDocumentSetResponse ebXML = transformer.toEbXML(response);
        expectFailure(MIME_TYPE_MUST_BE_SPECIFIED, ebXML);
    }

    @Test
    public void testDocumentMustBeSpecified() {
        DocumentReference requestData = new DocumentReference("repo3", "doc3", "urn:oid:1.2.5");
        RetrievedDocument doc = new RetrievedDocument();
        doc.setRequestData(requestData);
        doc.setDataHandler(null);
        doc.setMimeType("text/plain");
        response.getDocuments().add(doc);
        EbXMLRetrieveDocumentSetResponse ebXML = transformer.toEbXML(response);
        expectFailure(MISSING_DOCUMENT_FOR_DOC_ENTRY, ebXML);
    }
        
    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(response));
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLRetrieveDocumentSetResponse ebXMLRegistryResponse) {
        try {
            validator.validate(ebXMLRegistryResponse, ITI_43);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
