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
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RemoveDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import static org.junit.Assert.*;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SUBMISSION_SET_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;

/**
 * Test for {@link org.openehealth.ipf.commons.ihe.xds.core.validate.requests.RemoveObjectsRequestValidator}.
 * @author Boris Stanojevic
 */
public class RemoveObjectsRequestValidatorTest {
    private RemoveObjectsRequestValidator validator;
    private RemoveDocumentSet request;
    private RemoveDocumentSetTransformer transformer;
    private ValidationProfile profile = new ValidationProfile(IpfInteractionId.ITI_62);

    @Before
    public void setUp() {
        validator = new RemoveObjectsRequestValidator();
        request = SampleData.createRemoveDocumentSet();
        transformer = new RemoveDocumentSetTransformer();
    }
    
    @Test
    public void testValidateGoodCase() {
        validator.validate(transformer.toEbXML(request), profile);
    }

    @Test
    public void testValidateWrongDeletionScope() {
        request.setDeletionScope("Wrong-deletion-scope");
        expectFailure(WRONG_DELETION_SCOPE);
    }

    @Test
    public void testValidateEmptyReferences() {
        request.getReferences().clear();
        expectFailure(EMPTY_REFERENCE_LIST);
    }

    @Test
    public void testValidateAdHocRequestNotSpecified() {
        QueryRegistry queryRegistry = SampleData.createFindDocumentsQuery();
        request.setQuery(queryRegistry.getQuery());
        expectFailure(OBJECT_SHALL_NOT_BE_SPECIFIED);
    }
    
    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(request));
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLRemoveObjectsRequest ebXML) {
        expectFailure(expectedMessage, ebXML, profile);
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLRemoveObjectsRequest ebXML, ValidationProfile profile) {
        try {
            validator.validate(ebXML, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
