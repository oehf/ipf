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
package org.openehealth.ipf.commons.ihe.xds.validate.responses;

import static org.junit.Assert.*;
import static org.openehealth.ipf.commons.ihe.xds.validate.ValidationMessage.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.SampleData;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.commons.ihe.xds.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.metadata.ObjectReference;
import org.openehealth.ipf.commons.ihe.xds.metadata.Organization;
import org.openehealth.ipf.commons.ihe.xds.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.validate.XDSMetaDataException;
import org.openehealth.ipf.commons.ihe.xds.validate.XDSValidationException;
import org.openehealth.ipf.commons.ihe.xds.validate.responses.QueryResponseValidator;

/**
 * Test for {@link ProvideAndRegisterDocumentSetRequestValidator}.
 * @author Jens Riemschneider
 */
public class QueryResponseValidatorTest {
    private QueryResponseValidator validator;    
    private EbXMLFactory factory;
    private QueryResponse response;
    private QueryResponseTransformer transformer;
    private DocumentEntry docEntry;

    @Before
    public void setUp() {
        validator = new QueryResponseValidator();
        factory = new EbXMLFactory21();
        
        response = SampleData.createQueryResponseWithLeafClass();
        transformer = new QueryResponseTransformer(factory);

        docEntry = response.getDocumentEntries().get(0);
    }
    
    @Test
    public void testValidateGoodCase() throws XDSValidationException {
        validator.validate(transformer.toEbXML(response), null);
    }
    
    @Test
    public void testQueryResponseDoesNotHaveSubmissionSetLimitations() throws XDSValidationException {
        response.getSubmissionSets().clear();
        validator.validate(transformer.toEbXML(response), null);
    }
    
    @Test
    public void testValidateDelegatesToSubmitObjectsRequestValidator() {
        // Try a failure that is produced by the SubmitObjectsRequestValidator
        docEntry.getAuthors().get(0).getAuthorInstitution().add(new Organization(null, "LOL", null));
        expectFailure(ORGANIZATION_NAME_MISSING);            
    }
    
    @Test
    public void testValidateDelegatesToRegistryResponseValidator() {
        // Try a failure that is produced by the RegistryResponseValidator
        response.setStatus(null);
        expectFailure(INVALID_STATUS_IN_RESPONSE);
    }
    
    @Test
    public void testMissingObjRef() {
        response.getReferences().add(new ObjectReference());        
        expectFailure(MISSING_OBJ_REF);
    }
    
    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(response));
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLQueryResponse ebXMLQueryResponse) {
        try {
            validator.validate(ebXMLQueryResponse, null);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
