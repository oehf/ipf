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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;

/**
 * Encapsulation of {@link ClassificationType}.
 * @author Jens Riemschneider
 */
public class Classification21 implements Classification {
    private final static ObjectFactory rimFactory = new ObjectFactory();

    private final ClassificationType classification;
    private final ObjectLibrary objectLibrary;
    
    private Classification21(ClassificationType classification, ObjectLibrary objectLibrary) {
        notNull(classification, "classification cannot be null");
        notNull(objectLibrary, "objectLibrary cannot be null");
        
        this.classification = classification;
        this.objectLibrary = objectLibrary;
    }
    
    static Classification21 create(ObjectLibrary objectLibrary) {
        return new Classification21(rimFactory.createClassificationType(), objectLibrary);
    }
    
    static Classification create(ClassificationType classification, ObjectLibrary objectLibrary) {
        return new Classification21(classification, objectLibrary);
    }
    
    @Override
    public void addSlot(String slotName, String... slotValues) {
        if (slotValues == null || slotValues.length == 0) {
            return;
        }

        Slot21 slot21 = Slot21.create(slotName, slotValues);
        if (!slot21.getValueList().isEmpty()) {
            List<SlotType1> slots = classification.getSlot();
            slots.add(slot21.getInternal());
        }
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
    public List<Slot> getSlots() {
        List<SlotType1> slots21 = classification.getSlot();
        List<Slot> slots = new ArrayList<Slot>(slots21.size());
        for (SlotType1 slot21 : slots21) {
            slots.add(Slot21.create(slot21));
        }
        return slots;
    }

    @Override
    public List<String> getSlotValues(String slotName) {
        notNull(slotName, "slotName cannot be null");
        List<SlotType1> slots21 = classification.getSlot();
        for (SlotType1 slot21 : slots21) {
            if (slotName.equals(slot21.getName())) {
                return slot21.getValueList().getValue();
            }
        }
        
        return Collections.emptyList();
    }
    
    @Override
    public void setClassificationScheme(String classificationScheme) {
        classification.setClassificationScheme(objectLibrary.getById(classificationScheme));
    }

    @Override
    public void setClassifiedObject(String classifiedObject) {
        classification.setClassifiedObject(objectLibrary.getById(classifiedObject));
    }

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
    public InternationalString21 getNameAsInternationalString() {
        return InternationalString21.create(classification.getName());
    }

    @Override
    public void setName(LocalizedString name) {
        classification.setName(InternationalString21.create(name).getInternal());
    }
    
    @Override
    public void setClassificationNode(String classificationNode) {
        classification.setClassificationNode(objectLibrary.getById(classificationNode));
    }
    
    @Override
    public String getClassificationNode() {
        return objectLibrary.getByObj(classification.getClassificationNode());
    }
}
