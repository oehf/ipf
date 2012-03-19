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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml;

import java.util.List;

/**
 * Provides functionality for containers of various ebXML objects.
 * @author Jens Riemschneider
 */
public interface EbXMLObjectContainer {
    /**
     * Adds the given extrinsic object to this container.
     * @param extrinsic
     *          the object to add. If <code>null</code> nothing will be added.
     */
    void addExtrinsicObject(EbXMLExtrinsicObject extrinsic);
    
    /**
     * Returns the list of extrinsic objects of a given type.
     * @param objectTypes
     *          the object types of the extrinsic objects to return.
     * @return the extrinsic objects.
     */
    List<EbXMLExtrinsicObject> getExtrinsicObjects(String... objectTypes);
    
    /**
     * Returns the list of all extrinsic objects.
     * @return the extrinsic objects of this container.
     */
    List<EbXMLExtrinsicObject> getExtrinsicObjects();

    /**
     * Adds a registry package to this container. 
     * @param regPackage
     *          the registry package to add.
     */
    void addRegistryPackage(EbXMLRegistryPackage regPackage);
    
    /**
     * Returns the list of registry packages matching the classification node.
     * @param classificationNode
     *          the classification node.
     * @return the list of packages that match the node.
     */
    List<EbXMLRegistryPackage> getRegistryPackages(String classificationNode);
    
    /**
     * Returns all registry packages of this container.
     * @return the list of packages.
     */
    List<EbXMLRegistryPackage> getRegistryPackages();

    /**
     * Adds an association to this container.
     * @param association
     *          the association to add.
     */
    void addAssociation(EbXMLAssociation association);
    
    /**
     * Returns all associations of this container.
     * @return the associations.
     */
    List<EbXMLAssociation> getAssociations();

    /**
     * Adds a classification to this container.
     * @param classification
     *          the classification to add.
     */
    void addClassification(EbXMLClassification classification);
    
    /**
     * @return all classifications contained in this entry.
     */
    List<EbXMLClassification> getClassifications();

    /**
     * @return the object library used by this container.
     */
    EbXMLObjectLibrary getObjectLibrary();
}
