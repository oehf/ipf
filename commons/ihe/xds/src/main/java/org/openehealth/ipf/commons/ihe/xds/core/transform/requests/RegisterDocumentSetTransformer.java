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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import static java.util.Objects.requireNonNull;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.AssociationTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.DocumentEntryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.FolderTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.SubmissionSetTransformer;

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
    
    /**
     * Constructs the transformer
     * @param factory
     *          factory for version independent ebXML objects. 
     */
    public RegisterDocumentSetTransformer(EbXMLFactory factory) {
        requireNonNull(factory, "factory cannot be null");
        this.factory = factory;
        
        submissionSetTransformer = new SubmissionSetTransformer(factory);
        documentEntryTransformer = new DocumentEntryTransformer(factory);
        folderTransformer = new FolderTransformer(factory);
        associationTransformer = new AssociationTransformer(factory);
    }

    /**
     * Transforms the request into its ebXML representation.
     * @param request
     *          the request. Can be <code>null</code>.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLSubmitObjectsRequest toEbXML(RegisterDocumentSet request) {
        requireNonNull(request, "request cannot be null");

        var ebXML = factory.createSubmitObjectsRequest();
        var library = ebXML.getObjectLibrary();
        
        for (var docEntry : request.getDocumentEntries()) {
            ebXML.addExtrinsicObject(documentEntryTransformer.toEbXML(docEntry, library));
        }
        
        for (var folder : request.getFolders()) {
            ebXML.addRegistryPackage(folderTransformer.toEbXML(folder, library));
            addClassification(ebXML, folder.getEntryUuid(), Vocabulary.FOLDER_CLASS_NODE, library);
        }

        var submissionSet = request.getSubmissionSet();
        ebXML.addRegistryPackage(submissionSetTransformer.toEbXML(submissionSet, library));
        var entryUUID = submissionSet != null ? submissionSet.getEntryUuid() : null;
        addClassification(ebXML, entryUUID, Vocabulary.SUBMISSION_SET_CLASS_NODE, library);
        
        for (var association : request.getAssociations()) {
            ebXML.addAssociation(associationTransformer.toEbXML(association, library));
        }

        return ebXML;
    }

    /**
     * Transforms the ebXML representation into a request.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     * @return the request. <code>null</code> if the input was <code>null</code>.
     */
    public RegisterDocumentSet fromEbXML(EbXMLSubmitObjectsRequest ebXML) {
        requireNonNull(ebXML, "ebXML cannot be null");

        var request = new RegisterDocumentSet();
        
        for (var extrinsic : ebXML.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND)) {
            request.getDocumentEntries().add(documentEntryTransformer.fromEbXML(extrinsic));
        }

        for (var regPackage : ebXML.getRegistryPackages(Vocabulary.FOLDER_CLASS_NODE)) {
            request.getFolders().add(folderTransformer.fromEbXML(regPackage));
        }

        var regPackages = ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        if (regPackages.size() > 0) {
            request.setSubmissionSet(submissionSetTransformer.fromEbXML(regPackages.get(0)));
        }
        
        for (var association : ebXML.getAssociations()) {
            request.getAssociations().add(associationTransformer.fromEbXML(association));
        }

        return request;
    }

    private void addClassification(EbXMLSubmitObjectsRequest ebXML, String classified, String node, EbXMLObjectLibrary library) {
        var classification = factory.createClassification(library);
        classification.setClassifiedObject(classified);
        classification.setClassificationNode(node);
        classification.assignUniqueId();
        ebXML.addClassification(classification);
    }    
}
