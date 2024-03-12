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
package org.openehealth.ipf.commons.ihe.xds.core.validate.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveImagingDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveImagingDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveSeries;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveStudy;
import org.openehealth.ipf.commons.ihe.xds.core.stub.xdsi.RetrieveImagingDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RetrieveImagingDocumentSetRequestTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.openehealth.ipf.commons.ihe.xds.RAD.Interactions.RAD_69;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;

/**
 * Validates {@link org.openehealth.ipf.commons.ihe.xds.core.validate.requests.RetrieveImagingDocumentSetRequestValidator}.
 * @author Clay Sebourn
 */
public class RetrieveImagingDocumentSetRequestValidatorTest
{
    private RetrieveImagingDocumentSetRequestValidator validator;
    private RetrieveImagingDocumentSet request;
    private RetrieveImagingDocumentSetRequestTransformer transformer;

    @BeforeEach
    public void setUp() {
        validator = RetrieveImagingDocumentSetRequestValidator.getInstance();
        EbXMLFactory factory = new EbXMLFactory30();
        transformer = new RetrieveImagingDocumentSetRequestTransformer(factory);
        request = SampleData.createRetrieveImagingDocumentSet();
    }
    
    @Test
    public void testGoodCase() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(request), RAD_69);
    }
    
    @Test
    public void testStudyInstanceIdMustBeSpecified() {
        var documentReferences = new ArrayList<DocumentReference>();
        var documentReference = new DocumentReference("repo1", "doc1", "urn:oid:1.2.5");
        documentReferences.add(documentReference);

        var retrieveSeries = new RetrieveSeries("urn:oid:1.2.3", documentReferences);
        var retrieveSerieses = new ArrayList<RetrieveSeries>();
        retrieveSerieses.add(retrieveSeries);

        request.getRetrieveStudies().add(new RetrieveStudy(null, retrieveSerieses));
        var ebXML = transformer.toEbXML(request);
        expectFailure(STUDY_INSTANCE_UID_MUST_BE_SPECIFIED, ebXML);
    }

    @Test
    public void testSteriesInstanceIdMustBeSpecified() {
        var documentReferences = new ArrayList<DocumentReference>();
        var documentReference = new DocumentReference("repo1", "doc1", "urn:oid:1.2.6");
        documentReferences.add(documentReference);

        var retrieveSeries = new RetrieveSeries(null, documentReferences);
        var retrieveSerieses = new ArrayList<RetrieveSeries>();
        retrieveSerieses.add(retrieveSeries);

        request.getRetrieveStudies().add(new RetrieveStudy("urn:oid:1.1.3", retrieveSerieses));
        var ebXML = transformer.toEbXML(request);
        expectFailure(SERIES_INSTANCE_UID_MUST_BE_SPECIFIED, ebXML);
    }

    @Test
    public void testRepoIdMustBeSpecified() {
        request.getRetrieveStudies().get(0).getRetrieveSerieses().get(0).getDocuments().add(new DocumentReference(null, "doc3", "home3"));
        var ebXML = transformer.toEbXML(request);
        expectFailure(REPO_ID_MUST_BE_SPECIFIED, ebXML);
    }

    @Test
    public void testDocIdMustBeSpecified() {
        request.getRetrieveStudies().get(0).getRetrieveSerieses().get(0).getDocuments().add(new DocumentReference("repo3", "", "home3"));
        var ebXML = transformer.toEbXML(request);
        expectFailure(DOC_ID_MUST_BE_SPECIFIED, ebXML);
    }
        
    private void expectFailure(ValidationMessage expectedMessage, EbXMLRetrieveImagingDocumentSetRequest<RetrieveImagingDocumentSetRequestType> ebXML) {
        try {
            validator.validate(ebXML, RAD_69);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
