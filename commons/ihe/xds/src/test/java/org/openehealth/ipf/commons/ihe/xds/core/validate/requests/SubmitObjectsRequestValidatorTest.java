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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SUBMISSION_SET_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;

/**
 * Test for {@link SubmitObjectsRequestValidator}.
 * @author Jens Riemschneider
 */
public class SubmitObjectsRequestValidatorTest {
    private SubmitObjectsRequestValidator validator;    
    private EbXMLFactory factory;
    private ProvideAndRegisterDocumentSet request;
    private ProvideAndRegisterDocumentSetTransformer transformer;
    private ValidationProfile profile = new ValidationProfile(IpfInteractionId.ITI_42);

    private DocumentEntry docEntry;

    @Before
    public void setUp() {
        validator = new SubmitObjectsRequestValidator();
        factory = new EbXMLFactory21();
        
        request = SampleData.createProvideAndRegisterDocumentSet();
        transformer = new ProvideAndRegisterDocumentSetTransformer(factory);

        docEntry = request.getDocuments().get(0).getDocumentEntry();
    }
    
    @Test
    public void testValidateGoodCase() {
        validator.validate(transformer.toEbXML(request), profile);
    }
    
    @Test
    public void testValidateBadAuthorInstitution() {
        docEntry.getAuthors().get(0).getAuthorInstitution().add(new Organization(null, "LOL", null));
        expectFailure(ORGANIZATION_NAME_MISSING);            
    }

