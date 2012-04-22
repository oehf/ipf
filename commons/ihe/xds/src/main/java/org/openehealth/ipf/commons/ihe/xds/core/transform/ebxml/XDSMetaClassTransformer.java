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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import static org.apache.commons.lang3.Validate.notNull;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

/**
 * Base class for transformers of {@link XDSMetaClass} and ebXML representations.
 * @param <E>
 *          the ebXML type.
 * @param <C>
 *          the {@link XDSMetaClass} type.
 * @author Jens Riemschneider
 */
public abstract class XDSMetaClassTransformer<E extends EbXMLRegistryObject, C extends XDSMetaClass> {
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
     *          the meta data to transform. Can be <code>null</code>.
     * @param objectLibrary
     *          the object library.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public E toEbXML(C metaData, EbXMLObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objectLibrary cannot be null");
        
        if (metaData == null) {
            return null;
        }
        
        E ebXML = createEbXMLInstance(metaData.getEntryUuid(), objectLibrary);
        
        addAttributes(metaData, ebXML, objectLibrary);        
        addClassifications(metaData, ebXML, objectLibrary);
        addSlots(metaData, ebXML, objectLibrary);
        addExternalIdentifiers(metaData, ebXML, objectLibrary);
        
        return ebXML;
    }

    /**
     * Transforms an ebXML representation into its {@link XDSMetaClass}.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     * @return the meta data. <code>null</code> if the input was <code>null</code>.
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
     * @param id
     *          the id of the object to create.
     * @param objectLibrary
     *          the object library. 
     * @return a new instance of the ebXML type.
     */
    protected abstract E createEbXMLInstance(String id, EbXMLObjectLibrary objectLibrary);

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
     * @param objectLibrary 
     *          the object library.
     */
    protected void addAttributes(C metaData, E ebXML, EbXMLObjectLibrary objectLibrary) {
        ebXML.setDescription(metaData.getComments());
        ebXML.setName(metaData.getTitle());
        ebXML.setLid(metaData.getLogicalUuid());
        ebXML.setVersionInfo(metaData.getVersion());
    }

    /**
     * Called by the base class to add attributes to the meta data.
     * @param metaData
     *          the meta data instance receiving the attributes.
     * @param ebXML
     *          the ebXML instance containing the attributes.
     */
    protected void addAttributesFromEbXML(C metaData, E ebXML) {
        metaData.setComments(ebXML.getDescription());
        metaData.setTitle(ebXML.getName());
        metaData.setEntryUuid(ebXML.getId());
        metaData.setLogicalUuid(ebXML.getLid());
        metaData.setVersion(ebXML.getVersionInfo());
    }

    /**
     * Called by the base class to add slots to the ebXML instance.
     * @param metaData
     *          the meta data instance containing the slots. 
     * @param ebXML
     *          the ebXML instance receiving the slots.
     * @param objectLibrary 
     *          the object library.
     */
    protected void addSlots(C metaData, E ebXML, EbXMLObjectLibrary objectLibrary) {}

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
     * @param objectLibrary 
     *          the object library.
     */
    protected void addClassifications(C metaData, E ebXML, EbXMLObjectLibrary objectLibrary) {}

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
     * @param objectLibrary 
     *          the object library.
     */
    protected void addExternalIdentifiers(C metaData, E ebXML, EbXMLObjectLibrary objectLibrary) {
        String patientID = Hl7v2Based.render(metaData.getPatientId());
        ebXML.addExternalIdentifier(patientID, patientIdExternalId, patientIdLocalizedString);        
        ebXML.addExternalIdentifier(metaData.getUniqueId(), uniqueIdExternalId, uniqueIdLocalizedString);
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
        metaData.setPatientId(Hl7v2Based.parse(patientID, Identifiable.class));
        metaData.setUniqueId(ebXML.getExternalIdentifierValue(uniqueIdExternalId));
    }
}
