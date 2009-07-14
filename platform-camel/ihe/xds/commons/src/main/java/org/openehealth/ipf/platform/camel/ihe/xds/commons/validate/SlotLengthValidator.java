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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.validate;

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidatorAssertions.*;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidationMessage.*;

import java.util.List;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLObjectContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRegistryObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLSlot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLSlotList;

/**
 * Validates lengths of ebXML slot values.
 * @author Jens Riemschneider
 */
public class SlotLengthValidator {
    /**
     * Validates slot lengths for the given ebXML object container.
     * @param container
     *          the container of ebXML objects.
     * @throws XDSMetaDataException
     *          if a slot length validation failed.
     */
    public void validate(EbXMLObjectContainer container) throws XDSMetaDataException {
        validateSlotLengthsOfRegistryObjects(container.getAssociations());
        validateSlotLengthsOfRegistryObjects(container.getExtrinsicObjects());
        validateSlotLengthsOfRegistryObjects(container.getRegistryPackages());
        validateSlotLengths(container.getClassifications());
    }

    private void validateSlotLengthsOfRegistryObjects(List<? extends EbXMLRegistryObject> regObjects) throws XDSMetaDataException {
        validateSlotLengths(regObjects);
        for (EbXMLRegistryObject regObj : regObjects) {
            validateSlotLengths(regObj.getClassifications());
        }
    }

    private void validateSlotLengths(List<? extends EbXMLSlotList> slotListContainers) throws XDSMetaDataException {
        for (EbXMLSlotList slotList : slotListContainers) {
            for (EbXMLSlot slot : slotList.getSlots()) {
                for (String value : slot.getValueList()) {
                    if (value != null) {
                        metaDataAssert(value.length() <= slot.getValueLengthLimit(), SLOT_VALUE_TOO_LONG, slot.getName());
                    }
                }
            }
        }
    }
}
