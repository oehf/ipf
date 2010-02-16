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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;

/**
 * Encapsulation of the ebXML classes for {@code ClassificationType}. 
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLClassification extends EbXMLSlotList {
    /**
     * @return the object being classified by this classification.
     */
    String getClassifiedObject();
    
    /**
     * @param classifiedObject
     *          the object being classified by this classification.
     */
    void setClassifiedObject(String classifiedObject);
    
    /**
     * @return the classification scheme.
     */
    String getClassificationScheme();

    /**
     * @param classificationScheme
     *          the classification scheme.
     */
    void setClassificationScheme(String classificationScheme);
    
    /**
     * @return the node representation.
     */
    String getNodeRepresentation();
    
    /**
     * @param nodeRepresentation
     *          the node representation.
     */
    void setNodeRepresentation(String nodeRepresentation);
    
    /**
     * @return the name as a localized string.
     */
    LocalizedString getName();
    
    /**
     * @return the name as an international string.
     */
    EbXMLInternationalString getNameAsInternationalString();
    
    /**
     * @param name
     *          the name as a localized string.
     */
    void setName(LocalizedString name);

    /**
     * @param classificationNode
     *          the classification node.
     */
    void setClassificationNode(String classificationNode);
    
    /**
     * @return the classification node.
     */
    String getClassificationNode();
    
    /**
     * Assigns a random unique ID to this classification.
     */
    void assignUniqueId();
}
