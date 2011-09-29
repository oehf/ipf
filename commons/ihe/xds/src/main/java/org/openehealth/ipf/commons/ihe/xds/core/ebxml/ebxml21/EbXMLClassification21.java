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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ClassificationType;

/**
 * Encapsulation of {@link ClassificationType}.
 * @author Jens Riemschneider
 */
public class EbXMLClassification21 implements EbXMLClassification {
    private final ClassificationType classification;
    private final EbXMLObjectLibrary objectLibrary;
    
    /**
     * Constructs a classification by wrapping a ebXML 2.1 object.
     * @param classification
     *          the classification to wrap.
     * @param objectLibrary
     *          the object library to use.
     */    
    public EbXMLClassification21(ClassificationType classification, EbXMLObjectLibrary objectLibrary) {
        notNull(classification, "classification cannot be null");
        notNull(objectLibrary, "objectLibrary cannot be null");
        
        this.classification = classification;
        this.objectLibrary = objectLibrary;
    }
    
    @Override
    public String getClassificationScheme() {
        return objectLibrary.getByObj(classification.getClassificationScheme());
    }

    @Override
    public String getClassifiedObject() {
        return objectLibrary.getByObj(classification.getClassifiedObject());
    }

    @Override
    public void addSlot(String slotName, String... slotValues) {
        getSlotList().addSlot(slotName, slotValues);
    }

    @Override
    public List<EbXMLSlot> getSlots() {
        return getSlotList().getSlots();
    }

    @Override
    public List<String> getSlotValues(String slotName) {
        return getSlotList().getSlotValues(slotName);
    }
    
    @Override
    public String getSingleSlotValue(String slotName) {
        return getSlotList().getSingleSlotValue(slotName);
    }

    @Override
    public List<EbXMLSlot> getSlots(String slotName) {
        return getSlotList().getSlots(slotName);
    }
    
    @Override
    public void setClassificationScheme(String classificationScheme) {
        classification.setClassificationScheme(objectLibrary.getById(classificationScheme));
    }

    @Override
    public void setClassifiedObject(String classifiedObject) {
        classification.setClassifiedObject(objectLibrary.getById(classifiedObject));
    }

    /**
     * @return the object being wrapped by this class.
     */
    ClassificationType getInternal() {
        return classification;
    }

    @Override
    public String getNodeRepresentation() {
        return classification.getNodeRepresentation();
    }

    @Override
    public void setNodeRepresentation(String nodeRepresentation) {
        classification.setNodeRepresentation(nodeRepresentation);        
    }

    @Override
    public LocalizedString getName() {
        return getNameAsInternationalString().getSingleLocalizedString();
    }

    @Override
    public EbXMLInternationalString21 getNameAsInternationalString() {
        return new EbXMLInternationalString21(classification.getName());
    }

    @Override
    public void setName(LocalizedString name) {
        classification.setName(new EbXMLInternationalString21(name).getInternal());
    }
    
    @Override
    public void setClassificationNode(String classificationNode) {
        classification.setClassificationNode(objectLibrary.getById(classificationNode));
    }
    
    @Override
    public String getClassificationNode() {
        return objectLibrary.getByObj(classification.getClassificationNode());
    }

    private EbXMLSlotList21 getSlotList() {
        return new EbXMLSlotList21(classification.getSlot());
    }

    @Override
    public void assignUniqueId() {
        // Not supported/required in 2.1
    }
}
