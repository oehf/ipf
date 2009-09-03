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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference;

/**
 * Encapsulation of the ebXML classes for {@code AdhocQueryResponse}. 
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLQueryResponse extends EbXMLObjectContainer, EbXMLRegistryResponse {
    /**
     * Adds an object reference to the response.
     * @param ref
     *          the object reference.
     */
    void addReference(ObjectReference ref);
    
    /**
     * @return all object references contained in the response.
     */
    List<ObjectReference> getReferences();
}
