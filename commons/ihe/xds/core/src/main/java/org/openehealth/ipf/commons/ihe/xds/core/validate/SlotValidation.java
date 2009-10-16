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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;

import java.util.List;

/**
 * Validation for slots.
 */
public class SlotValidation implements RegistryObjectValidator {
    private final String slotName;
    private final ValueListValidator validator;

    /**
     * Constructs the validation.
     * @param slotName
     *          the name of the slot.
     * @param validator
     *          the validator to call for the slot. 
     */
    public SlotValidation(String slotName, ValueListValidator validator) {
        this.slotName = slotName;
        this.validator = validator;
    }

    @Override
    public void validate(EbXMLRegistryObject obj) throws XDSMetaDataException {
        List<String> slotValues = obj.getSlotValues(slotName);
        validator.validate(slotValues);
    }
}
