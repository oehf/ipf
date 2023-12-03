/*
 * Copyright 2023 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.FOLDER_CLASS_NODE;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_CLASS_NODE;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class LeafClassTransformer {

    public static final String PROPERTY_SUBMISSION_SET_CLASSIFICATION_INSIDE = LeafClassTransformer.class.getName() + ".ss-cl-inside";
    public static final String PROPERTY_FOLDER_CLASSIFICATION_INSIDE = LeafClassTransformer.class.getName() + ".f-cl-inside";

    protected final EbXMLFactory factory;
    protected final SubmissionSetTransformer submissionSetTransformer;
    protected final DocumentEntryTransformer documentEntryTransformer;
    protected final FolderTransformer folderTransformer;
    protected final AssociationTransformer associationTransformer;

    /**
     * Constructs the transformer
     *
     * @param factory factory for version independent ebXML objects.
     */
    protected LeafClassTransformer(EbXMLFactory factory) {
        this.factory = requireNonNull(factory, "factory cannot be null");

        submissionSetTransformer = new SubmissionSetTransformer(factory);
        documentEntryTransformer = new DocumentEntryTransformer(factory);
        folderTransformer = new FolderTransformer(factory);
        associationTransformer = new AssociationTransformer(factory);
    }

    protected void handleSubmissionSet(
        SubmissionSet submissionSet,
        EbXMLObjectContainer container,
        EbXMLObjectLibrary library)
    {
        handleRegistryPackage(
            submissionSet,
            submissionSetTransformer,
            container,
            library,
            PROPERTY_SUBMISSION_SET_CLASSIFICATION_INSIDE,
            SUBMISSION_SET_CLASS_NODE);
    }

    protected void handleFolder(
        Folder folder,
        EbXMLObjectContainer container,
        EbXMLObjectLibrary library)
    {
        handleRegistryPackage(
            folder,
            folderTransformer,
            container,
            library,
            PROPERTY_FOLDER_CLASSIFICATION_INSIDE,
            FOLDER_CLASS_NODE);
    }

    private <T extends XDSMetaClass> void handleRegistryPackage(
        T object,
        XDSMetaClassTransformer<EbXMLRegistryPackage, T> transformer,
        EbXMLObjectContainer container,
        EbXMLObjectLibrary library,
        String classificationInsidePropertyName,
        String classificationNodeName)
    {
        if (object != null) {
            var ebXmlObject = transformer.toEbXML(object, library);
            if (Boolean.getBoolean(classificationInsidePropertyName)) {
                var classification = createClassification(object.getEntryUuid(), classificationNodeName, library);
                ebXmlObject.addClassification(classification, "");
            } else {
                addClassification(container, object.getEntryUuid(), classificationNodeName, library);
            }
            container.addRegistryPackage(ebXmlObject);
        }
    }

    protected EbXMLClassification createClassification(String classified, String node, EbXMLObjectLibrary library) {
        var classification = factory.createClassification(library);
        classification.setClassifiedObject(classified);
        classification.setClassificationNode(node);
        classification.assignUniqueId();
        return classification;
    }

    protected void addClassification(EbXMLObjectContainer ebXML, String classified, String node, EbXMLObjectLibrary library) {
        var classification = createClassification(classified, node, library);
        ebXML.addClassification(classification);
    }

}
