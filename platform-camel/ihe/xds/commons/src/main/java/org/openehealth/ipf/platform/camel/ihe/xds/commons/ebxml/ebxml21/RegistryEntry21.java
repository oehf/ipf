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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExternalIdentifier;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.InternationalString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.InternationalStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryEntryType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;

/**
 * Encapsulation of {@link RegistryEntryType}.
 * @author Jens Riemschneider
 */
public abstract class RegistryEntry21<E extends RegistryEntryType> implements RegistryEntry {
    private final E registryEntry;
    private final ObjectLibrary objectLibrary;
    
    protected RegistryEntry21(E registryEntry, ObjectLibrary objectLibrary) {
        notNull(registryEntry, "registryEntry cannot be null");
        notNull(objectLibrary, "objectLibrary cannot be null");
        this.registryEntry = registryEntry;
        this.objectLibrary = objectLibrary;
    }

    @Override
    public void addClassification(Classification classification, String scheme) {
        notNull(scheme, "scheme cannot be null");
     
        if (classification != null) {
            classification.setClassificationScheme(scheme);
            classification.setClassifiedObject(registryEntry.getId());
            List<ClassificationType> classifications = registryEntry.getClassification();
            classifications.add(((Classification21)classification).getInternal());
        }
    }

    @Override
    public void addExternalIdentifier(String value, String scheme, String name) {
        notNull(name, "name cannot be null");
        notNull(scheme, "scheme cannot be null");

        if (value != null) {
            ExternalIdentifier externalIdentifier = ExternalIdentifier21.create(objectLibrary);
            externalIdentifier.setValue(value);
            externalIdentifier.setIdentificationScheme(scheme);
                        
            LocalizedString localized = new LocalizedString(name, null, null);
            externalIdentifier.setName(InternationalString21.create(localized));
            
            List<ExternalIdentifierType> externalIdentifiers = registryEntry.getExternalIdentifier();
            externalIdentifiers.add(((ExternalIdentifier21)externalIdentifier).getInternal());
        }
    }

    @Override
    public void addSlot(String slotName, String... slotValues) {
        if (slotValues == null || slotValues.length == 0) {
            return;
        }

        Slot21 slot21 = Slot21.create(slotName, slotValues);
        if (!slot21.getValueList().isEmpty()) {
            List<SlotType1> slots = registryEntry.getSlot();
            slots.add(slot21.getInternal());
        }
    }

    @Override
    public List<Classification> getClassifications() {
        List<ClassificationType> classifications = registryEntry.getClassification();
        List<Classification> results = new ArrayList<Classification>(classifications.size());
        for (ClassificationType classification : classifications) {
            results.add(Classification21.create(classification, objectLibrary));
        }
        return results;
    }

    @Override
    public List<Classification> getClassifications(String scheme) {
        notNull(scheme, "scheme cannot be null");
        
        List<Classification> results = new ArrayList<Classification>();
        for (ClassificationType classification : registryEntry.getClassification()) {            
            Object schemeObj = classification.getClassificationScheme();
            String schemeId = objectLibrary.getByObj(schemeObj);
            if (scheme.equals(schemeId)) {
                results.add(Classification21.create(classification, objectLibrary));
            }            
        }
        return results;
    }

    @Override
    public LocalizedString getDescription() {
        InternationalStringType description = registryEntry.getDescription();
        InternationalString encapsulated = InternationalString21.create(description);
        return encapsulated.getSingleLocalizedString();
    }

    @Override
    public String getExternalIdentifierValue(String scheme) {        
        for (ExternalIdentifierType identifier : registryEntry.getExternalIdentifier()) {
            Object schemeObj = identifier.getIdentificationScheme();
            if (scheme.equals(objectLibrary.getByObj(schemeObj))) {
                return identifier.getValue();
            }
        }
        
        return null;
    }

    @Override
    public List<ExternalIdentifier> getExternalIdentifiers() {
        List<ExternalIdentifierType> externalIdentifiers = registryEntry.getExternalIdentifier();
        List<ExternalIdentifier> results = new ArrayList<ExternalIdentifier>(externalIdentifiers.size());
        for (ExternalIdentifierType identifier : externalIdentifiers) {
            results.add(ExternalIdentifier21.create(identifier, objectLibrary));
        }
        return results;
    }

    @Override
    public String getId() {
        return registryEntry.getId();
    }

    @Override
    public LocalizedString getName() {
        InternationalStringType name = registryEntry.getName();
        InternationalString encapsulated = InternationalString21.create(name);
        return encapsulated.getSingleLocalizedString();
    }

    @Override
    public String getObjectType() {
        return registryEntry.getObjectType();
    }

    @Override
    public Classification getSingleClassification(String scheme) {
        List<Classification> filtered = getClassifications(scheme);
        if (filtered.size() == 0) {
            return null;
        }
        
        return filtered.get(0);
    }

    @Override
    public List<String> getSlotValues(String slotName) {
        notNull(slotName, "slotName cannot be null");
        List<SlotType1> slots21 = registryEntry.getSlot();
        for (SlotType1 slot21 : slots21) {
            if (slotName.equals(slot21.getName())) {
                return slot21.getValueList().getValue();
            }
        }
        
        return Collections.emptyList();
    }
    
    @Override
    public String getSingleSlotValue(String slotName) {
        List<String> slotValues = getSlotValues(slotName);
        return slotValues.size() > 0 ? slotValues.get(0) : null;
    }
    
    @Override
    public List<Slot> getSlots() {
        List<SlotType1> slots21 = registryEntry.getSlot();
        List<Slot> slots = new ArrayList<Slot>(slots21.size());
        for (SlotType1 slot21 : slots21) {
            slots.add(Slot21.create(slot21));
        }
        return slots;
    }

    @Override
    public String getStatus() {
        return registryEntry.getStatus();
    }

    @Override
    public void setDescription(LocalizedString description) {        
        InternationalString21 encapsulated = InternationalString21.create(description);
        registryEntry.setDescription(encapsulated.getInternal());
    }

    @Override
    public void setName(LocalizedString name) {
        InternationalString21 encapsulated = InternationalString21.create(name);
        registryEntry.setName(encapsulated.getInternal());
    }

    @Override
    public void setObjectType(String objectType) {
        registryEntry.setObjectType(objectType);
    }

    @Override
    public void setStatus(String status) {
        registryEntry.setStatus(status);
    }
    

    @Override
    public String getHome() {
        // Not available in 2.1
        return null;
    }

    @Override
    public void setHome(String home) {
        // Not available in 2.1
    }

    // TODO: Should be package private once the higher level is implemented
    public E getInternal() {
        return registryEntry;
    }
}
