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
 * Provides access to slots and their values.
 * @author Jens Riemschneider
 */
public interface EbXMLSlotList {
    /**
     * @return the complete list of all slots.
     */
    List<EbXMLSlot> getSlots();
    
    /**
     * Returns a filtered list of the slots.
     * @param slotName
     *          name of the slots.
     * @return the list of slots named with the given slot name.
     */
    List<EbXMLSlot> getSlots(String slotName);
    
    /**
     * Adds a slot with a list of values.
     * @param slotName
     *          the slot name.
     * @param slotValues
     *          the slot values. The slot will not be created if this parameter is
     *          empty or <code>null</code>.
     */
    void addSlot(String slotName, String... slotValues);
    
    /**
     * Gets the values of a slot.
     * @param slotName
     *          the name of the slot. It is expected that the name is only used for
     *          a single slot. Use {@link #getSlots(String)} if it is possible that
     *          the name is used multiple times.
     * @return the list of slot values.
     */
    List<String> getSlotValues(String slotName);

    /**
     * Gets a single slot value. 
     * @param slotName
     *          the name of the slot. It is expected that the name is only used for
     *          a single slot. Use {@link #getSlots(String)} if it is possible that
     *          the name is used multiple times.
     * @return the first slot value. Other slot values are ignored. Can be 
     *          <code>null</code> if the slot does not exist, has no slot values 
     *          or the value is <code>null</code>.
     */
    String getSingleSlotValue(String slotName);
}
