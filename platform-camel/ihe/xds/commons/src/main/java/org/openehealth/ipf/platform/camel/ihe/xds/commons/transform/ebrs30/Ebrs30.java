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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30;

import static org.apache.commons.lang.Validate.notNull;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.IdentifiableType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.InternationalStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.LocalizedStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.SlotType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ValueListType;

/**
 * Various helper methods to build and interpret EBRS 3.0.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class Ebrs30 {
    
    public static final QName EXTRINSIC_OBJECT_QNAME = 
        new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "ExtrinsicObject", "rim"); 
    
    private final static ObjectFactory rimFactory = new ObjectFactory();

    
    /**
     * Wraps the given intrinsic object into JAXB to insert it into an XML element list.
     * @param qname 
     *      qualified XML name of the resulting XML element. 
     * @param object
     *      object which is to be inserted into the list.
     * @return
     *      JAXB-wrapped object list item (XML artifact).
     */
    @SuppressWarnings("unchecked")
    public static JAXBElement<IdentifiableType> getJaxbElement(QName qname, IdentifiableType object) {
        return new JAXBElement<IdentifiableType>(qname, (Class)object.getClass(), object);         
    }

    
    /**
     * Creates a classification with the given scheme.
     * @param scheme
     *          the scheme object.
     * @return the created classification.
     */
    public static ClassificationType createClassification(String scheme) {
        ClassificationType classification = rimFactory.createClassificationType();
        classification.setClassificationScheme(scheme);
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
        
        ValueListType valueList = rimFactory.createValueListType();
        List<String> values = valueList.getValue();
        for (String slotValue : slotValues) {
            values.add(slotValue);            
        }
        
        SlotType1 slot = rimFactory.createSlotType1();
        slot.setName(slotName);
        slot.setValueList(valueList);

        List<SlotType1> slots = classification.getSlot();
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
        LocalizedStringType localizedEbRS30 = rimFactory.createLocalizedStringType();
        localizedEbRS30.setCharset(localized.getCharset());
        localizedEbRS30.setLang(localized.getLang());
        localizedEbRS30.setValue(localized.getValue());
        return localizedEbRS30;
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
        
        LocalizedStringType localizedEbRS30 = localizedList.get(idx);
        if (localizedEbRS30 == null) {
            return null;
        }
        
        LocalizedString localized = new LocalizedString();
        localized.setCharset(localizedEbRS30.getCharset());
        localized.setLang(localizedEbRS30.getLang());
        localized.setValue(localizedEbRS30.getValue());
        
        return localized;
    }
}
