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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import static org.apache.commons.lang.Validate.notNull;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.SlotType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ValueListType;

/**
 * Encapsulation of {@link SlotType1}.
 * @author Jens Riemschneider
 */
public class Slot30 implements Slot {
    private final static ObjectFactory rimFactory = new ObjectFactory();
    
    private final SlotType1 slot;
    
    private Slot30(SlotType1 slot) {
        notNull(slot, "slot cannot be null");
        this.slot = slot;
    }
    
    static Slot30 create(String slotName, String... slotValues) {
        notNull(slotName, "slotName cannot be null");
        
        ValueListType valueList = rimFactory.createValueListType();
        List<String> values = valueList.getValue();
        for (String slotValue : slotValues) {
            if (slotValue != null) {
                values.add(slotValue);
            }
        }
        
        SlotType1 slotEbrs30 = rimFactory.createSlotType1();
        slotEbrs30.setName(slotName);
        slotEbrs30.setValueList(valueList);
        return new Slot30(slotEbrs30);
    }
    
    public static Slot create(SlotType1 slot30) {
        return new Slot30(slot30);
    }
    
    @Override
    public String getName() {
        return slot.getName();
    }

    @Override
    public List<String> getValueList() {
        return slot.getValueList().getValue();
    }

    SlotType1 getInternal() {
        return slot;
    }
}
