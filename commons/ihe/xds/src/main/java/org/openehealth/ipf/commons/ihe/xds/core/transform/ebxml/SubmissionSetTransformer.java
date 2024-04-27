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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp.toHL7;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_INTENDED_RECIPIENT;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SUBMISSION_TIME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_AUTHOR_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_LIMITED_METADATA_CLASS_NODE;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_LOCALIZED_STRING_PATIENT_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_LOCALIZED_STRING_SOURCE_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_LOCALIZED_STRING_UNIQUE_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID;

/**
 * Transforms between a {@link SubmissionSet} and its ebXML representation.
 * @author Jens Riemschneider
 */
public class SubmissionSetTransformer extends XDSMetaClassTransformer<EbXMLRegistryPackage, SubmissionSet> {
    private final AuthorTransformer authorTransformer;
    private final CodeTransformer codeTransformer;

    private final RecipientTransformer recipientTransformer = new RecipientTransformer();

    /**
     * Constructs the transformer
     * @param factory
     *          factory for version independent ebXML objects.
     */
    public SubmissionSetTransformer(EbXMLFactory factory) {
        super(SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID,
                SUBMISSION_SET_LOCALIZED_STRING_PATIENT_ID,
                SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID,
                SUBMISSION_SET_LOCALIZED_STRING_UNIQUE_ID,
                SUBMISSION_SET_LIMITED_METADATA_CLASS_NODE,
                factory);
        authorTransformer = new AuthorTransformer(factory);
        codeTransformer = new CodeTransformer(factory);
    }

    @Override
    protected EbXMLRegistryPackage createEbXMLInstance(String id, EbXMLObjectLibrary objectLibrary) {
        return factory.createRegistryPackage(id, objectLibrary);
    }

    @Override
    protected SubmissionSet createMetaClassInstance() {
        return new SubmissionSet();
    }

    @Override
    protected void addAttributes(SubmissionSet metaData, EbXMLRegistryPackage ebXML, EbXMLObjectLibrary objectLibrary) {
        super.addAttributes(metaData, ebXML, objectLibrary);
        ebXML.setStatus(metaData.getAvailabilityStatus());
        ebXML.setHome(metaData.getHomeCommunityId());
    }

    @Override
    protected void addAttributesFromEbXML(SubmissionSet metaData, EbXMLRegistryPackage ebXML) {
        super.addAttributesFromEbXML(metaData, ebXML);
        metaData.setAvailabilityStatus(ebXML.getStatus());
        metaData.setHomeCommunityId(ebXML.getHome());
    }

    @Override
    protected void addSlots(SubmissionSet metaData, EbXMLRegistryPackage ebXML, EbXMLObjectLibrary objectLibrary) {
        super.addSlots(metaData, ebXML, objectLibrary);

        var slotValues = metaData.getIntendedRecipients().stream()
                .map(recipientTransformer::toEbXML)
                .toArray(String[]::new);
        ebXML.addSlot(SLOT_NAME_INTENDED_RECIPIENT, slotValues);
        ebXML.addSlot(SLOT_NAME_SUBMISSION_TIME, toHL7(metaData.getSubmissionTime()));
    }

    @Override
    protected void addSlotsFromEbXML(SubmissionSet metaData, EbXMLRegistryPackage ebXML) {
        super.addSlotsFromEbXML(metaData, ebXML);

        var recipients = metaData.getIntendedRecipients();
        recipients.addAll(ebXML.getSlotValues(SLOT_NAME_INTENDED_RECIPIENT).stream()
                .map(recipientTransformer::fromEbXML)
                .toList());

        metaData.setSubmissionTime(ebXML.getSingleSlotValue(SLOT_NAME_SUBMISSION_TIME));
    }

    @Override
    protected void addClassificationsFromEbXML(SubmissionSet set, EbXMLRegistryPackage regPackage) {
        super.addClassificationsFromEbXML(set, regPackage);

        regPackage.getClassifications(SUBMISSION_SET_AUTHOR_CLASS_SCHEME).forEach(author ->
                set.getAuthors().add(authorTransformer.fromEbXML(author)));

        var contentType = regPackage.getSingleClassification(SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME);
        set.setContentTypeCode(codeTransformer.fromEbXML(contentType));
    }

    @Override
    protected void addClassifications(SubmissionSet set, EbXMLRegistryPackage regPackage, EbXMLObjectLibrary objectLibrary) {
        super.addClassifications(set, regPackage, objectLibrary);

        set.getAuthors().stream()
                .map(author -> authorTransformer.toEbXML(author, objectLibrary))
                .forEach(authorClassification -> regPackage.addClassification(authorClassification, SUBMISSION_SET_AUTHOR_CLASS_SCHEME));

        var contentType = codeTransformer.toEbXML(set.getContentTypeCode(), objectLibrary);
        regPackage.addClassification(contentType, SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME);
    }

    @Override
    protected void addExternalIdentifiers(SubmissionSet metaData, EbXMLRegistryPackage ebXML, EbXMLObjectLibrary objectLibrary) {
        super.addExternalIdentifiers(metaData, ebXML, objectLibrary);

        ebXML.addExternalIdentifier(metaData.getSourceId(),
                SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID,
                SUBMISSION_SET_LOCALIZED_STRING_SOURCE_ID);
    }

    @Override
    protected void addExternalIdentifiersFromEbXML(SubmissionSet metaData, EbXMLRegistryPackage ebXML) {
        super.addExternalIdentifiersFromEbXML(metaData, ebXML);

        var sourceID = ebXML.getExternalIdentifierValue(SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID);
        metaData.setSourceId(sourceID);
    }
}
