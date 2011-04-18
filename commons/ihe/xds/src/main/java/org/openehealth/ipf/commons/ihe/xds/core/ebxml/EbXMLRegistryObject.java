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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Version;

import java.util.List;

/**
 * Encapsulation of the ebXML classes for {@code RegistryEntryType} and 
 * {@code RegistryObjectType}.
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLRegistryObject extends EbXMLSlotList {
    /**
     * @return all classifications contained in this entry.
     */
    List<EbXMLClassification> getClassifications();
    
    /**
     * Returns the classification matching the given scheme.
     * @param scheme
     *          the scheme.
     * @return the classifications contained in this entry that match the scheme.
     */
    List<EbXMLClassification> getClassifications(String scheme);
    
    /**
     * Returns the first classification matching the given scheme.
     * @param scheme
     *          the scheme.
     * @return the classification matching the scheme. Only the first classification
     *          is considered, other classifications are ignored.
     */
    EbXMLClassification getSingleClassification(String scheme);
    
    /**
     * Adds a classification to this entry with a random id.
     * @param classification
     *          the classification to add.
     * @param scheme
     *          the scheme of the classification.
     */
    void addClassification(EbXMLClassification classification, String scheme);    

    /**
     * @return all external identifiers contained in this entry.
     */
    List<EbXMLExternalIdentifier> getExternalIdentifiers();
    
    /**
     * Returns the value of the external identifier matching the given scheme.
     * @param scheme
     *          the scheme of the external identifier.
     * @return the value of the external identifier that matched the scheme. <code>null</code>
     *          if no identifier was found that matched the scheme.
     */
    String getExternalIdentifierValue(String scheme);
    
    /**
     * Adds a new external identifier to this entry.
     * @param value
     *          the value of the identifier.
     * @param scheme
     *          the scheme of the identifier.
     * @param name
     *          the name of the identifier.
     */
    void addExternalIdentifier(String value, String scheme, String name);

    /**
     * @return the object type of this entry.
     */
    String getObjectType();
    
    /**
     * @param objectType
     *          the object type of this entry.
     */
    void setObjectType(String objectType);
    
    /**
     * @return the description of this entry.
     */
    LocalizedString getDescription();
    
    /**
     * @param description
     *          the description of this entry.
     */
    void setDescription(LocalizedString description);
    
    /**
     * @return the name of this entry.
     */
    LocalizedString getName();
    
    /**
     * @param name 
     *          the name of this entry.
     */
    void setName(LocalizedString name);

    /**
     * @return the home attribute of this entry.
     */
    String getHome();
    
    /**
     * @param home
     *          the home attribute of this entry.
     */
    void setHome(String home);

    /**
     * @return the id of this entry.
     */
    String getId();
    
    /**
     * @param id  the id of this entry.
     */
    void setId(String id);

    /**
     * @return the lid of this entry.
     */
    String getLid();

    /**
     * @param lid  the lid of this entry.
     */
    void setLid(String lid);

    /**
     * @return the id of this entry.
     */
    Version getVersionInfo();

    /**
     * @param version  the versionInfo of this entry.
     */
    void setVersionInfo(Version version);

    /**
     * @return the ebXML object being wrapped by this entry.
     */
    Object getInternal();
}
