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
package org.openehealth.ipf.commons.ihe.xds.validate.requests;

import static org.junit.Assert.*;
import static org.openehealth.ipf.commons.ihe.xds.validate.ValidationMessage.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.SampleData;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.commons.ihe.xds.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.metadata.Organization;
import org.openehealth.ipf.commons.ihe.xds.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.validate.Actor;
import org.openehealth.ipf.commons.ihe.xds.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.validate.XDSMetaDataException;
import org.openehealth.ipf.commons.ihe.xds.validate.XDSValidationException;
import org.openehealth.ipf.commons.ihe.xds.validate.requests.ProvideAndRegisterDocumentSetRequestValidator;

/**
 * Test for {@link ProvideAndRegisterDocumentSetRequestValidator}.
 * @author Jens Riemschneider
 */
public class ProvideAndRegisterDocumentSetRequestValidatorTest {
    private ProvideAndRegisterDocumentSetRequestValidator validator;    
    private EbXMLFactory factory;
    private ProvideAndRegisterDocumentSet request;
    private ProvideAndRegisterDocumentSetTransformer transformer;
    
    private DocumentEntry docEntry;

    @Before
    public void setUp() {
        validator = new ProvideAndRegisterDocumentSetRequestValidator();
        factory = new EbXMLFactory21();
        
        request = SampleData.createProvideAndRegisterDocumentSet();
        transformer = new ProvideAndRegisterDocumentSetTransformer(factory);

        docEntry = request.getDocuments().get(0).getDocumentEntry();
    }
    
    @Test
    public void testValidateGoodCase() throws XDSValidationException {
        validator.validate(transformer.toEbXML(request), null);
    }
    
    @Test
    public void testValidateDelegatesToSubmitObjectsRequestValidator() {
        // Try a failure that is produced by the SubmitObjectsRequestValidator
        docEntry.getAuthors().get(0).getAuthorInstitution().add(new Organization(null, "LOL", null));
        expectFailure(ORGANIZATION_NAME_MISSING);            
    }
    
    @Test
    public void testValidateMissingDocEntryForDocument() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.addDocument("lol", SampleData.createDataHandler());
        expectFailure(MISSING_DOC_ENTRY_FOR_DOCUMENT, ebXML);
    }
    
    @Test
    public void testValidateMissingDocumentForDocEntry() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.removeDocument("document01");
        expectFailure(MISSING_DOCUMENT_FOR_DOC_ENTRY, ebXML);
    }
    
    @Test
    public void testRepositoryUniqueIdIsNotNecessary() {
        docEntry.setRepositoryUniqueId(null);
        validator.validate(transformer.toEbXML(request), new ValidationProfile(false, true, Actor.REPOSITORY));
    }
    
    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(request), null);
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLProvideAndRegisterDocumentSetRequest ebXML) {
        expectFailure(expectedMessage, ebXML, null);
    }
    
    private void expectFailure(ValidationMessage expectedMessage, EbXMLProvideAndRegisterDocumentSetRequest ebXML, ValidationProfile profile) {
        try {
            validator.validate(ebXML, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
