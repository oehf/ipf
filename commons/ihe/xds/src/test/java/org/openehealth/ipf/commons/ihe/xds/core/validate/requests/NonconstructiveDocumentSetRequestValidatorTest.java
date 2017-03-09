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
package org.openehealth.ipf.commons.ihe.xds.core.validate.requests;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLNonconstructiveDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RetrieveDocumentSetRequestTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.ihe.xds.XDS_B.Interactions.ITI_43;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DOC_ID_MUST_BE_SPECIFIED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.REPO_ID_MUST_BE_SPECIFIED;

/**
 * Validates {@link NonconstructiveDocumentSetRequestValidator}.
 * @author Jens Riemschneider
 */
public class NonconstructiveDocumentSetRequestValidatorTest {
    private NonconstructiveDocumentSetRequestValidator validator;
    private RetrieveDocumentSet request;
    private RetrieveDocumentSetRequestTransformer transformer;

    @Before
    public void setUp() {
        validator = new NonconstructiveDocumentSetRequestValidator();
        EbXMLFactory factory = new EbXMLFactory30();
        transformer = new RetrieveDocumentSetRequestTransformer(factory);
        request = SampleData.createRetrieveDocumentSet();
    }
    
    @Test
    public void testGoodCase() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(request), ITI_43);
    }
    
    @Test
    public void testRepoIdMustBeSpecified() {
        request.getDocuments().add(new DocumentReference(null, "doc3", "home3"));
        EbXMLNonconstructiveDocumentSetRequest ebXML = transformer.toEbXML(request);
        expectFailure(REPO_ID_MUST_BE_SPECIFIED, ebXML);
    }
    
    @Test
    public void testDocIdMustBeSpecified() {
        request.getDocuments().add(new DocumentReference("repo3", "", "home3"));
        EbXMLNonconstructiveDocumentSetRequest ebXML = transformer.toEbXML(request);
        expectFailure(DOC_ID_MUST_BE_SPECIFIED, ebXML);
    }
        
    private void expectFailure(ValidationMessage expectedMessage, EbXMLNonconstructiveDocumentSetRequest ebXML) {
        try {
            validator.validate(ebXML, ITI_43);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
