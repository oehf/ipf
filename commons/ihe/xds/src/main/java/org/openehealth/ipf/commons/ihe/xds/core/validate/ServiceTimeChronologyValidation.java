/*
 * Copyright 2018 the original author or authors.
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

import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SERVICE_START_TIME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SERVICE_STOP_TIME;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.TIME_WRONG_CHRONOLOGY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;

/**
 *
 * Validate that serviceStart and serviceStop date/time DTM values are in
 * chronological order (according to IHE ITI TF 2b 3.42.4.1.3.3.6). The
 * validation is just performed on a basic string compare and therefore expect
 * that the validation of the time format itself was performed before.
 *
 * @author Thomas Papke
 *
 */
public class ServiceTimeChronologyValidation implements RegistryObjectValidator {

    @Override
    public void validate(EbXMLRegistryObject obj) throws XDSMetaDataException {
        var serviceStartTime = obj.getSingleSlotValue(SLOT_NAME_SERVICE_START_TIME);
        var serviceStopTime = obj.getSingleSlotValue(SLOT_NAME_SERVICE_STOP_TIME);

        if (serviceStartTime != null && serviceStopTime != null && !serviceStartTime.equals(serviceStopTime)) {
            metaDataAssert(fillToMaxLength(serviceStartTime, '0').compareTo(fillToMaxLength(serviceStopTime, '9')) <= 0,
                    TIME_WRONG_CHRONOLOGY, serviceStartTime, serviceStopTime);
        }
    }

    private String fillToMaxLength(String timeValue, char fillCharacter) {
        return rightPad(timeValue, 14, fillCharacter);
    }
}
