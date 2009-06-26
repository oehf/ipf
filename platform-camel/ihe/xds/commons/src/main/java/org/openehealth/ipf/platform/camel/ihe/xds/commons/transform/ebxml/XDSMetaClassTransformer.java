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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.EntryUUID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.UniqueID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.XDSMetaClass;

/**
 * Base class for transformers of {@link XDSMetaClass} and ebXML representations.
 * @param <E>
 *          the ebXML type.
 * @param <C>
 *          the {@link XDSMetaClass} type.
 * @author Jens Riemschneider
 */
public abstract class XDSMetaClassTransformer<E extends RegistryEntry, C extends XDSMetaClass> {
    private final IdentifiableTransformer identifiableTransformer = new IdentifiableTransformer();
    private final String patientIdExternalId;
    private final String patientIdLocalizedString;
    private final String uniqueIdExternalId;
    private final String uniqueIdLocalizedString;

    /**
     * Constructs the transformer using various constants from {@link Vocabulary}.
     * @param patientIdExternalId
     *          the external ID of the patient ID.
     * @param patientIdLocalizedString
     *          the localized string of the patient ID.
     * @param uniqueIdExternalId
     *          the external ID of the unique ID.
     * @param uniqueIdLocalizedString
     *          the localized string of the unique ID.
     */
    protected XDSMetaClassTransformer(
            String patientIdExternalId,
            String patientIdLocalizedString, 
            String uniqueIdExternalId,
            String uniqueIdLocalizedString) {
        
        this.patientIdExternalId = patientIdExternalId;
        this.patientIdLocalizedString = patientIdLocalizedString;
        this.uniqueIdExternalId = uniqueIdExternalId;
        this.uniqueIdLocalizedString = uniqueIdLocalizedString;
    }

    /**
     * Transforms the given {@link XDSMetaClass} into its ebXML representation.
     * @param metaData
     *          the meta data to transform.
     * @return the ebXML representation.
     */
    public E toEbXML(C metaData) {
        if (metaData == null) {
            return null;
        }
        
        EntryUUID entryUUID = metaData.getEntryUUID();
        E ebXML = createEbXMLInstance(entryUUID == null ? null : entryUUID.getValue());
        
        addAttributes(metaData, ebXML);        
        addClassifications(metaData, ebXML);
        addSlots(metaData, ebXML);
        addExternalIdentifiers(metaData, ebXML);
        
        return ebXML;
    }

    /**
     * Transforms an ebXML representation into its {@link XDSMetaClass}.
     * @param ebXML
     *          the ebXML representation.
     * @return the meta data.
     */
    public C fromEbXML(E ebXML) {
        if (ebXML == null) {
            return null;
        }
                
        C metaData = createMetaClassInstance();
        
        addAttributesFromEbXML(metaData, ebXML);        
        addClassificationsFromEbXML(metaData, ebXML);
        addSlotsFromEbXML(metaData, ebXML);
        addExternalIdentifiersFromEbXML(metaData, ebXML);
        
        return metaData;
    }
    
    /**
     * Called by the base class to create a new instance of the ebXML type. 
     * @return a new instance of the ebXML type.
     */
    protected abstract E createEbXMLInstance(String id);

    /**
     * Called by the base class to create a new instance of the {@link XDSMetaClass}. 
     * @return a new instance of the meta data type.
     */
    protected abstract C createMetaClassInstance();

    /**
     * Called by the base class to add attributes to the ebXML instance.
     * @param metaData
     *          the meta data instance containing the attributes. 
     * @param ebXML
     *          the ebXML instance receiving the attributes.
     */
    protected void addAttributes(C metaData, E ebXML) {
        ebXML.setStatus(AvailabilityStatus.toOpcode(metaData.getAvailabilityStatus()));                
        ebXML.setDescription(metaData.getComments());
        ebXML.setName(metaData.getTitle());
    }
    
    /**
     * Called by the base class to add attributes to the meta data.
     * @param metaData
     *          the meta data instance receiving the attributes. 
     * @param ebXML
     *          the ebXML instance containing the attributes.
     */
    protected void addAttributesFromEbXML(C metaData, E ebXML) {
        metaData.setAvailabilityStatus(AvailabilityStatus.valueOfOpcode(ebXML.getStatus()));        
        metaData.setComments(ebXML.getDescription());
        metaData.setTitle(ebXML.getName());
        metaData.setEntryUUID(ebXML.getId() != null ? new EntryUUID(ebXML.getId()) : null);
    }
    
    /**
     * Called by the base class to add slots to the ebXML instance.
     * @param metaData
     *          the meta data instance containing the slots. 
     * @param ebXML
     *          the ebXML instance receiving the slots.
     */
    protected void addSlots(C metaData, E ebXML) {}

    /**
     * Called by the base class to add slots to the meta data.
     * @param metaData
     *          the meta data instance receiving the slots. 
     * @param ebXML
     *          the ebXML instance containing the slots.
     */
    protected void addSlotsFromEbXML(C metaData, E ebXML) {}

    /**
     * Called by the base class to add classifications to the ebXML instance.
     * @param metaData
     *          the meta data instance containing the classifications. 
     * @param ebXML
     *          the ebXML instance receiving the classifications.
     */
    protected void addClassifications(C metaData, E ebXML) {}

    /**
     * Called by the base class to add classifications to the meta data.
     * @param metaData
     *          the meta data instance receiving the classifications. 
     * @param ebXML
     *          the ebXML instance containing the classifications.
     */
    protected void addClassificationsFromEbXML(C metaData, E ebXML) {}

    /**
     * Called by the base class to add external identifiers to the ebXML instance.
     * @param metaData
     *          the meta data instance containing the external identifiers. 
     * @param ebXML
     *          the ebXML instance receiving the external identifiers.
     */
    protected void addExternalIdentifiers(C metaData, E ebXML) {
        String patientID = identifiableTransformer.toEbXML(metaData.getPatientID());
        ebXML.addExternalIdentifier(patientID, patientIdExternalId, patientIdLocalizedString);
        
        String uniqueID = metaData.getUniqueID() != null ? metaData.getUniqueID().getValue() : null;
        ebXML.addExternalIdentifier(uniqueID, uniqueIdExternalId, uniqueIdLocalizedString);
    }

    /**
     * Called by the base class to add external identifiers to the meta data.
     * @param metaData
     *          the meta data instance receiving the external identifiers. 
     * @param ebXML
     *          the ebXML instance containing the external identifiers.
     */
    protected void addExternalIdentifiersFromEbXML(C metaData, E ebXML) {
        String patientID = ebXML.getExternalIdentifierValue(patientIdExternalId);
        metaData.setPatientID(identifiableTransformer.fromEbXML(patientID));
        
        String uniqueID = ebXML.getExternalIdentifierValue(uniqueIdExternalId);
        metaData.setUniqueID(uniqueID != null ? new UniqueID(uniqueID) : null);
    }
}
