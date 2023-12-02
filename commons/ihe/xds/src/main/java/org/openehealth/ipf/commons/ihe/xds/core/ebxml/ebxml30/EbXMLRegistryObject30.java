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

import lombok.experimental.Delegate;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Version;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

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
        this.registryEntry = requireNonNull(registryEntry, "registryEntry cannot be null");
        this.objectLibrary = requireNonNull(objectLibrary, "objectLibrary cannot be null");
    }

    /**
     * @return the object library.
     */
    protected EbXMLObjectLibrary getObjectLibrary() {
        return objectLibrary;
    }

    @Override
    public void addClassification(EbXMLClassification classification, String scheme) {
        requireNonNull(scheme, "scheme cannot be null");

        if (classification != null) {
            classification.setClassificationScheme(scheme);
            classification.setClassifiedObject(registryEntry.getId());
            classification.assignUniqueId();
            var classifications = registryEntry.getClassification();
            classifications.add(((EbXMLClassification30)classification).getInternal());
        }
    }

    @Override
    public void addExternalIdentifier(String value, String scheme, String name) {
        requireNonNull(name, "name cannot be null");
        requireNonNull(scheme, "scheme cannot be null");

        if (value != null) {
            var identifier = EbXMLFactory30.RIM_FACTORY.createExternalIdentifierType();
            EbXMLExternalIdentifier externalIdentifier = new EbXMLExternalIdentifier30(identifier);

            externalIdentifier.setValue(value);
            externalIdentifier.setIdentificationScheme(scheme);
            externalIdentifier.setRegistryObject(registryEntry.getId());
            externalIdentifier.setId("urn:uuid:" + UUID.randomUUID());

            var localized = new LocalizedString(name, null, null);
            externalIdentifier.setName(new EbXMLInternationalString30(localized));

            var externalIdentifiers = registryEntry.getExternalIdentifier();
            externalIdentifiers.add(identifier);
        }
    }

    @Override
    public List<EbXMLClassification> getClassifications() {
        var classifications = registryEntry.getClassification();
        return classifications.stream()
                .map(EbXMLClassification30::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<EbXMLClassification> getClassifications(String scheme) {
        requireNonNull(scheme, "scheme cannot be null");

        return registryEntry.getClassification().stream()
                .filter(classification -> scheme.equals(classification.getClassificationScheme()))
                .map(EbXMLClassification30::new)
                .collect(Collectors.toList());
    }

    @Override
    public LocalizedString getDescription() {
        var description = registryEntry.getDescription();
        EbXMLInternationalString encapsulated = new EbXMLInternationalString30(description);
        return encapsulated.getSingleLocalizedString();
    }

    @Override
    public String getExternalIdentifierValue(String scheme) {
        for (var identifier : registryEntry.getExternalIdentifier()) {
            if (scheme.equals(identifier.getIdentificationScheme())) {
                return identifier.getValue();
            }
        }

        return null;
    }

    @Override
    public List<EbXMLExternalIdentifier> getExternalIdentifiers() {
        var externalIdentifiers = registryEntry.getExternalIdentifier();
        return externalIdentifiers.stream()
                .map(EbXMLExternalIdentifier30::new)
                .collect(Collectors.toList());
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
        var versionInfo = registryEntry.getVersionInfo();
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
        var name = registryEntry.getName();
        EbXMLInternationalString encapsulated = new EbXMLInternationalString30(name);
        return encapsulated.getSingleLocalizedString();
    }

    @Override
    public String getObjectType() {
        return registryEntry.getObjectType();
    }

    @Override
    public EbXMLClassification getSingleClassification(String scheme) {
        var filtered = getClassifications(scheme);
        if (filtered.isEmpty()) {
            return null;
        }

        return filtered.get(0);
    }

    @Override
    public void setDescription(LocalizedString description) {
        var encapsulated = new EbXMLInternationalString30(description);
        registryEntry.setDescription(encapsulated.getInternal());
    }

    @Override
    public void setName(LocalizedString name) {
        var encapsulated = new EbXMLInternationalString30(name);
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

    /**
     * Implements the {@link org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlotList} interface
     * by delegating the calls to a "proper" slot list.
     */
    @Delegate
    private EbXMLSlotList30 getSlotList() {
        return new EbXMLSlotList30(registryEntry.getSlot());
    }
}
