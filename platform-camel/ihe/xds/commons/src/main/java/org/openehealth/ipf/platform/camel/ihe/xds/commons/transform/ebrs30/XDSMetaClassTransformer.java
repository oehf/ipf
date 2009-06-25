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

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30.Ebrs30.*;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.EntryUUID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.UniqueID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.XDSMetaClass;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.IdentifiableTransformer;

/**
 * Base class for transformers of {@link XDSMetaClass} and ebXML 3.0 representations.
 * @param <E>
 *          the ebXML 3.0 type.
 * @param <C>
 *          the {@link XDSMetaClass} type.
 * @author Jens Riemschneider
 */
public abstract class XDSMetaClassTransformer<E extends RegistryObjectType, C extends XDSMetaClass> {
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
     * Transforms the given {@link XDSMetaClass} into its ebXML 3.0 representation.
     * @param metaData
     *          the meta data to transform.
     * @return the ebXML 3.0 representation.
     */
    public E toEbXML30(C metaData) {
        if (metaData == null) {
            return null;
        }
        
        E ebXML = createEbXMLInstance();
        
        addAttributes(metaData, ebXML);        
        addClassifications(metaData, ebXML);
        addSlots(metaData, ebXML);
        addExternalIdentifiers(metaData, ebXML);
        
        return ebXML;
    }

    /**
     * Transforms an ebXML 3.0 representation into its {@link XDSMetaClass}.
     * @param ebXML
     *          the ebXML 3.0 representation.
     * @return the meta data.
     */
    public C fromEbXML30(E ebXML) {
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
    protected abstract E createEbXMLInstance();

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
        ebXML.setDescription(createInternationalString(metaData.getComments()));
        ebXML.setName(createInternationalString(metaData.getTitle()));
        
        EntryUUID entryUUID = metaData.getEntryUUID();
        ebXML.setId(entryUUID != null ? entryUUID.getValue() : null);       
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
        metaData.setComments(getSingleLocalizedString(ebXML.getDescription()));
        metaData.setTitle(getSingleLocalizedString(ebXML.getName()));
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
        List<ExternalIdentifierType> identifiers = ebXML.getExternalIdentifier();
        
        String patientID = identifiableTransformer.toEbXML(metaData.getPatientID());
        addExternalIdentifier(identifiers, 
                createExternalIdentifiable(patientID), 
                patientIdExternalId, 
                patientIdLocalizedString);
        
        String uniqueID = metaData.getUniqueID() != null ? metaData.getUniqueID().getValue() : null;
        addExternalIdentifier(identifiers, 
                createExternalIdentifiable(uniqueID), 
                uniqueIdExternalId, 
                uniqueIdLocalizedString);
    }

    /**
     * Called by the base class to add external identifiers to the meta data.
     * @param metaData
     *          the meta data instance receiving the external identifiers. 
     * @param ebXML
     *          the ebXML instance containing the external identifiers.
     */
    protected void addExternalIdentifiersFromEbXML(C metaData, E ebXML) {
        List<ExternalIdentifierType> externalIdentifiers = ebXML.getExternalIdentifier();
        
        String patientID = getExternalIdentifier(externalIdentifiers, patientIdExternalId);
        metaData.setPatientID(identifiableTransformer.fromEbXML(patientID));
        
        String uniqueID = getExternalIdentifier(externalIdentifiers, uniqueIdExternalId);
        metaData.setUniqueID(uniqueID != null ? new UniqueID(uniqueID) : null);
    }
}
