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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs21;

import static org.apache.commons.lang.Validate.notNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ValueListType;

/**
 * Various helper methods to build and interpret EBRS 2.1.
 * @author Jens Riemschneider
 */
public class Ebrs21 {
    private final static ObjectFactory objectFactory = new ObjectFactory();
    
    private final static Map<String, ObjectRefType> objectLibrary;
    
    static {
        objectLibrary = new HashMap<String, ObjectRefType>();
        addObjToLib(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME);
    }

    private static void addObjToLib(String id) {
        ObjectRefType objRef = new ObjectRefType();
        objRef.setId(id);
        objectLibrary.put(id, objRef);
    }
    
    /**
     * Creates a classification with the given scheme.
     * @param scheme
     *          the scheme object.
     * @param classifiedObj
     *          the classified object.
     * @return the created classification.
     */
    public static ClassificationType createClassification(ObjectRefType scheme, Object classifiedObj) {
        notNull(classifiedObj, "classifiedObj cannot be null");
        notNull(scheme, "scheme cannot be null");
        
        ClassificationType classification = objectFactory.createClassificationType();        
        classification.setClassificationScheme(scheme);
        classification.setClassifiedObject(classifiedObj);
        classification.setNodeRepresentation("");
        return classification;
    }
 
    /**
     * Adds a slot to the given classification.
     * <p>
     * This method always creates a new slot, no matter if a slot with the given
     * name is already within the slot list.
     * @param classification
     *          the classification.
     * @param slotName
     *          the name of the slot.
     * @param slotValues
     *          the values of the slot to be added as a value list. If this list is empty or
     *          <code>null</code> no slot will be created.
     */
    public static void addSlot(ClassificationType classification, String slotName, String... slotValues) {
        notNull(slotName, "slotName cannot be null");
        notNull(classification, "classification cannot be null");
        
        if (slotValues == null || slotValues.length == 0) {
            return;
        }
        
        List<SlotType1> slots = classification.getSlot();
        SlotType1 slot = objectFactory.createSlotType1();
        slot.setName(slotName);
        ValueListType valueList = objectFactory.createValueListType();
        slot.setValueList(valueList);
        List<String> values = valueList.getValue();
        for (String slotValue : slotValues) {
            values.add(slotValue);            
        }
        slots.add(slot);
    }

    /**
     * Returns the slot values of a given slot.
     * @param classification
     *          the classification.
     * @param slotName
     *          the slot name to look for.
     * @return the list of values. The list is empty if the slot was not found.
     */
    public static List<String> getSlotValues(ClassificationType classification, String slotName) {
        notNull(classification, "classification cannot be null");
        notNull(slotName, "slotName cannot be null");
        
        List<SlotType1> slots = classification.getSlot();       
        for (SlotType1 slot : slots) {
            if (slotName.equals(slot.getName())) {
                return slot.getValueList().getValue();
            }
        }
        
        return Collections.emptyList();
    }

    /**
     * Returns an object reference for an id related to the {@link Vocabulary}.
     * @param objectId
     *          the object ID.
     * @return the object represented by the ID.
     */
    public static ObjectRefType getObjFromLib(String objectId) {
        return objectLibrary.get(objectId);
    }
    
    /**
     * Returns all the object references in the object library.
     * @return the object references in the library.
     */
    public static Collection<ObjectRefType> getObjLib() {
        return objectLibrary.values();
    }
}
