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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30;

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary.*;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30.Ebrs30.*;

import java.util.ArrayList;
import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Recipient;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryPackageType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.SlotType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.RecipientTransformer;

/**
 * Transforms between a {@link SubmissionSet} and its ebXML 3.0 representation.
 * @author Jens Riemschneider
 */
public class SubmissionSetTransformer extends XDSMetaClassTransformer<RegistryPackageType, SubmissionSet> {
    private final AuthorTransformer authorTransformer = new AuthorTransformer();
    private final CodeTransformer codeTransformer = new CodeTransformer();
    private final RecipientTransformer recipientTransformer = new RecipientTransformer();

    public SubmissionSetTransformer() {
        super(SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID,
                SUBMISSION_SET_LOCALIZED_STRING_PATIENT_ID,
                SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID,
                SUBMISSION_SET_LOCALIZED_STRING_UNIQUE_ID);
    }

    @Override
    protected RegistryPackageType createEbXMLInstance() {
        return createRegistryPackage();
    }

    @Override
    protected SubmissionSet createMetaClassInstance() {
        return new SubmissionSet();
    }
    
    @Override
    protected void addSlots(SubmissionSet metaData, RegistryPackageType ebXML) {
        super.addSlots(metaData, ebXML);
        List<SlotType1> slots = ebXML.getSlot();
        
        List<String> slotValues = new ArrayList<String>();
        for (Recipient recipient : metaData.getIntendedRecipients()) {
            slotValues.add(recipientTransformer.toEbXML(recipient));
        }
        addSlot(slots, SLOT_NAME_INTENDED_RECIPIENT, slotValues.toArray(new String[0]));
        
        addSlot(slots, SLOT_NAME_SUBMISSION_TIME, metaData.getSubmissionTime());        
    }
    
    @Override
    protected void addSlotsFromEbXML(SubmissionSet metaData, RegistryPackageType ebXML) {
        super.addSlotsFromEbXML(metaData, ebXML);
        List<SlotType1> slots = ebXML.getSlot();
        
        List<Recipient> recipients = metaData.getIntendedRecipients();
        for (String slotValue : getSlotValues(slots, SLOT_NAME_INTENDED_RECIPIENT)) {
            recipients.add(recipientTransformer.fromEbXML(slotValue));
        }

        metaData.setSubmissionTime(getSingleSlotValue(slots, SLOT_NAME_SUBMISSION_TIME));
    }
    
    @Override
    protected void addClassificationsFromEbXML(SubmissionSet set, RegistryPackageType regPackage) {
        super.addClassificationsFromEbXML(set, regPackage);
        List<ClassificationType> classifications = regPackage.getClassification();
        
        ClassificationType author = getSingleClassification(classifications, SUBMISSION_SET_AUTHOR_CLASS_SCHEME);
        set.setAuthor(authorTransformer.fromEbXML30(author));
 
        ClassificationType contentType = getSingleClassification(classifications, SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME);
        set.setContentTypeCode(codeTransformer.fromEbXML30(contentType));
    }
    
    @Override
    protected void addClassifications(SubmissionSet set, RegistryPackageType regPackage) {
        super.addClassifications(set, regPackage);
        
        List<ClassificationType> classifications = regPackage.getClassification();
        
        ClassificationType author = authorTransformer.toEbXML30(set.getAuthor());
        addClassification(classifications, author, regPackage, SUBMISSION_SET_AUTHOR_CLASS_SCHEME);

        ClassificationType contentType = codeTransformer.toEbXML30(set.getContentTypeCode());
        addClassification(classifications, contentType, regPackage, SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME);
    }
    
    @Override
    protected void addExternalIdentifiers(SubmissionSet metaData, RegistryPackageType ebXML) {
        super.addExternalIdentifiers(metaData, ebXML);

        List<ExternalIdentifierType> identifiers = ebXML.getExternalIdentifier();
        
        addExternalIdentifier(identifiers, 
                createExternalIdentifiable(metaData.getSourceID()), 
                SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID,
                SUBMISSION_SET_LOCALIZED_STRING_SOURCE_ID);
    }
    
    @Override
    protected void addExternalIdentifiersFromEbXML(SubmissionSet metaData, RegistryPackageType ebXML) {
        super.addExternalIdentifiersFromEbXML(metaData, ebXML);
        List<ExternalIdentifierType> externalIdentifiers = ebXML.getExternalIdentifier();

        String sourceID = getExternalIdentifier(externalIdentifiers, SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID);
        metaData.setSourceID(sourceID);
    }
}