    @Test
    public void testValidateTooManyAuthorPersons() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications().get(0).getSlots().get(0).getValueList().add("bla");
        expectFailure(WRONG_NUMBER_OF_SLOT_VALUES, ebXML);
    }
    
    @Test
    public void testValidateFolderHasInvalidAvailabilityStatus() {
        request.getFolders().get(0).setAvailabilityStatus(AvailabilityStatus.DEPRECATED);
        expectFailure(FOLDER_INVALID_AVAILABILITY_STATUS);
    }

    @Test
    public void testValidateSubmissionSetHasInvalidAvailabilityStatus() {
        request.getSubmissionSet().setAvailabilityStatus(AvailabilityStatus.DEPRECATED);
        expectFailure(SUBMISSION_SET_INVALID_AVAILABILITY_STATUS);
    }

    @Test
    public void testValidateDocumentEntryHasInvalidAvailabilityStatus() {
        docEntry.setAvailabilityStatus(AvailabilityStatus.SUBMITTED);
        expectFailure(DOC_ENTRY_INVALID_AVAILABILITY_STATUS);
    }
    
    @Test
    public void testValidateExactlyOneSubmissionSetMustExist() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        EbXMLRegistryPackage regPackage = factory.createRegistryPackage("lol", ebXML.getObjectLibrary());
        ebXML.addRegistryPackage(regPackage);
        EbXMLClassification classification = factory.createClassification(ebXML.getObjectLibrary());
        classification.setClassifiedObject("lol");
        classification.setClassificationNode(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        ebXML.addClassification(classification);
        docEntry.setAvailabilityStatus(AvailabilityStatus.SUBMITTED);
        expectFailure(EXACTLY_ONE_SUBMISSION_SET_MUST_EXIST, ebXML);
    }
    
    @Test
    public void testInvalidTitleEncoding() {
        docEntry.setTitle(new LocalizedString("lol", "lol", "lol"));
        expectFailure(INVALID_TITLE_ENCODING);
    }

    @Test
    public void testTitleTooLong() {
        docEntry.setTitle(new LocalizedString("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"));
        expectFailure(TITLE_TOO_LONG);
    }

    @Test
    public void testUniqueIdMissing() {
        request.getFolders().get(0).setUniqueId(null);
        expectFailure(UNIQUE_ID_MISSING);
    }

    @Test
    public void testUniqueIdTooLong() {
        request.getFolders().get(0).setUniqueId("123456789012345678901234567890123456789012345678901234567890123451234567890123456789012345678901234567890123456789012345678901234");
        expectFailure(UNIQUE_ID_TOO_LONG);
    }

    @Test
    public void testUUIDNotUnique() {
        request.getFolders().get(0).setEntryUuid("id");
        docEntry.setEntryUuid("id");
        expectFailure(UUID_NOT_UNIQUE);
    }

    @Test
    public void testDocEntryPatientIdWrong() {
        docEntry.setPatientId(new Identifiable("lol", new AssigningAuthority("1.3")));
        expectFailure(DOC_ENTRY_PATIENT_ID_WRONG);
    }
    
    @Test
    public void testFolderPatientIdWrong() {
        request.getFolders().get(0).setPatientId(new Identifiable("lol", new AssigningAuthority("1.3")));
        expectFailure(FOLDER_PATIENT_ID_WRONG);
    }
    
    @Test
    public void testInvalidAssociationType() {
        request.getAssociations().get(0).setAssociationType(null);
        expectFailure(INVALID_ASSOCIATION_TYPE);
    }

    @Test
    public void testHasMemberShouldNotHaveDocCode() {
        request.getAssociations().get(0).setDocCode(new Code("docCode1", new LocalizedString("docCode1"), "docScheme1"));
        expectFailure(DOC_CODE_NOT_ALLOWED_ON_HAS_MEMBER);
    }

    @Test
    public void testTooManySubmissionSetStates() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getAssociations().get(0).getSlots(SLOT_NAME_SUBMISSION_SET_STATUS).get(0).getValueList().add("lol");
        expectFailure(TOO_MANY_SUBMISSION_SET_STATES, ebXML);
    }

    @Test
    public void testInvalidSubmissionSetStatus() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getAssociations().get(0).getSlots(SLOT_NAME_SUBMISSION_SET_STATUS).get(0).getValueList().set(0, "lol");
        expectFailure(INVALID_SUBMISSION_SET_STATUS, ebXML);
    }
    
    @Test
    public void testMissingOriginal() {
        request.getAssociations().get(0).setTargetUuid("lol");
        expectFailure(MISSING_ORIGINAL);
    }
    
    @Test
    public void testSourceUUIDNotFound() {
        Association association = new Association();
        association.setTargetUuid("blabla");
        association.setSourceUuid("lol");
        association.setAssociationType(AssociationType.TRANSFORM);
        request.getAssociations().add(association);       
        expectFailure(SOURCE_UUID_NOT_FOUND);
    }

    @Test    
    public void testWrongNumberOfClassifications() {
        docEntry.setFormatCode(null);
        expectFailure(WRONG_NUMBER_OF_CLASSIFICATIONS);
    }
    
    @Test    
    public void testNoClassifiedObject() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications().get(0).setClassifiedObject("lol");
        expectFailure(NO_CLASSIFIED_OBJ, ebXML);
    }
    
    @Test    
    public void testWrongClassifiedObject() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications().get(0).setClassifiedObject("folder01");
        expectFailure(WRONG_CLASSIFIED_OBJ, ebXML);
    }
    
    @Test    
    public void testWrongNodeRepresentation() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications().get(0).setNodeRepresentation(null);
        expectFailure(WRONG_NODE_REPRESENTATION, ebXML);
    }
    
    @Test    
    public void testCXTooManyComponents() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("rotfl^^^^^^^^^lol");
        expectFailure(CX_TOO_MANY_COMPONENTS, ebXML);
    }
    
    @Test    
    public void testCXNeedsId() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("");
        expectFailure(CX_NEEDS_ID, ebXML);
    }
    
    @Test    
    public void testHDMUstNotHaveNamespaceId() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("12^^^lol&12.3&ISO");
        expectFailure(HD_MUST_NOT_HAVE_NAMESPACE_ID, ebXML);
    }
    
    @Test    
    public void testUniversalIDMustBeISO() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("12^^^&12.3&LOL");
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXML);
    }
    
    @Test    
    public void testUniversalIDToo() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("12");
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXML);
    }

    @Test
    public void testHDNeedsUniversalID() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("12^^^&&ISO");
        expectFailure(HD_NEEDS_UNIVERSAL_ID, ebXML);
    }

    @Test    
    public void testMissingExternalIdentifier() {
        request.getSubmissionSet().setSourceId(null);
        expectFailure(MISSING_EXTERNAL_IDENTIFIER);
    }
    
    @Test    
    public void testInvalidHashCode() {
        docEntry.setHash("lol");
        expectFailure(INVALID_HASH_CODE);
    }
    
    @Test    
    public void testInvalidLanguageCode() {
        docEntry.setLanguageCode("123lol");
        expectFailure(INVALID_LANGUAGE_CODE);
    }
    
    @Test    
    public void testOIDTooLong() {
        request.getSubmissionSet().setSourceId("12345678901234567890123456789012345678901234567890123456789012345");
        expectFailure(OID_TOO_LONG);
    }
    
    @Test    
    public void testInvalidOID() {
        request.getSubmissionSet().setSourceId("lol");
        expectFailure(INVALID_OID);
    }
        
    @Test    
    public void testInvalidPID() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_SOURCE_PATIENT_INFO).get(0).getValueList().add("PID-lol|lol");
        expectFailure(INVALID_PID, ebXML);
    }
    
    @Test    
    public void testUnsupportedPID() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_SOURCE_PATIENT_INFO).get(0).getValueList().add("PID-2|lol");
        expectFailure(UNSUPPORTED_PID, ebXML);
    }
    
    @Test    
    public void testInvalidNumberFormat() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_SIZE).get(0).getValueList().set(0, "lol");
        expectFailure(INVALID_NUMBER_FORMAT, ebXML);
    }
    
