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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.SlotType1;

/**
 * Encapsulation of {@link ClassificationType}.
 * @author Jens Riemschneider
 */
public class Classification30 implements Classification {
    private final static ObjectFactory rimFactory = new ObjectFactory();

    private final ClassificationType classification;
    
    private Classification30(ClassificationType classification) {
        notNull(classification, "classification cannot be null");
        
        this.classification = classification;
    }
    
    public static Classification create() {
        return new Classification30(rimFactory.createClassificationType());
    }
    
    public static Classification create(ClassificationType classification) {
        return new Classification30(classification);
    }
    
    @Override
    public void addSlot(String slotName, String... slotValues) {
        if (slotValues == null || slotValues.length == 0) {
            return;
        }

        Slot30 slot30 = Slot30.create(slotName, slotValues);
        if (!slot30.getValueList().isEmpty()) {
            List<SlotType1> slots = classification.getSlot();
            slots.add(slot30.getInternal());
        }
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
    public List<Slot> getSlots() {
        List<SlotType1> slots30 = classification.getSlot();
        List<Slot> slots = new ArrayList<Slot>(slots30.size());
        for (SlotType1 slot30 : slots30) {
            slots.add(Slot30.create(slot30));
        }
        return slots;
    }

    @Override
    public List<String> getSlotValues(String slotName) {
        notNull(slotName, "slotName cannot be null");
        List<SlotType1> slots30 = classification.getSlot();
        for (SlotType1 slot30 : slots30) {
            if (slotName.equals(slot30.getName())) {
                return slot30.getValueList().getValue();
            }
        }
        
        return Collections.emptyList();
    }
    
    @Override
    public void setClassificationScheme(String classificationScheme) {
        classification.setClassificationScheme(classificationScheme);
    }

    @Override
    public void setClassifiedObject(String classifiedObject) {
        classification.setClassifiedObject(classifiedObject);
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
    public InternationalString30 getNameAsInternationalString() {
        return InternationalString30.create(classification.getName());
    }

    @Override
    public void setName(LocalizedString name) {
        classification.setName(InternationalString30.create(name).getInternal());
    }
}
