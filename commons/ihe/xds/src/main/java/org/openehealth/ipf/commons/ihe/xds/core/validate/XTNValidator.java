/*
 * Copyright 2013 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Telecom;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates an XTN value.
 * @author Dmytro Rud
 */
public class XTNValidator implements ValueValidator {

    @Override
    public void validate(String hl7XTN) throws XDSMetaDataException {
        var telecom = Telecom.parse(hl7XTN);
        metaDataAssert(telecom != null, MISSING_TELECOM_PARAM, hl7XTN);

        if ("Internet".equals(telecom.getType())) {
            metaDataAssert(isBlank(telecom.getUse()) || "NET".equals(telecom.getUse()), WRONG_TELECOM_USE, hl7XTN);
            metaDataAssert(isNotBlank(telecom.getEmail()), MISSING_TELECOM_PARAM, hl7XTN);
            metaDataAssert(telecom.getCountryCode() == null, INCONSISTENT_TELECOM_PARAM, hl7XTN);
            metaDataAssert(telecom.getAreaCityCode() == null, INCONSISTENT_TELECOM_PARAM, hl7XTN);
            metaDataAssert(telecom.getLocalNumber() == null, INCONSISTENT_TELECOM_PARAM, hl7XTN);
            metaDataAssert(telecom.getExtension() == null, INCONSISTENT_TELECOM_PARAM, hl7XTN);
            metaDataAssert(isBlank(telecom.getUnformattedPhoneNumber()), INCONSISTENT_TELECOM_PARAM, hl7XTN);

        } else if ("PH".equals(telecom.getType()) || "CP".equals(telecom.getType())) {
            metaDataAssert(!"NET".equals(telecom.getUse()), WRONG_TELECOM_USE, hl7XTN);
            metaDataAssert(isBlank(telecom.getEmail()), INCONSISTENT_TELECOM_PARAM, hl7XTN);

            var localPresent = (telecom.getLocalNumber() != null);
            var unformattedPresent = isNotBlank(telecom.getUnformattedPhoneNumber());
            metaDataAssert(localPresent || unformattedPresent, MISSING_TELECOM_PARAM, hl7XTN);
            metaDataAssert(!(localPresent && unformattedPresent), INCONSISTENT_TELECOM_PARAM, hl7XTN);

        } else {
            throw new XDSMetaDataException(WRONG_TELECOM_TYPE, hl7XTN);
        }
    }
}
