/*
 * Copyright 2011 the original author or authors.
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

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_CODING_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ClassificationValidation.assertDisplayNamePresent;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlotList;
/**
 * Validates event code display name to be required, if eventCode has a value.
 *  
 * Checkout http://www.ihe.net/Technical_Framework/upload/IHE_ITI_TF_Rev8-
 * 0_Vol3_FT_2011-08-19.pdf page 22
 * 
 * @author Mitko Kolev
 * 
 */
public class EventCodeListDisplayNameValidator extends SlotValueValidation {

    public EventCodeListDisplayNameValidator(){
        super (SLOT_NAME_CODING_SCHEME, new NopValidator(), 0, Integer.MAX_VALUE);
    }
    
    @Override
    public void validate(EbXMLSlotList slotList){
        validateClassification(slotList);
    }
    
    private void validateClassification(EbXMLSlotList classification){
        List<String> slotValues = classification.getSlotValues(SLOT_NAME_CODING_SCHEME);
        // if slotValues are not empty
        boolean haveNonEmptySlotValue = false;
        if (slotValues.size() >= 0) {
            for (String value : slotValues) {
                if (value != null && !value.isEmpty()) {
                    haveNonEmptySlotValue = true;
                    break;
                }
            }
        }
        if (haveNonEmptySlotValue == true) {
            assertDisplayNamePresent((EbXMLClassification) classification, SLOT_NAME_CODING_SCHEME);
        }
    }

}
