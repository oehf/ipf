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

/**
 * Encapsulation of the ebXML classes for {@code ExternalIdentifierType}. 
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLExternalIdentifier {
    /**
     * @return the value of the identifier.
     */
    String getValue();
    
    /**
     * @param value
     *          the value of the identifier.
     */
    void setValue(String value);
    
    /**
     * @return the scheme of the identifier.
     */
    String getIdentificationScheme();
    
    /**
     * @param identificationScheme
     *          the scheme of the identifier.
     */
    void setIdentificationScheme(String identificationScheme);
    
    /**
     * @return the name of the identifier.
     */
    EbXMLInternationalString getName();
    
    /**
     * @param name
     *          the name of the identifier.
     */
    void setName(EbXMLInternationalString name);

    /**
     * @param registryObject
     *          the registry object that this identifier belongs to.
     */
    void setRegistryObject(String registryObject);
    
    /**
     * @return the registry object that this identifier belongs to.
     */
    String getRegistryObject();

    /**
     * @param id
     *          the id of the identifier.
     */
    void setId(String id);
    
    /**
     * @return the id of the identifier.
     */
    String getId();
}
