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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests;

import static org.apache.commons.lang.Validate.notNull;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.AssociationTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.DocumentEntryTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.FolderTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.SubmissionSetTransformer;

/**
 * Transforms between a {@link RegisterDocumentSet} and its ebXML representation.
 * @author Jens Riemschneider
 */
public class RegisterDocumentSetTransformer {
    private final EbXMLFactory factory;    
    private final SubmissionSetTransformer submissionSetTransformer;
    private final DocumentEntryTransformer documentEntryTransformer;
    private final FolderTransformer folderTransformer;
    private final AssociationTransformer associationTransformer;
    
    public RegisterDocumentSetTransformer(EbXMLFactory factory) {
        notNull(factory, "factory cannot be null");
        this.factory = factory;
        
        submissionSetTransformer = new SubmissionSetTransformer(factory);
        documentEntryTransformer = new DocumentEntryTransformer(factory);
        folderTransformer = new FolderTransformer(factory);
        associationTransformer = new AssociationTransformer(factory);
    }
    
    public SubmitObjectsRequest toEbXML(RegisterDocumentSet request) {
        if (request == null) {
            return null;
        }
        
        ObjectLibrary library = factory.createObjectLibrary();        
        SubmitObjectsRequest ebXML = factory.createSubmitObjectsRequest(library);
        
        for (DocumentEntry docEntry : request.getDocumentEntries()) {
            ebXML.addExtrinsicObject(documentEntryTransformer.toEbXML(docEntry, library));
        }
        
        for (Folder folder : request.getFolders()) {
            ebXML.addRegistryPackage(folderTransformer.toEbXML(folder, library));
            addClassification(ebXML, folder.getEntryUUID(), Vocabulary.FOLDER_CLASS_NODE, library);
        }
        
        SubmissionSet submissionSet = request.getSubmissionSet();
        ebXML.addRegistryPackage(submissionSetTransformer.toEbXML(submissionSet, library));
        String entryUUID = submissionSet != null ? submissionSet.getEntryUUID() : null;
        addClassification(ebXML, entryUUID, Vocabulary.SUBMISSION_SET_CLASS_NODE, library);
        
        for (Association association : request.getAssociations()) {
            ebXML.addAssociation(associationTransformer.toEbXML(association, library));
        }
        
        return ebXML;
    }

    private void addClassification(SubmitObjectsRequest ebXML, String classified, String node, ObjectLibrary library) {
        Classification classification = factory.createClassification(library);
        classification.setClassifiedObject(classified);
        classification.setClassificationNode(node);
        ebXML.addClassification(classification);
    }
    
    public RegisterDocumentSet fromEbXML(SubmitObjectsRequest ebXML) {
        if (ebXML == null) {
            return null;
        }
        
        RegisterDocumentSet request = new RegisterDocumentSet();
        
        for (ExtrinsicObject extrinsic : ebXML.getExtrinsicObjects(Vocabulary.DOC_ENTRY_CLASS_NODE)) {
            request.getDocumentEntries().add(documentEntryTransformer.fromEbXML(extrinsic));
        }

        for (RegistryPackage regPackage : ebXML.getRegistryPackages(Vocabulary.FOLDER_CLASS_NODE)) {
            request.getFolders().add(folderTransformer.fromEbXML(regPackage));
        }

        List<RegistryPackage> regPackages = ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        if (regPackages.size() > 0) {
            request.setSubmissionSet(submissionSetTransformer.fromEbXML(regPackages.get(0)));
        }
        
        for (EbXMLAssociation association : ebXML.getAssociations()) {
            request.getAssociations().add(associationTransformer.fromEbXML(association));
        }
        
        return request;
    }
}
