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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml;

import static org.apache.commons.lang.Validate.notNull;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLClassification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationLabel;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;

/**
 * Transforms an {@link Association} to its ebXML representation.
 * @author Jens Riemschneider
 */
public class AssociationTransformer {
    private final EbXMLFactory factory;
    private final CodeTransformer codeTransformer;
    
    /**
     * Constructs the transformer
     * @param factory
     *          factory for version independent ebXML objects. 
     */
    public AssociationTransformer(EbXMLFactory factory) {
        notNull(factory, "factory cannot be null");
        this.factory = factory;
        this.codeTransformer = new CodeTransformer(factory);
    }
    
    /**
     * Transforms the given association to its ebXML representation.
     * @param association
     *          the association to transform. Can be <code>null</code>.
     * @param objectLibrary 
     *          the object library.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLAssociation toEbXML(Association association, EbXMLObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objectLibrary cannot be null");
        if (association == null) {
            return null;
        }
        
        EbXMLAssociation result = factory.createAssociation(association.getEntryUuid(), objectLibrary);
        result.setAssociationType(association.getAssociationType());
        result.setSource(association.getSourceUuid());
        result.setTarget(association.getTargetUuid());
        
        String label = AssociationLabel.toOpcode(association.getLabel());
        result.addSlot(Vocabulary.SLOT_NAME_SUBMISSION_SET_STATUS, label);
        
        EbXMLClassification contentType = codeTransformer.toEbXML(association.getDocCode(), objectLibrary);
        result.addClassification(contentType, Vocabulary.ASSOCIATION_DOC_CODE_CLASS_SCHEME);
        
        return result;
    }
    
    /**
     * Transforms the given ebXML representation into an {@link Association}. 
     * @param association
     *          the ebXML association. Can be <code>null</code>.
     * @return the created {@link Association} instance. <code>null</code> if the input 
     *          was <code>null</code>.
     */
    public Association fromEbXML(EbXMLAssociation association) {
        if (association == null) {
            return null;
        }
        
        Association result = new Association();
        result.setAssociationType(association.getAssociationType());
        result.setTargetUuid(association.getTarget());
        result.setSourceUuid(association.getSource());
        result.setEntryUuid(association.getId());
        
        String label = association.getSingleSlotValue(Vocabulary.SLOT_NAME_SUBMISSION_SET_STATUS);
        result.setLabel(AssociationLabel.fromOpcode(label));
        
        EbXMLClassification docCode = association.getSingleClassification(Vocabulary.ASSOCIATION_DOC_CODE_CLASS_SCHEME);
        result.setDocCode(codeTransformer.fromEbXML(docCode));
        
        return result;
    }
}
