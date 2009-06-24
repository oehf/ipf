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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.AssociationType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.InternationalStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LocalizedStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ValueListType;

/**
 * Various helper methods to build and interpret EBRS 2.1.
 * @author Jens Riemschneider
 */
public class Ebrs21 {
    private final static ObjectFactory rimFactory = new ObjectFactory();
    
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
     * @return the created classification.
     */
    public static ClassificationType createClassification() {
        return rimFactory.createClassificationType();
    }
 
    /**
     * Adds a slot to the given slot list.
     * <p>
     * This method always creates a new slot, no matter if a slot with the given
     * name is already within the slot list.
     * @param slots
     *          the slots (e.g. of a classification or extrinsic object) to which the slot 
     *          is to be added
     * @param slotName
     *          the name of the slot.
     * @param slotValues
     *          the values of the slot to be added as a value list. If this list is empty 
     *          or <code>null</code> no slot will be created. Only values that are not 
     *          <code>null</code> will be added to the slot.
     */
    public static void addSlot(List<SlotType1> slots, String slotName, String... slotValues) {
        notNull(slots, "slots cannot be null");
        
        if (slotValues == null || slotValues.length == 0) {
            return;
        }
        
        ValueListType valueList = rimFactory.createValueListType();
        List<String> values = valueList.getValue();
        for (String slotValue : slotValues) {
            if (slotValue != null) {
                values.add(slotValue);
            }
        }

        if (!values.isEmpty()) {
            SlotType1 slot = rimFactory.createSlotType1();
            slot.setName(slotName);
            slot.setValueList(valueList);
            slots.add(slot);
        }
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

    /**
     * Creates an international string via a single localized string.
     * @param localized
     *          the string to convert.
     * @return the created string instance.
     */
    public static InternationalStringType createInternationalString(LocalizedString localized) {
        if (localized == null) {
            return null;
        }
        
        InternationalStringType international = rimFactory.createInternationalStringType();
        international.getLocalizedString().add(createLocalizedString(localized));
        return international;
    }

    private static LocalizedStringType createLocalizedString(LocalizedString localized) {
        LocalizedStringType localizedEbRS21 = rimFactory.createLocalizedStringType();
        localizedEbRS21.setCharset(localized.getCharset());
        localizedEbRS21.setLang(localized.getLang());
        localizedEbRS21.setValue(localized.getValue());
        return localizedEbRS21;
    }

    /**
     * Retrieves a localized string from an international string.
     * @param international
     *          the international string.
     * @param idx
     *          the index of the localized string within the international string.
     * @return the international string. Can be <code>null</code> if the index was out
     *          of bounds or if the element was null.
     */
    public static LocalizedString getLocalizedString(InternationalStringType international, int idx) {
        if (international == null) {
            return null;
        }
        
        List<LocalizedStringType> localizedList = international.getLocalizedString();
        if (idx >= localizedList.size() || idx < 0) {
            return null;
        }
        
        LocalizedStringType localizedEbRS21 = localizedList.get(idx);
        if (localizedEbRS21 == null) {
            return null;
        }
        
        LocalizedString localized = new LocalizedString();
        localized.setCharset(localizedEbRS21.getCharset());
        localized.setLang(localizedEbRS21.getLang());
        localized.setValue(localizedEbRS21.getValue());
        
        return localized;
    }

    /**
     * Creates a new ebXML 2.1 Association. 
     * @return the new association.
     */
    public static AssociationType1 createAssociation() {
        return rimFactory.createAssociationType1();
    }

    /**
     * Creates a new ebXML 2.1 extrinsic object.
     * @return the new extrinsic object.
     */
    public static ExtrinsicObjectType createExtrinsicObjectType() {
        return rimFactory.createExtrinsicObjectType();
    }

    /**
     * Creates a new ebXML 2.1 external identifier.
     * @param value 
     *          the value of the external identifier.
     * @return the new external identifier.
     */
    public static ExternalIdentifierType createExternalIdentifiable(String value) {
        if (value == null) {
            return null;
        }
        
        ExternalIdentifierType identifier = rimFactory.createExternalIdentifierType();
        identifier.setValue(value);
        return identifier;
    }
}
