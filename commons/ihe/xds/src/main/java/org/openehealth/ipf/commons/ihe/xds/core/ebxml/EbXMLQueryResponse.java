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
public interface EbXMLQueryResponse<E> extends EbXMLObjectContainer, EbXMLRegistryResponse<E> {
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

    /**
     * Adds a slot to the ebRS response slot list, which is where a registry reports about the response
     * itself rather than about the objects in it -- for instance which sort order it actually applied.
     *
     * @param slotName   name of the slot.
     * @param slotValues values of the slot.
     */
    void addResponseSlot(String slotName, String... slotValues);

    /**
     * @param slotName name of the slot.
     * @return values of the response slot with that name, empty if there is none.
     */
    List<String> getResponseSlotValues(String slotName);

    /**
     * @param requestId identifier of the request this responds to, as the requester set it.
     */
    void setRequestId(String requestId);

    /**
     * @return identifier of the request this responds to, or <code>null</code> if the registry did not
     *      echo one.
     */
    String getRequestId();

    /**
     * @param startIndex index of the first result in this response, or <code>null</code> if it is not
     *                   a window into a larger result set.
     */
    void setStartIndex(Integer startIndex);

    /**
     * @return index of the first result in this response, or <code>null</code>.
     */
    Integer getStartIndex();

    /**
     * @param totalResultCount size of the whole result set this response is a window into, or
     *                         <code>null</code> if the registry does not report it.
     */
    void setTotalResultCount(Integer totalResultCount);

    /**
     * @return size of the whole result set, or <code>null</code> if the registry does not report it.
     *      This is what makes a total available without fetching every result.
     */
    Integer getTotalResultCount();
}
