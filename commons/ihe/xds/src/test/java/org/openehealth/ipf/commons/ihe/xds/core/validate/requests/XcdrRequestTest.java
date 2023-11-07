/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.openehealth.ipf.commons.ihe.xds.XCDR.Interactions.ITI_80;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_41;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_EXTERNAL_IDENTIFIER;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.UNIVERSAL_ID_TYPE_MUST_BE_ISO;

/**
 * @author Dmytro Rud
 */
public class XcdrRequestTest {

    private ProvideAndRegisterDocumentSetRequestValidator validator;
    private ProvideAndRegisterDocumentSetTransformer transformer;

    @BeforeEach
    public void before() {
        validator = ProvideAndRegisterDocumentSetRequestValidator.getInstance();
        transformer = new ProvideAndRegisterDocumentSetTransformer(new EbXMLFactory30());
    }

    @Test
    public void testPatientIdValidationXcdr() {
        var request = SampleData.createProvideAndRegisterDocumentSet();
        var ebXml = transformer.toEbXML(request);
        validator.validate(ebXml, ITI_41);
        validator.validate(ebXml, ITI_80);

        // incomplete patient ID --> both ITI-41 and ITI-80 validations fail
        var wrongPatientId = new Identifiable("orphan");
        request.getSubmissionSet().setPatientId(wrongPatientId);
        request.getDocuments().forEach(document -> document.getDocumentEntry().setPatientId(wrongPatientId));
        request.getFolders().forEach(folder -> folder.setPatientId(wrongPatientId));

        var ebXml2 = transformer.toEbXML(request);
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXml2, ITI_41);
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXml2, ITI_80);

        // missing patient ID --> ITI-41 validation fails, ITI-80 validation passes
        request.getSubmissionSet().setPatientId(null);
        request.getDocuments().forEach(document -> document.getDocumentEntry().setPatientId(null));
        request.getFolders().forEach(folder -> folder.setPatientId(null));

        var ebXml3 = transformer.toEbXML(request);
        expectFailure(MISSING_EXTERNAL_IDENTIFIER, ebXml3, ITI_41);
        validator.validate(ebXml3, ITI_80);
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLProvideAndRegisterDocumentSetRequest<ProvideAndRegisterDocumentSetRequestType> ebXML, ValidationProfile profile) {
        try {
            validator.validate(ebXML, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }

}