//  This check is disabled for compatibility with older versions.
//    @Test    
//    public void testRecipientListEmpty() {
//        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
//        ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE).get(0).getSlots(Vocabulary.SLOT_NAME_INTENDED_RECIPIENT).get(0).getValueList().clear();
//        expectFailure(RECIPIENT_LIST_EMPTY, ebXML);
//    }
    
    @Test    
    public void testRecipientEmpty() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE).get(0).getSlots(Vocabulary.SLOT_NAME_INTENDED_RECIPIENT).get(0).getValueList().add("");
        expectFailure(RECIPIENT_EMPTY, ebXML);
    }
    
    @Test    
    public void testInvalidRecipient() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE).get(0).getSlots(Vocabulary.SLOT_NAME_INTENDED_RECIPIENT).get(0).getValueList().add("|||");
        expectFailure(INVALID_RECIPIENT, ebXML);
    }
    
    @Test    
    public void testSlotValueTooLong() {
        docEntry.setCreationTime("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456");
        expectFailure(SLOT_VALUE_TOO_LONG);
    }
    
    @Test    
    public void testWrongNumberOfSlotValues() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_CREATION_TIME).get(0).getValueList().add("lol");
        expectFailure(WRONG_NUMBER_OF_SLOT_VALUES, ebXML);
    }
    
    @Test    
    public void testEmptySlotValue() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_CREATION_TIME).get(0).getValueList().set(0, null);
        expectFailure(EMPTY_SLOT_VALUE, ebXML);
    }
    
    @Test    
    public void testInvalidTime() {
        docEntry.setCreationTime("lol");
        expectFailure(INVALID_TIME);
    }
    
    @Test    
    public void testNullUri() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().set(0, null);
        expectFailure(NULL_URI, ebXML);
    }
    
    @Test    
    public void testEmptyUri() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().set(0, "");
        expectFailure(EMPTY_URI, ebXML);
    }
    
    @Test    
    public void testInvalidUri() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().set(0, ":lol:");
        expectFailure(INVALID_URI, ebXML);
    }
    
    @Test    
    public void testNullUriPart() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().add(null);
        expectFailure(NULL_URI_PART, ebXML);
    }
    
    @Test    
    public void testInvalidUriPart() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().add("lol|");
        expectFailure(INVALID_URI_PART, ebXML);
    }
    
    @Test    
    public void testMissingUriPart() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().set(0, "2|lol");
        expectFailure(MISSING_URI_PART, ebXML);
    }
    
    @Test    
    public void testPersonMissingNameAndID() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME).get(0).getSlots().get(0).getValueList().set(0, "^^^^^^^^&1.2.840.113619.6.197&ISO");
        expectFailure(PERSON_MISSING_NAME_AND_ID, ebXML);
    }
    
    @Test    
    public void testPersonHDMissing() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME).get(0).getSlots().get(0).getValueList().set(0, "lol");
        // The spec allows this case: "If component 1 (ID Number) is specified, component 9 (Assigning Authority) shall be present if available"
        validator.validate(transformer.toEbXML(request), profile);
    }
        
    @Test    
    public void testOrganizationNameMissing() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME).get(0).getSlots().get(1).getValueList().set(0, "^lol");
        expectFailure(ORGANIZATION_NAME_MISSING, ebXML);
    }
    
    @Test    
    public void testOrganizationTooManyComponents() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME).get(0).getSlots().get(1).getValueList().set(0, "Otto^lol");
        expectFailure(ORGANIZATION_TOO_MANY_COMPONENTS, ebXML);
    }

    @Test
    public void testRepositoryUniqueIdIsNecessaryInXDSB() {
        docEntry.setRepositoryUniqueId(null);
        expectFailure(WRONG_NUMBER_OF_SLOT_VALUES);
    }

    @Test
    public void testAuthorValidation() {
        request.getSubmissionSet().getAuthors().clear();
        EbXMLProvideAndRegisterDocumentSetRequest ebXml = transformer.toEbXML(request);
        new ObjectContainerValidator().validate(ebXml, profile);

        Author author = new Author();
        author.getAuthorRole().add("clown");
        request.getSubmissionSet().setAuthor(author);
        ebXml = transformer.toEbXML(request);

        boolean failed = false;
        try {
            new ObjectContainerValidator().validate(ebXml, profile);
        } catch (XDSMetaDataException e) {
            failed = true;
        }

        assertTrue(failed);
    }


    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(request));
    }

    private void expectFailure(ValidationMessage expectedMessage, ValidationProfile profile) {
        expectFailure(expectedMessage, transformer.toEbXML(request), profile);
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLSubmitObjectsRequest ebXML) {
        expectFailure(expectedMessage, ebXML, profile);
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
}
