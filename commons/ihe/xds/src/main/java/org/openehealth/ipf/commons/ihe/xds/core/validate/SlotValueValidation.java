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
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlotList;

/**
 * Performs a validation of a slot value.
 * @author Jens Riemschneider
 */
public class SlotValueValidation implements RegistryObjectValidator {
    private final String slotName;
    private final ValueValidator validator;
    private final int min;
    private final int max;

    /**
     * Constructs the validation.
     * @param slotName
     *          the name of the slot.
     * @param validator
     *          the validator to use.
     * @param min
     *          the minimum number of values that the slot must have.
     * @param max
     *          the maximum number of values that the slot must have.
     */
    public SlotValueValidation(String slotName, ValueValidator validator, int min, int max) {
        this.slotName = slotName;
        this.validator = validator;
        this.min = min;
        this.max = max;
    }

    /**
     * Constructs the validation.
     * @param slotName
     *          the name of the slot.
     * @param validator
     *          the validator to use.
     */
    public SlotValueValidation(String slotName, ValueValidator validator) {
        this.slotName = slotName;
        this.validator = validator;
        min = 1;
        max = 1;
    }

    @Override
    public void validate(EbXMLRegistryObject obj) throws XDSMetaDataException {
        validate((EbXMLSlotList)obj);
    }

    /**
     * Validates the given slot list.
     * @param slotList
     *          the slot list.
     * @throws XDSMetaDataException
     *          if the validation failed.
     */
    public void validate(EbXMLSlotList slotList) throws XDSMetaDataException {
        List<String> slotValues = slotList.getSlotValues(slotName);
        metaDataAssert(slotValues.size() >= min && slotValues.size() <= max,
                WRONG_NUMBER_OF_SLOT_VALUES, slotName, min, max, slotValues.size());

        for (String value : slotValues) {
            metaDataAssert(value != null, EMPTY_SLOT_VALUE, slotName);            
            validator.validate(value);
        }
    }
}