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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSlot30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_42;
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

    private DocumentEntry docEntry;

    @BeforeEach
    public void setUp() {
        validator = SubmitObjectsRequestValidator.getInstance();
        factory = new EbXMLFactory30();

        request = SampleData.createProvideAndRegisterDocumentSet();
        transformer = new ProvideAndRegisterDocumentSetTransformer(factory);

        docEntry = request.getDocuments().get(0).getDocumentEntry();
    }

    @Test
    public void testValidateGoodCase() {
        validator.validate(transformer.toEbXML(request), ITI_42);
    }

    @Test
    public void testValidateBadAuthorInstitution() {
        docEntry.getAuthors().get(0).getAuthorInstitution().add(new Organization(null, "LOL", null));
        expectFailure(ORGANIZATION_NAME_MISSING);
    }

    @Test
    public void testValidateTooManyAuthorPersons() {
        var ebXML = transformer.toEbXML(request);
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
    public void testValidateExactlyOneSubmissionSetMustExist() {
        var ebXML = transformer.toEbXML(request);
        var regPackage = factory.createRegistryPackage("lol", ebXML.getObjectLibrary());
        ebXML.addRegistryPackage(regPackage);
        var classification = factory.createClassification(ebXML.getObjectLibrary());
        classification.setClassifiedObject("lol");
        classification.setClassificationNode(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        ebXML.addClassification(classification);
        docEntry.setAvailabilityStatus(AvailabilityStatus.APPROVED);
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
        var ebXML = transformer.toEbXML(request);
        ebXML.getAssociations().get(0).getSlots(SLOT_NAME_SUBMISSION_SET_STATUS).get(0).getValueList().add("lol");
        expectFailure(TOO_MANY_SUBMISSION_SET_STATES, ebXML);
    }

    @Test
    public void testInvalidSubmissionSetStatus() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getAssociations().get(0).getSlots(SLOT_NAME_SUBMISSION_SET_STATUS).get(0).getValueList().set(0, "lol");
        expectFailure(INVALID_SUBMISSION_SET_STATUS, ebXML);
    }

    @Test
    public void testMandatorySubmissionSetStatus() {
        request.getAssociations().get(0).setLabel(null);
        expectFailure(SUBMISSION_SET_STATUS_MANDATORY);
    }

    @Test
    public void testMissingOriginal() {
        request.getAssociations().get(0).setTargetUuid("lol");
        expectFailure(MISSING_ORIGINAL);
    }

    @Test
    public void testSourceUUIDNotFound() {
        var association = new Association();
        association.setTargetUuid("blabla");
        association.setSourceUuid("lol");
        association.setAssociationType(AssociationType.TRANSFORM);
        request.getAssociations().add(association);
        expectFailure(SOURCE_UUID_NOT_FOUND);
    }

    @Test
    public void testSourceIsSnapshotAssociation() {
        var association = new Association();
        association.setTargetUuid("urn:uuid:e0985823-dc50-45a5-a6c8-a11a829893bd");
        association.setSourceUuid("blah");
        association.setAssociationType(AssociationType.IS_SNAPSHOT_OF);
        association.setEntryUuid("isSnapshotEntryId");
        request.getAssociations().add(association);
        expectFailure(MISSING_SNAPSHOT_ASSOCIATION);
    }

    @Test
    public void testSubmitAssociation() {
        var association = new Association();
        association.setTargetUuid("urn:uuid:aa0da13b-51b0-4c2e-868c-cef8d7e1bc3d");
        association.setSourceUuid("document01");
        association.setAssociationType(AssociationType.APPEND);
        association.setEntryUuid("apnd_assoc");
        request.getAssociations().add(association);

        var submitAssociation = new Association();
        submitAssociation.setTargetUuid("apnd_assoc1");
        submitAssociation.setSourceUuid("submissionSet01");
        submitAssociation.setAssociationType(AssociationType.SUBMIT_ASSOCIATION);
        submitAssociation.setEntryUuid("submitAssociation");
        request.getAssociations().add(submitAssociation);
        expectFailure(MISSING_ASSOCIATION);
    }

    @Test
    public void testWrongNumberOfClassifications() {
        docEntry.setFormatCode(null);
        expectFailure(WRONG_NUMBER_OF_CLASSIFICATIONS);
    }

    @Test
    public void testNoClassifiedObject() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications().get(0).setClassifiedObject(null);
        expectFailure(NO_CLASSIFIED_OBJ, ebXML);
    }

    @Test
    public void testWrongClassifiedObject() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications().get(0).setClassifiedObject("folder01");
        expectFailure(WRONG_CLASSIFIED_OBJ, ebXML);
    }

    private static boolean isAuthorClassification(EbXMLClassification classification) {
        return classification.getClassificationScheme().equals(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME) ||
               classification.getClassificationScheme().equals(Vocabulary.SUBMISSION_SET_AUTHOR_CLASS_SCHEME);
    }

    @Test
    public void testRequiredNodeRepresentationIsNull() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications().stream()
                .filter(x -> !isAuthorClassification(x))
                .findAny()
                .ifPresent(e -> e.setNodeRepresentation(null));
        expectFailure(NODE_REPRESENTATION_MISSING, ebXML);
    }

    @Test
    public void testRequiredNodeRepresentationIsEmpty() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications().stream()
                .filter(x -> !isAuthorClassification(x))
                .findAny()
                .ifPresent(ebXMLClassification -> ebXMLClassification.setNodeRepresentation(""));
        expectFailure(NODE_REPRESENTATION_MISSING, ebXML);
    }

    @Test
    public void testProhibitedNodeRepresentationIsNotEmpty() {
        var ebXML = transformer.toEbXML(request);
        var classification = ebXML.getExtrinsicObjects().get(0).getClassifications().stream()
                .filter(SubmitObjectsRequestValidatorTest::isAuthorClassification)
                .findAny()
                .orElseThrow(AssertionError::new);

        classification.setNodeRepresentation(null);
        validator.validate(ebXML, ITI_42);

        classification.setNodeRepresentation("");
        validator.validate(ebXML, ITI_42);

        classification.setNodeRepresentation("abcd");
        expectFailure(NODE_REPRESENTATION_PROHIBITED, ebXML);
    }

    @Test
    public void testCXTooManyComponents() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("rotfl^^^^^^^^^lol");
        expectFailure(CX_TOO_MANY_COMPONENTS, ebXML);
    }

    @Test
    public void testCXNeedsId() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("");
        expectFailure(CX_NEEDS_ID, ebXML);
    }

    @Test
    public void testHDMUstNotHaveNamespaceId() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("12^^^lol&12.3&ISO");
        expectFailure(HD_MUST_NOT_HAVE_NAMESPACE_ID, ebXML);
    }

    @Test
    public void testUniversalIDMustBeISO() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("12^^^&12.3&LOL");
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXML);
    }

    @Test
    public void testUniversalIDToo() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getExternalIdentifiers().get(0).setValue("12");
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXML);
    }

    @Test
    public void testHDNeedsUniversalID() {
        var ebXML = transformer.toEbXML(request);
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
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_SOURCE_PATIENT_INFO).get(0).getValueList().add("PID-lol|lol");
        expectFailure(INVALID_PID, ebXML);
    }

    @Test
    public void testUnsupportedPID() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_SOURCE_PATIENT_INFO).get(0).getValueList().add("PID-2|lol");
        expectFailure(UNSUPPORTED_PID, ebXML);
    }

    @Test
    public void testInvalidNumberFormat() {
        var ebXML = transformer.toEbXML(request);
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
        var ebXML = transformer.toEbXML(request);
        ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE).get(0).getSlots(Vocabulary.SLOT_NAME_INTENDED_RECIPIENT).get(0).getValueList().add("");
        expectFailure(RECIPIENT_EMPTY, ebXML);
    }

    @Test
    public void testInvalidRecipient() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE).get(0).getSlots(Vocabulary.SLOT_NAME_INTENDED_RECIPIENT).get(0).getValueList().add("|||");
        expectFailure(INVALID_RECIPIENT, ebXML);
    }

    @Test
    public void testSlotValueTooLong() {
        var chars = new char[EbXMLSlot30.MAX_SLOT_LENGTH + 1];
        Arrays.fill(chars, 'x');
        docEntry.setHash(String.valueOf(chars));
        expectFailure(SLOT_VALUE_TOO_LONG);
    }

    @Test
    public void testWrongNumberOfSlotValues() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_CREATION_TIME).get(0).getValueList().add("lol");
        expectFailure(WRONG_NUMBER_OF_SLOT_VALUES, ebXML);
    }

    @Test
    public void testMissingMimeType() {
        docEntry.setMimeType("");
        var ebXML = transformer.toEbXML(request);
        expectFailure(MIME_TYPE_MUST_BE_SPECIFIED, ebXML);
    }

    @Test
    public void testEmptySlotValue() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_CREATION_TIME).get(0).getValueList().set(0, null);
        expectFailure(EMPTY_SLOT_VALUE, ebXML);
    }

    @Test
    public void testInvalidTime() {
        Assertions.assertThrows(XDSMetaDataException.class, () ->
                docEntry.setCreationTime("lol"));
    }

    @Test
    public void testMultipleUriValues() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().add("second value");
        assertEquals(2, ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().size());
        expectFailure(WRONG_NUMBER_OF_SLOT_VALUES, ebXML);
    }

    @Test
    public void testEmptyUri() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().set(0, "");
        expectFailure(EMPTY_URI, ebXML);
    }

    @Test
    public void testInvalidUri() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getSlots(Vocabulary.SLOT_NAME_URI).get(0).getValueList().set(0, ":lol:");
        expectFailure(INVALID_URI, ebXML);
    }

    @Test
    public void testPersonMissingNameAndID() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME).get(0).getSlots().get(0).getValueList().set(0, "^^^^^^^^&1.2.840.113619.6.197&ISO");
        expectFailure(PERSON_MISSING_NAME_AND_ID, ebXML);
    }

    @Test
    public void testPersonHDMissing() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME).get(0).getSlots().get(0).getValueList().set(0, "lol");
        // The spec allows this case: "If component 1 (ID Number) is specified, component 9 (Assigning Authority) shall be present if available"
        validator.validate(transformer.toEbXML(request), ITI_42);
    }

    @Test
    public void testOrganizationNameMissing() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME).get(0).getSlots().get(1).getValueList().set(0, "^lol");
        expectFailure(ORGANIZATION_NAME_MISSING, ebXML);
    }

    @Test
    public void testOrganizationTooManyComponents() {
        var ebXML = transformer.toEbXML(request);
        ebXML.getExtrinsicObjects().get(0).getClassifications(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME).get(0).getSlots().get(1).getValueList().set(0, "Otto^lol");
        expectFailure(ORGANIZATION_TOO_MANY_COMPONENTS, ebXML);
    }

    @Test
    public void testRepositoryUniqueIdIsNecessaryInXDSB() {
        docEntry.setRepositoryUniqueId(null);
        expectFailure(WRONG_NUMBER_OF_SLOT_VALUES);
    }

    @Test
    public void testFolderUpdateTimeLowPrecision() {
        request.getFolders().get(0).setLastUpdateTime("20170207");
        expectFailure(TIME_PRECISION_TOO_LOW);
    }

    @Test
    public void testAuthorValidation() {
        request.getSubmissionSet().getAuthors().clear();
        var ebXml = transformer.toEbXML(request);
        ObjectContainerValidator.getInstance().validate(ebXml, ITI_42);

        var author = new Author();
        author.getAuthorRole().add(new Identifiable("clown", new AssigningAuthority("1.3.14.15", "ISO")));
        request.getSubmissionSet().getAuthors().add(author);
        ebXml = transformer.toEbXML(request);

        var failed = false;
        try {
            ObjectContainerValidator.getInstance().validate(ebXml, ITI_42);
        } catch (XDSMetaDataException e) {
            failed = true;
        }

        assertTrue(failed);
    }
    
    @Test
    public void testUniqueIdsAreNotUnique() {
        request.getFolders().get(0).setUniqueId("1.2.3");
        docEntry.setUniqueId("1.2.3");
        docEntry.setEntryUuid("doc01");
        expectFailure(UNIQUE_ID_NOT_UNIQUE);
    }

    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(request));
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLSubmitObjectsRequest<ProvideAndRegisterDocumentSetRequestType> ebXML) {
        expectFailure(expectedMessage, ebXML, ITI_42);
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLSubmitObjectsRequest<ProvideAndRegisterDocumentSetRequestType> ebXML, ValidationProfile profile) {
        try {
            validator.validate(ebXML, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
