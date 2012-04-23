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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectContainer;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlotList;

/**
 * Validates lengths of ebXML slot values and uniqueness of slot names.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class SlotLengthAndNameUniquenessValidator {

    /**
     * Validates slot lengths and name uniqueness
     * for the given ebXML object container.
     *
     * @param container
     *          the container of ebXML objects.
     * @throws XDSMetaDataException
     *          if a slot length validation failed.
     */
    public void validateContainer(EbXMLObjectContainer container) throws XDSMetaDataException {
        validateRegistryObjects(container.getAssociations());
        validateRegistryObjects(container.getExtrinsicObjects());
        validateRegistryObjects(container.getRegistryPackages());
        validateSlotLists(container.getClassifications());
    }

    private void validateRegistryObjects(List<? extends EbXMLRegistryObject> regObjects) throws XDSMetaDataException {
        validateSlotLists(regObjects);
        for (EbXMLRegistryObject regObj : regObjects) {
            validateSlotLists(regObj.getClassifications());
        }
    }

    private void validateSlotLists(List<? extends EbXMLSlotList> slotListContainers) throws XDSMetaDataException {
        for (EbXMLSlotList slotList : slotListContainers) {
            validateSlots(slotList.getSlots(), Collections.<String>emptySet());
        }
    }

    /**
     * Validates uniqueness of slot names and maximal lengths of slot values in the given collection.
     * @param slots
     *      ebXML slot collection.
     * @param allowedSlotNamesMultiple
     *      names of slots which are allowed to be present more than once.
     * @throws XDSMetaDataException
     *      when the validation fails.
     */
    public void validateSlots(
            List<? extends EbXMLSlot> slots,
            Set<String> allowedSlotNamesMultiple) throws XDSMetaDataException
    {
        HashSet<String> names = new HashSet<String>();
        for (EbXMLSlot slot : slots) {
            // validate uniqueness of slot names
            String name = slot.getName();
            if (! allowedSlotNamesMultiple.contains(name)) {
                metaDataAssert(StringUtils.isNotEmpty(name), MISSING_SLOT_NAME);
                metaDataAssert(! names.contains(name), DUPLICATE_SLOT_NAME, name);
                names.add(name);
            }

            // validate lengths of slot values
            for (String value : slot.getValueList()) {
                if (value != null) {
                    metaDataAssert(value.length() <= slot.getValueLengthLimit(), SLOT_VALUE_TOO_LONG, name);
                }
            }
        }
    }

}
