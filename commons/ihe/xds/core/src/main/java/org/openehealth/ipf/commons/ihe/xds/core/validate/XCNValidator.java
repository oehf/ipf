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

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.*;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7;
import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7Delimiter;

/**
 * Validates a XCN string.
 * @author Jens Riemschneider
 */
public class XCNValidator implements ValueValidator {
    private final OIDValidator oidValidator = new OIDValidator();

    @Override
    public void validate(String hl7xcn) throws XDSMetaDataException {
        notNull(hl7xcn, "hl7xcn cannot be null");
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7xcn);

        String idNumber = HL7.get(parts, 1, true);
        String hl7fn = HL7.get(parts, 2, false);
        
        metaDataAssert(hl7fn != null || idNumber != null, PERSON_MISSING_NAME_AND_ID, hl7xcn);

        String hl7hd = HL7.get(parts, 9, false);
        metaDataAssert(idNumber == null || hl7hd != null, PERSON_HD_MISSING, hl7xcn);
        
        if (hl7hd != null) {
            List<String> hdParts = HL7.parse(HL7Delimiter.SUBCOMPONENT, hl7hd);
            metaDataAssert(HL7.get(hdParts, 1, false) == null,
                    HD_MUST_NOT_HAVE_NAMESPACE_ID, hl7xcn);
            metaDataAssert("ISO".equals(HL7.get(hdParts, 3, true)), 
                    UNIVERSAL_ID_TYPE_MUST_BE_ISO, hl7xcn);
            String oid = HL7.get(hdParts, 2, true);
            metaDataAssert(oid != null, HD_NEEDS_UNIVERSAL_ID, hl7xcn);
            oidValidator.validate(oid);
        }
    }
}
