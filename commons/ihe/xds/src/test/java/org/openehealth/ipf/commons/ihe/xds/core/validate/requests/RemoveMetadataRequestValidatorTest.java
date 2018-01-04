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
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRemoveMetadataRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRemoveMetadataRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveMetadata;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectRefListType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectRefType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RemoveMetadataRequestTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_62;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.EMPTY_REFERENCE_LIST;

/**
 * Test for {@link RemoveMetadataRequestValidator}.
 * @author Boris Stanojevic
 */
public class RemoveMetadataRequestValidatorTest {
    private RemoveMetadataRequestValidator validator;
    private RemoveMetadata request;
    private RemoveMetadataRequestTransformer transformer;

    @Before
    public void setUp() {
        validator = new RemoveMetadataRequestValidator();
        request = SampleData.createRemoveMetadata();
        transformer = new RemoveMetadataRequestTransformer();
    }
    
    @Test
    public void testValidateGoodCase() {
        validator.validate(transformer.toEbXML(request), ITI_62);
    }

    @Test
    public void testValidateEmptyReferences() {
        request.getReferences().clear();
        expectFailure(EMPTY_REFERENCE_LIST);
    }

    @Test
    public void testIssue150() {
        String[] uuids = {UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()};
        RemoveObjectsRequest request = new RemoveObjectsRequest();
        request.setObjectRefList(new ObjectRefListType());
        for (String uuid : uuids) {
            ObjectRefType reference = new ObjectRefType();
            reference.setId(uuid);
            request.getObjectRefList().getObjectRef().add(reference);
        }
        EbXMLRemoveMetadataRequest ebXml = new EbXMLRemoveMetadataRequest30(request);
        validator.validate(ebXml, ITI_62);
    }

    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(request));
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLRemoveMetadataRequest ebXML) {
        expectFailure(expectedMessage, ebXML, ITI_62);
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLRemoveMetadataRequest ebXML, ValidationProfile profile) {
        try {
            validator.validate(ebXML, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
