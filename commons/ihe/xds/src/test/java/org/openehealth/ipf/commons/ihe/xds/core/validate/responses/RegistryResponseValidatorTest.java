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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.ResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.*;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;

/**
 * Tests for {@link RegistryResponseValidator}.
 * @author Jens Riemschneider
 */
public class RegistryResponseValidatorTest {
    private RegistryResponseValidator validator;
    private Response response;
    private ResponseTransformer transformer;
    private ValidationProfile profile;

    @Before
    public void setUp() {
        validator = new RegistryResponseValidator();
        EbXMLFactory factory = new EbXMLFactory30();
        transformer = new ResponseTransformer(factory);
        response = SampleData.createResponse();
        profile = new ValidationProfile(IpfInteractionId.ITI_18);
    }

    @Test
    public void testGoodCase() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(response), profile);
    }
    
    @Test
    public void testInvalidStatus() {
        response.setStatus(null);
        expectFailure(INVALID_STATUS_IN_RESPONSE);
    }
    
    @Test
    public void testInvalidErrorCode() {
        response.getErrors().add(new ErrorInfo(null, null, Severity.ERROR, null, null));
        expectFailure(INVALID_ERROR_CODE_IN_RESPONSE);
    }    

    @Test
    public void testInvalidSeverity() {
        EbXMLRegistryResponse ebXML = transformer.toEbXML(response);
        ((RegistryResponseType)ebXML.getInternal()).getRegistryErrorList().getRegistryError().get(0).setSeverity("lol");
        expectFailure(INVALID_SEVERITY_IN_RESPONSE, ebXML);
    }    

    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(response));
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLRegistryResponse ebXMLRegistryResponse) {
        try {
            validator.validate(ebXMLRegistryResponse, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
