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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;

/**
 * Encapsulation of the ebXML classes for {@code AssociationType1}. 
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLAssociation extends EbXMLRegistryObject {
    /**
     * @return the id of the target object of this association.
     */
    String getTarget();
    
    /**
     * @param target            
     *          the id of the target object of this association.
     */
    void setTarget(String target);

    /**
     * @return the id of the source object of this association.
     */
    String getSource();
    
    /**
     * @param source
     *          the id of the source object of this association.
     */
    void setSource(String source);

    /**
     * @return the type of this association.
     */
    AssociationType getAssociationType();
    
    /**
     * @param associationType
     *          the type of this association.
     */
    void setAssociationType(AssociationType associationType);
}
