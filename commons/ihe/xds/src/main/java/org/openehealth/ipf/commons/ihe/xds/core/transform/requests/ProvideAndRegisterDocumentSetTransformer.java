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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.LeafClassTransformer;

import jakarta.activation.DataHandler;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;

/**
 * Transforms between a {@link ProvideAndRegisterDocumentSet} and its ebXML representation.
 * @author Jens Riemschneider
 */
public class ProvideAndRegisterDocumentSetTransformer extends LeafClassTransformer {

    /**
     * Constructs the transformer
     * @param factory
     *          factory for version independent ebXML objects.
     */
    public ProvideAndRegisterDocumentSetTransformer(EbXMLFactory factory) {
        super(factory);
    }

    /**
     * Transforms a request into its ebXML representation.
     * @param request
     *          the request. Can be <code>null</code>.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLProvideAndRegisterDocumentSetRequest<ProvideAndRegisterDocumentSetRequestType> toEbXML(ProvideAndRegisterDocumentSet request) {
        if (request == null) {
            return null;
        }

        var library = factory.createObjectLibrary();
        var ebXML = factory.createProvideAndRegisterDocumentSetRequest(library);

        for (var doc : request.getDocuments()) {
            var docEntry = doc.getDocumentEntry();
            if (docEntry != null) {
                ebXML.addExtrinsicObject(documentEntryTransformer.toEbXML(docEntry, library));
                ebXML.addDocument(docEntry.getEntryUuid(), doc.getContent(DataHandler.class));
            }
        }

        for (var folder : request.getFolders()) {
            handleFolder(folder, ebXML, library);
        }

        handleSubmissionSet(request.getSubmissionSet(), ebXML, library);

        for (var association : request.getAssociations()) {
            ebXML.addAssociation(associationTransformer.toEbXML(association, library));
        }

        if (request.getTargetHomeCommunityId() != null) {
            ebXML.addSlot(SLOT_NAME_HOME_COMMUNITY_ID, request.getTargetHomeCommunityId());
        }

        return ebXML;
    }

    /**
     * Transforms an ebXML representation or a request.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     * @return the request. <code>null</code> if the input was <code>null</code>.
     */
    public ProvideAndRegisterDocumentSet fromEbXML(EbXMLProvideAndRegisterDocumentSetRequest<ProvideAndRegisterDocumentSetRequestType> ebXML) {
        if (ebXML == null) {
            return null;
        }

        var request = new ProvideAndRegisterDocumentSet();

        var documents = ebXML.getDocuments();
        for (var extrinsic : ebXML.getExtrinsicObjects(DocumentEntryType.STABLE.getUuid())) {
            var docEntry = documentEntryTransformer.fromEbXML(extrinsic);
            if (docEntry != null) {
                var document = new Document();
                document.setDocumentEntry(docEntry);
                if (docEntry.getEntryUuid() != null) {
                    var id = docEntry.getEntryUuid();
                    var data = documents.get(id);
                    document.setContent(DataHandler.class, data);
                }
                request.getDocuments().add(document);
            }
        }

        for (var regPackage : ebXML.getRegistryPackages(FOLDER_CLASS_NODE)) {
            request.getFolders().add(folderTransformer.fromEbXML(regPackage));
        }

        var regPackages = ebXML.getRegistryPackages(SUBMISSION_SET_CLASS_NODE);
        if (!regPackages.isEmpty()) {
            request.setSubmissionSet(submissionSetTransformer.fromEbXML(regPackages.get(0)));
        }

        for (var association : ebXML.getAssociations()) {
            request.getAssociations().add(associationTransformer.fromEbXML(association));
        }

        request.setTargetHomeCommunityId(ebXML.getSingleSlotValue(SLOT_NAME_HOME_COMMUNITY_ID));

        return request;
    }

}
