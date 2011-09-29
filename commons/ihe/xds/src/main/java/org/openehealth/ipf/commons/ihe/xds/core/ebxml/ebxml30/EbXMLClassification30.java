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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;
import java.util.UUID;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ClassificationType;

/**
 * Encapsulation of {@link ClassificationType}.
 * @author Jens Riemschneider
 */
public class EbXMLClassification30 implements EbXMLClassification {
    private final ClassificationType classification;
    
    /**
     * Constructs a classification by wrapping the given ebXML 3.0 object.
     * @param classification
     *          the object to wrap.
     */
    public EbXMLClassification30(ClassificationType classification) {
        notNull(classification, "classification cannot be null");        
        this.classification = classification;
    }
    
    @Override
    public String getClassificationScheme() {
        return classification.getClassificationScheme();
    }

    @Override
    public String getClassifiedObject() {
        return classification.getClassifiedObject();
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
    public void addSlot(String slotName, String... slotValues) {
        getSlotList().addSlot(slotName, slotValues);
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
        classification.setClassificationScheme(classificationScheme);
    }

    @Override
    public void setClassifiedObject(String classifiedObject) {
        classification.setClassifiedObject(classifiedObject);
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
    public EbXMLInternationalString30 getNameAsInternationalString() {
        return new EbXMLInternationalString30(classification.getName());
    }

    @Override
    public void setName(LocalizedString name) {
        classification.setName(new EbXMLInternationalString30(name).getInternal());
    }
    
    @Override
    public void setClassificationNode(String classificationNode) {
        classification.setClassificationNode(classificationNode);
    }
    
    @Override
    public String getClassificationNode() {
        return classification.getClassificationNode();
    }

    /**
     * @return the ebXML 3.0 object being wrapped by this class.
     */
    ClassificationType getInternal() {
        return classification;
    }

    private EbXMLSlotList30 getSlotList() {
        return new EbXMLSlotList30(classification.getSlot());
    }

    @Override
    public void assignUniqueId() {
        classification.setId("urn:uuid:" + UUID.randomUUID().toString());
    }
}
