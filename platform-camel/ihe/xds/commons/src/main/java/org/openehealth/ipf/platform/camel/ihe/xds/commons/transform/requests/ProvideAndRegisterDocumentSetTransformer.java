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
import java.util.Map;

import javax.activation.DataHandler;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Document;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.EntryUUID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.AssociationTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.DocumentEntryTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.FolderTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.SubmissionSetTransformer;

/**
 * Transforms between a {@link ProvideAndRegisterDocumentSet} and its ebXML representation.
 * @author Jens Riemschneider
 */
public class ProvideAndRegisterDocumentSetTransformer {
    private final EbXMLFactory factory;    
    private final SubmissionSetTransformer submissionSetTransformer;
    private final DocumentEntryTransformer documentEntryTransformer;
    private final FolderTransformer folderTransformer;
    private final AssociationTransformer associationTransformer;
    
    public ProvideAndRegisterDocumentSetTransformer(EbXMLFactory factory) {
        notNull(factory, "factory cannot be null");
        this.factory = factory;
        
        submissionSetTransformer = new SubmissionSetTransformer(factory);
        documentEntryTransformer = new DocumentEntryTransformer(factory);
        folderTransformer = new FolderTransformer(factory);
        associationTransformer = new AssociationTransformer(factory);
    }
    
    public ProvideAndRegisterDocumentSetRequest toEbXML(ProvideAndRegisterDocumentSet request) {
        if (request == null) {
            return null;
        }
        
        ObjectLibrary library = factory.createObjectLibrary();        
        ProvideAndRegisterDocumentSetRequest ebXML = factory.createProvideAndRegisterDocumentSetRequest(library);
        
        for (Document doc : request.getDocuments()) {
            DocumentEntry docEntry = doc.getDocumentEntry();
            if (docEntry != null) {
                ebXML.addExtrinsicObject(documentEntryTransformer.toEbXML(docEntry, library));
                EntryUUID id = docEntry.getEntryUUID();
                if (id != null) {
                    ebXML.addDocument(id.getValue(), doc.getDataHandler());
                }
            }
        }
        
        for (Folder folder : request.getFolders()) {
            ebXML.addRegistryPackage(folderTransformer.toEbXML(folder, library));
            addClassification(ebXML, folder.getEntryUUID().getValue(), Vocabulary.FOLDER_CLASS_NODE, library);
        }
        
        SubmissionSet submissionSet = request.getSubmissionSet();
        ebXML.addRegistryPackage(submissionSetTransformer.toEbXML(submissionSet, library));
        EntryUUID entryUUID = submissionSet != null ? submissionSet.getEntryUUID() : null;
        addClassification(ebXML, entryUUID != null ? entryUUID.getValue() : null, Vocabulary.SUBMISSION_SET_CLASS_NODE, library);
        
        for (Association association : request.getAssociations()) {
            ebXML.addAssociation(associationTransformer.toEbXML(association, library));
        }
        
        return ebXML;
    }

    private void addClassification(ProvideAndRegisterDocumentSetRequest ebXML, String classified, String node, ObjectLibrary library) {
        Classification classification = factory.createClassification(library);
        classification.setClassifiedObject(classified);
        classification.setClassificationNode(node);
        ebXML.addClassification(classification);
    }
    
    public ProvideAndRegisterDocumentSet fromEbXML(ProvideAndRegisterDocumentSetRequest ebXML) {
        if (ebXML == null) {
            return null;
        }
        
        ProvideAndRegisterDocumentSet request = new ProvideAndRegisterDocumentSet();
        
        Map<String, DataHandler> documents = ebXML.getDocuments();
        for (ExtrinsicObject extrinsic : ebXML.getExtrinsicObjects(Vocabulary.DOC_ENTRY_CLASS_NODE)) {
            DocumentEntry docEntry = documentEntryTransformer.fromEbXML(extrinsic);
            if (docEntry != null) {
                Document document = new Document();
                document.setDocumentEntry(docEntry);
                if (docEntry.getEntryUUID() != null) {
                    String id = docEntry.getEntryUUID().getValue();
                    document.setDataHandler(documents.get(id));
                }
                request.getDocuments().add(document);
            }
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
