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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.SlotType1;

/**
 * Encapsulation of {@link SlotType1}.
 * @author Jens Riemschneider
 */
public class EbXMLSlot30 implements EbXMLSlot {
    private final SlotType1 slot;
    
    /**
     * Constructs a slot by wrapping the given ebXML 3.0 object.
     * @param slot
     *          the object to wrap.
     */
    public EbXMLSlot30(SlotType1 slot) {
        notNull(slot, "slot cannot be null");
        this.slot = slot;
    }
    
    @Override
    public String getName() {
        return slot.getName();
    }

    @Override
    public List<String> getValueList() {
        return slot.getValueList().getValue();
    }

    @Override
    public int getValueLengthLimit() {
        return 256;
    }
}
