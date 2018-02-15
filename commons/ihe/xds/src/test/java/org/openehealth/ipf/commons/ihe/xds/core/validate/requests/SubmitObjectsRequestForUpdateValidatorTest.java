/*
 * Copyright 2013 the original author or authors.
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

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_57;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.LOGICAL_ID_EQUALS_ENTRY_UUID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.LOGICAL_ID_MISSING;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.LOGICAL_ID_SAME;

/**
 * Test for {@link org.openehealth.ipf.commons.ihe.xds.core.validate.requests.SubmitObjectsRequestValidator}.
 * @author Boris Stanojevic
 */
public class SubmitObjectsRequestForUpdateValidatorTest {
    private SubmitObjectsRequestValidator validator = new SubmitObjectsRequestValidator();

    @Test
    public void testOKFromRealEbXML() throws Exception {
        EbXMLSubmitObjectsRequest30 request = getRequest("SubmitObjectsRequest_ebrs30_update.xml");
        validator.validate(request, ITI_57);
    }

    @Test
    public void testLid() throws Exception {
        EbXMLSubmitObjectsRequest30 request = getRequest("SubmitObjectsRequest_ebrs30_update_sameLid.xml");

        expectFailure(LOGICAL_ID_SAME, request, ITI_57);

        request.getExtrinsicObjects().get(0).setLid(null);
        expectFailure(LOGICAL_ID_MISSING, request, ITI_57);

        request.getExtrinsicObjects().get(0).setLid(request.getExtrinsicObjects().get(0).getId());
        expectFailure(LOGICAL_ID_EQUALS_ENTRY_UUID, request, ITI_57);
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLSubmitObjectsRequest ebXML, ValidationProfile profile) {
        try {
            validator.validate(ebXML, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }

    private EbXMLSubmitObjectsRequest30 getRequest(String resourcePath) throws Exception {
        File file = new File(getClass().getClassLoader().getResource(resourcePath).toURI());

        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs");
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Object unmarshalled = unmarshaller.unmarshal(file);
        SubmitObjectsRequest original = (SubmitObjectsRequest) unmarshalled;
        return  new EbXMLSubmitObjectsRequest30(original);
    }
}
