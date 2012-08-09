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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveImagingDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveImagingDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveSeries;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveStudy;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RetrieveImagingDocumentSetRequestTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
    private ValidationProfile profile;

    @Before
    public void setUp() {
        validator = new RetrieveImagingDocumentSetRequestValidator();
        EbXMLFactory factory = new EbXMLFactory30();
        transformer = new RetrieveImagingDocumentSetRequestTransformer(factory);
        request = SampleData.createRetrieveImagingDocumentSet();
        profile = new ValidationProfile(IpfInteractionId.RAD_69);
    }
    
    @Test
    public void testGoodCase() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(request), profile);
    }
    
    @Test
    public void testStudyInstanceIdMustBeSpecified() {
        List<RetrieveDocument> retrieveDocuments = new ArrayList<RetrieveDocument>();
        RetrieveDocument retrieveDocument = new RetrieveDocument("repo1", "doc1", "urn:oid:1.2.5");
        retrieveDocuments.add(retrieveDocument);

        RetrieveSeries retrieveSeries = new RetrieveSeries("urn:oid:1.2.3", retrieveDocuments);
        List<RetrieveSeries> retrieveSerieses = new ArrayList<RetrieveSeries>();
        retrieveSerieses.add(retrieveSeries);

        request.getRetrieveStudies().add(new RetrieveStudy(null, retrieveSerieses));
        EbXMLRetrieveImagingDocumentSetRequest ebXML = transformer.toEbXML(request);
        expectFailure(STUDY_INSTANCE_UID_MUST_BE_SPECIFIED, ebXML);
    }

    @Test
    public void testSteriesInstanceIdMustBeSpecified() {
        List<RetrieveDocument> retrieveDocuments = new ArrayList<RetrieveDocument>();
        RetrieveDocument retrieveDocument = new RetrieveDocument("repo1", "doc1", "urn:oid:1.2.6");
        retrieveDocuments.add(retrieveDocument);

        RetrieveSeries retrieveSeries = new RetrieveSeries(null, retrieveDocuments);
        List<RetrieveSeries> retrieveSerieses = new ArrayList<RetrieveSeries>();
        retrieveSerieses.add(retrieveSeries);

        request.getRetrieveStudies().add(new RetrieveStudy("urn:oid:1.1.3", retrieveSerieses));
        EbXMLRetrieveImagingDocumentSetRequest ebXML = transformer.toEbXML(request);
        expectFailure(SERIES_INSTANCE_UID_MUST_BE_SPECIFIED, ebXML);
    }

    @Test
    public void testRepoIdMustBeSpecified() {
        request.getRetrieveStudies().get(0).getRetrieveSerieses().get(0).getDocuments().add(new RetrieveDocument(null, "doc3", "home3"));
        EbXMLRetrieveImagingDocumentSetRequest ebXML = transformer.toEbXML(request);
        expectFailure(REPO_ID_MUST_BE_SPECIFIED, ebXML);
    }

    @Test
    public void testDocIdMustBeSpecified() {
        request.getRetrieveStudies().get(0).getRetrieveSerieses().get(0).getDocuments().add(new RetrieveDocument("repo3", "", "home3"));
        EbXMLRetrieveImagingDocumentSetRequest ebXML = transformer.toEbXML(request);
        expectFailure(DOC_ID_MUST_BE_SPECIFIED, ebXML);
    }
        
    private void expectFailure(ValidationMessage expectedMessage, EbXMLRetrieveImagingDocumentSetRequest ebXML) {
        try {
            validator.validate(ebXML, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
