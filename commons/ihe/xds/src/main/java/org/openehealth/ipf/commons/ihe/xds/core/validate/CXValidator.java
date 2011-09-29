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
import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7;
import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7Delimiter;

/**
 * Validates a CX value.
 * @author Jens Riemschneider
 */
public class CXValidator implements ValueValidator {
    private final OIDValidator oidValidator = new OIDValidator();
    
    @Override
    public void validate(String hl7CX) throws XDSMetaDataException {
        notNull(hl7CX, "value cannot be null");
        
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7CX);
        
        metaDataAssert(parts.size() <= 4 && HL7.get(parts, 2, false) == null && HL7.get(parts, 3, false) == null,  
                CX_TOO_MANY_COMPONENTS);

        String idNumber = HL7.get(parts, 1, true);
        metaDataAssert(idNumber != null, CX_NEEDS_ID);
        
        String hl7HD = HL7.get(parts, 4, false);
        List<String> hd = HL7.parse(HL7Delimiter.SUBCOMPONENT, hl7HD);
        
        metaDataAssert(HL7.get(hd, 1, false) == null, HD_MUST_NOT_HAVE_NAMESPACE_ID, hl7CX);
        
        String oidType = HL7.get(hd, 3, true);
        metaDataAssert("ISO".equals(oidType), UNIVERSAL_ID_TYPE_MUST_BE_ISO, hl7CX);  

        String oid = HL7.get(hd, 2, true);
        metaDataAssert(oid != null, HD_NEEDS_UNIVERSAL_ID, hl7CX);

        oidValidator.validate(oid);
    }
}
