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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import static org.apache.commons.lang3.Validate.notNull;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Version;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Encapsulation of {@link RegistryObjectType}.
 * @param <E>
 *          the EBXML type of the registry object.
 * @author Jens Riemschneider
 */
public abstract class EbXMLRegistryObject30<E extends RegistryObjectType> implements EbXMLRegistryObject {
    private final E registryEntry;
    private final EbXMLObjectLibrary objectLibrary;
    
    /**
     * Constructs a registry entry by wrapping the given ebXML 3.0 object. 
     * @param registryEntry
     *          the object to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    protected EbXMLRegistryObject30(E registryEntry, EbXMLObjectLibrary objectLibrary) {
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
            classification.assignUniqueId();
            List<ClassificationType> classifications = registryEntry.getClassification();
            classifications.add(((EbXMLClassification30)classification).getInternal());
        }
    }

    @Override
    public void addExternalIdentifier(String value, String scheme, String name) {
        notNull(name, "name cannot be null");
        notNull(scheme, "scheme cannot be null");

        if (value != null) {
            ExternalIdentifierType identifier = EbXMLFactory30.RIM_FACTORY.createExternalIdentifierType();
            EbXMLExternalIdentifier externalIdentifier = new EbXMLExternalIdentifier30(identifier);
            
            externalIdentifier.setValue(value);
            externalIdentifier.setIdentificationScheme(scheme);
            externalIdentifier.setRegistryObject(registryEntry.getId());
            externalIdentifier.setId("urn:uuid:" + UUID.randomUUID().toString());
                        
            LocalizedString localized = new LocalizedString(name, null, null);
            externalIdentifier.setName(new EbXMLInternationalString30(localized));
            
            List<ExternalIdentifierType> externalIdentifiers = registryEntry.getExternalIdentifier();
            externalIdentifiers.add(identifier);
        }
    }

    @Override
    public List<EbXMLClassification> getClassifications() {
        List<ClassificationType> classifications = registryEntry.getClassification();
        List<EbXMLClassification> results = new ArrayList<EbXMLClassification>(classifications.size());
        for (ClassificationType classification : classifications) {
            results.add(new EbXMLClassification30(classification));
        }
        return results;
    }

    @Override
    public List<EbXMLClassification> getClassifications(String scheme) {
        notNull(scheme, "scheme cannot be null");
        
        List<EbXMLClassification> results = new ArrayList<EbXMLClassification>();
        for (ClassificationType classification : registryEntry.getClassification()) {            
            if (scheme.equals(classification.getClassificationScheme())) {
                results.add(new EbXMLClassification30(classification));
            }            
        }
        return results;
    }

    @Override
    public LocalizedString getDescription() {
        InternationalStringType description = registryEntry.getDescription();
        EbXMLInternationalString encapsulated = new EbXMLInternationalString30(description);
        return encapsulated.getSingleLocalizedString();
    }

    @Override
    public String getExternalIdentifierValue(String scheme) {        
        for (ExternalIdentifierType identifier : registryEntry.getExternalIdentifier()) {
            if (scheme.equals(identifier.getIdentificationScheme())) {
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
            results.add(new EbXMLExternalIdentifier30(identifier));
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
        return registryEntry.getLid();
    }

    @Override
    public void setLid(String lid) {
        registryEntry.setLid(lid);
    }

    @Override
    public Version getVersionInfo() {
        VersionInfoType versionInfo = registryEntry.getVersionInfo();
        if (versionInfo == null) {
            return null;
        }

        return new Version(versionInfo.getVersionName(), versionInfo.getComment());
    }

    @Override
    public void setVersionInfo(Version version) {
        VersionInfoType versionInfo = null;
        if (version != null) {
            versionInfo = EbXMLFactory30.RIM_FACTORY.createVersionInfoType();
            versionInfo.setVersionName(version.getVersionName());
            versionInfo.setComment(version.getComment());
        }
        registryEntry.setVersionInfo(versionInfo);
    }

    @Override
    public LocalizedString getName() {
        InternationalStringType name = registryEntry.getName();
        EbXMLInternationalString encapsulated = new EbXMLInternationalString30(name);
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
        EbXMLInternationalString30 encapsulated = new EbXMLInternationalString30(description);
        registryEntry.setDescription(encapsulated.getInternal());
    }

    @Override
    public void setName(LocalizedString name) {
        EbXMLInternationalString30 encapsulated = new EbXMLInternationalString30(name);
        registryEntry.setName(encapsulated.getInternal());
    }

    @Override
    public void setObjectType(String objectType) {
        registryEntry.setObjectType(objectType);
    }

    @Override
    public String getHome() {
        return registryEntry.getHome();
    }

    @Override
    public void setHome(String home) {
        registryEntry.setHome(home);
    }

    @Override
    public E getInternal() {
        return registryEntry;
    }

    private EbXMLSlotList30 getSlotList() {
        return new EbXMLSlotList30(registryEntry.getSlot());
    }    
}
