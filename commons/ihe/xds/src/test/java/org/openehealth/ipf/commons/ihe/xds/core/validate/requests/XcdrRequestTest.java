package org.openehealth.ipf.commons.ihe.xds.core.validate.requests;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.ihe.xds.XCDR.Interactions.ITI_80;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_41;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.UNIVERSAL_ID_TYPE_MUST_BE_ISO;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_EXTERNAL_IDENTIFIER;

/**
 * @author Dmytro Rud
 */
public class XcdrRequestTest {

    private ProvideAndRegisterDocumentSetRequestValidator validator;
    private ProvideAndRegisterDocumentSetTransformer transformer;

    @Before
    public void before() {
        validator = new ProvideAndRegisterDocumentSetRequestValidator();
        transformer = new ProvideAndRegisterDocumentSetTransformer(new EbXMLFactory30());
    }

    @Test
    public void testPatientIdValidationXcdr() {
        ProvideAndRegisterDocumentSet request = SampleData.createProvideAndRegisterDocumentSet();
        EbXMLProvideAndRegisterDocumentSetRequest ebXml;

        // correct patient ID --> both ITI-41 and ITI-80 are happy
        ebXml = transformer.toEbXML(request);
        validator.validate(ebXml, ITI_41);
        validator.validate(ebXml, ITI_80);

        // incomplete patient ID --> both ITI-41 and ITI-80 validations fail
        Identifiable wrongPatientId = new Identifiable("orphan");
        request.getSubmissionSet().setPatientId(wrongPatientId);
        request.getDocuments().forEach(document -> document.getDocumentEntry().setPatientId(wrongPatientId));
        request.getFolders().forEach(folder -> folder.setPatientId(wrongPatientId));
        ebXml = transformer.toEbXML(request);
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXml, ITI_41);
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXml, ITI_80);

        // missing patient ID --> ITI-41 validation fails, ITI-80 validation passes
        request.getSubmissionSet().setPatientId(null);
        request.getDocuments().forEach(document -> document.getDocumentEntry().setPatientId(null));
        request.getFolders().forEach(folder -> folder.setPatientId(null));
        ebXml = transformer.toEbXML(request);
        expectFailure(MISSING_EXTERNAL_IDENTIFIER, ebXml, ITI_41);
        validator.validate(ebXml, ITI_80);
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
