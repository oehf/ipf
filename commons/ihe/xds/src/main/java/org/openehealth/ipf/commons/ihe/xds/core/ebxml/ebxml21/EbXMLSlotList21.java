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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21;

import static org.apache.commons.lang3.Validate.notNull;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlotList;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.SlotType1;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ValueListType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a list of slots.
 * @author Jens Riemschneider
 */
public class EbXMLSlotList21 implements EbXMLSlotList {
    private final List<SlotType1> slotListObj;
    
    /**
     * Constructs the slot list by wrapping the given ebXML 2.1 object.
     * @param slotListObj
     *          the object to wrap.
     */
    public EbXMLSlotList21(List<SlotType1> slotListObj) {
        notNull(slotListObj, "slotListObj cannot be null");
        this.slotListObj = slotListObj;
    }

    @Override
    public void addSlot(String slotName, String... slotValues) {
        if (slotValues == null || slotValues.length == 0) {
            return;
        }

        ValueListType valueList = EbXMLFactory21.RIM_FACTORY.createValueListType();
        List<String> values = valueList.getValue();
        for (String slotValue : slotValues) {
            if (slotValue != null) {
                values.add(slotValue);
            }
        }
        
        SlotType1 slot = EbXMLFactory21.RIM_FACTORY.createSlotType1();
        slot.setName(slotName);
        slot.setValueList(valueList);

        EbXMLSlot21 slot21 = new EbXMLSlot21(slot);
        if (!slot21.getValueList().isEmpty()) {
            slotListObj.add(slot);
        }
    }

    @Override
    public List<String> getSlotValues(String slotName) {
        notNull(slotName, "slotName cannot be null");
        for (SlotType1 slot21 : slotListObj) {
            if (slotName.equals(slot21.getName())) {
                return slot21.getValueList().getValue();
            }
        }
        
        return Collections.emptyList();
    }

    @Override
    public String getSingleSlotValue(String slotName) {
        List<String> slotValues = getSlotValues(slotName);
        return slotValues.size() > 0 ? slotValues.get(0) : null;
    }

    @Override
    public List<EbXMLSlot> getSlots() {
        List<EbXMLSlot> slots = new ArrayList<EbXMLSlot>(slotListObj.size());
        for (SlotType1 slot21 : slotListObj) {
            slots.add(new EbXMLSlot21(slot21));
        }
        return slots;
    }

    @Override
    public List<EbXMLSlot> getSlots(String slotName) {
        notNull(slotName, "slotName cannot be null");
        
        List<EbXMLSlot> slots = new ArrayList<EbXMLSlot>(slotListObj.size());
        for (SlotType1 slot21 : slotListObj) {
            if (slotName.equals(slot21.getName())) {
                slots.add(new EbXMLSlot21(slot21));
            }
        }
        return slots;
    }
}
