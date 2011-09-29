/*
 * Copyright 2009-2011 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Version;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulation of {@link RegistryEntryType}.
 * @param <E>
 *          The EBXML type of the registry object.
 * @author Jens Riemschneider
 */
public abstract class EbXMLRegistryObject21<E extends RegistryObjectType> implements EbXMLRegistryObject {
    private final E registryEntry;
    private final EbXMLObjectLibrary objectLibrary;

    /**
     * Constructs the registry entry by wrapping the given ebXML 2.1 object.
     * @param registryEntry
     *          the object to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    protected EbXMLRegistryObject21(E registryEntry, EbXMLObjectLibrary objectLibrary) {
        notNull(registryEntry, "registryEntry cannot be null");
        notNull(objectLibrary, "objectLibrary cannot be null");
        this.registryEntry = registryEntry;
        this.objectLibrary = objectLibrary;
    }
    
    /**
     * @return the object library.
     */
    protected EbXMLObjectLibrary getObjectLibrary() {
        return objectLibrary;
    }

    @Override
    public void addClassification(EbXMLClassification classification, String scheme) {
        notNull(scheme, "scheme cannot be null");
     
        if (classification != null) {
            classification.setClassificationScheme(scheme);
            classification.setClassifiedObject(registryEntry.getId());
            List<ClassificationType> classifications = registryEntry.getClassification();
            classifications.add(((EbXMLClassification21)classification).getInternal());
        }
    }

    @Override
    public void addExternalIdentifier(String value, String scheme, String name) {
        notNull(name, "name cannot be null");
        notNull(scheme, "scheme cannot be null");
        
        if (value != null) {
            ExternalIdentifierType identifier = EbXMLFactory21.RIM_FACTORY.createExternalIdentifierType();
            EbXMLExternalIdentifier externalIdentifier = new EbXMLExternalIdentifier21(identifier, objectLibrary);
            externalIdentifier.setValue(value);
            externalIdentifier.setIdentificationScheme(scheme);
                        
            LocalizedString localized = new LocalizedString(name);
            externalIdentifier.setName(new EbXMLInternationalString21(localized));
            
            List<ExternalIdentifierType> externalIdentifiers = registryEntry.getExternalIdentifier();
            externalIdentifiers.add(identifier);
        }
    }

    @Override
    public List<EbXMLClassification> getClassifications() {
        List<ClassificationType> classifications = registryEntry.getClassification();
        List<EbXMLClassification> results = new ArrayList<EbXMLClassification>(classifications.size());
        for (ClassificationType classification : classifications) {
            results.add(new EbXMLClassification21(classification, objectLibrary));
        }
        return results;
    }

    @Override
    public List<EbXMLClassification> getClassifications(String scheme) {
        notNull(scheme, "scheme cannot be null");
        
        List<EbXMLClassification> results = new ArrayList<EbXMLClassification>();
        for (ClassificationType classification : registryEntry.getClassification()) {            
            Object schemeObj = classification.getClassificationScheme();
            String schemeId = objectLibrary.getByObj(schemeObj);
            if (scheme.equals(schemeId)) {
                results.add(new EbXMLClassification21(classification, objectLibrary));
            }            
        }
        return results;
    }

    @Override
    public LocalizedString getDescription() {
        InternationalStringType description = registryEntry.getDescription();
        EbXMLInternationalString encapsulated = new EbXMLInternationalString21(description);
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
    public List<EbXMLExternalIdentifier> getExternalIdentifiers() {
        List<ExternalIdentifierType> externalIdentifiers = registryEntry.getExternalIdentifier();
        List<EbXMLExternalIdentifier> results = new ArrayList<EbXMLExternalIdentifier>(externalIdentifiers.size());
        for (ExternalIdentifierType identifier : externalIdentifiers) {
            results.add(new EbXMLExternalIdentifier21(identifier, objectLibrary));
        }
        return results;
    }

    @Override
    public String getId() {
        return registryEntry.getId();
    }

    @Override
    public void setId(String id) {
        registryEntry.setId(id);
    }

    @Override
    public String getLid() {
        // Not available in 2.1
        return null;
    }

    @Override
    public void setLid(String lid) {
        // Not available in 2.1
    }

    @Override
    public Version getVersionInfo() {
        // Not available in 2.1
        return null;
    }

    @Override
    public void setVersionInfo(Version version) {
        // Not available in 2.1
    }

    @Override
    public LocalizedString getName() {
        InternationalStringType name = registryEntry.getName();
        EbXMLInternationalString encapsulated = new EbXMLInternationalString21(name);
        return encapsulated.getSingleLocalizedString();
    }

    @Override
    public String getObjectType() {
        return registryEntry.getObjectType();
    }

    @Override
    public EbXMLClassification getSingleClassification(String scheme) {
        List<EbXMLClassification> filtered = getClassifications(scheme);
        if (filtered.size() == 0) {
            return null;
        }
        
        return filtered.get(0);
    }

    @Override
    public void addSlot(String slotName, String... slotValues) {
        getSlotList().addSlot(slotName, slotValues);
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
    public List<EbXMLSlot> getSlots() {
        return getSlotList().getSlots();
    }

    @Override
    public List<EbXMLSlot> getSlots(String slotName) {
        return getSlotList().getSlots(slotName);
    }

    @Override
    public void setDescription(LocalizedString description) {        
        EbXMLInternationalString21 encapsulated = new EbXMLInternationalString21(description);
        registryEntry.setDescription(encapsulated.getInternal());
    }

    @Override
    public void setName(LocalizedString name) {
        EbXMLInternationalString21 encapsulated = new EbXMLInternationalString21(name);
        registryEntry.setName(encapsulated.getInternal());
    }

    @Override
    public void setObjectType(String objectType) {
        registryEntry.setObjectType(objectType);
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

    @Override
    public E getInternal() {
        return registryEntry;
    }

    private EbXMLSlotList21 getSlotList() {
        return new EbXMLSlotList21(registryEntry.getSlot());
    }    
}
