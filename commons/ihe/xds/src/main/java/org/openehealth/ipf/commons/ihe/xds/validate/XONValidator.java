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
package org.openehealth.ipf.commons.ihe.xds.validate;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.validate.ValidatorAssertions.*;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.hl7.HL7;
import org.openehealth.ipf.commons.ihe.xds.hl7.HL7Delimiter;

/**
 * Validates a XON string.
 * @author Jens Riemschneider
 */
public class XONValidator implements ValueValidator {
    private final OIDValidator oidValidator = new OIDValidator();

    @Override
    public void validate(String hl7XON) throws XDSMetaDataException {
        notNull(hl7XON, "hl7XON cannot be null");
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7XON);
        
        String organizationName = HL7.get(parts, 1, true);
        metaDataAssert(organizationName != null, ORGANIZATION_NAME_MISSING, hl7XON);
        
        String hl7HD = HL7.get(parts, 6, false);
        if (hl7HD != null) {
            List<String> hdParts = HL7.parse(HL7Delimiter.SUBCOMPONENT, hl7HD);
            metaDataAssert(HL7.get(hdParts, 1, false) == null,
                    HD_MUST_NOT_HAVE_NAMESPACE_ID, hl7XON);
            metaDataAssert("ISO".equals(HL7.get(hdParts, 3, true)), 
                    UNIVERSAL_ID_TYPE_MUST_BE_ISO, hl7XON);
            String oid = HL7.get(hdParts, 2, true);
            metaDataAssert(oid != null, HD_NEEDS_UNIVERSAL_ID, hl7XON);
            oidValidator.validate(oid);
        }
        else {
            String idNumber = HL7.get(parts, 10, true);
            if (idNumber == null) {
                idNumber = HL7.get(parts, 3, true);
            }
            if (idNumber != null) {
                oidValidator.validate(idNumber);
            }
        }
        
        metaDataAssert(parts.size() <= 10, ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(HL7.get(parts, 2, false) == null, ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(HL7.get(parts, 4, false) == null, ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(HL7.get(parts, 5, false) == null, ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(HL7.get(parts, 7, false) == null, ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(HL7.get(parts, 8, false) == null, ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(HL7.get(parts, 9, false) == null, ORGANIZATION_TOO_MANY_COMPONENTS);
    }
}
