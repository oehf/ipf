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

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.apache.commons.lang3.StringUtils.stripEnd;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.WRONG_TELECOM;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates an XTN value.
 * @author Dmytro Rud
 */
public class XTNValidator implements ValueValidator {

    @Override
    public void validate(String hl7XTN) throws XDSMetaDataException {
        metaDataAssert(hl7XTN.length() >= 17, WRONG_TELECOM, hl7XTN);   // 17 is the length of "^^Internet^a@b.de"
        metaDataAssert(hl7XTN.startsWith("^^Internet^"), WRONG_TELECOM, hl7XTN);
        metaDataAssert(countMatches(stripEnd(hl7XTN, "^"), "^") == 3, WRONG_TELECOM, hl7XTN);
    }
}
