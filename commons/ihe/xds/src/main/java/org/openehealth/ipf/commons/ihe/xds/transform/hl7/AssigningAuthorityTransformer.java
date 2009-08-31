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
package org.openehealth.ipf.commons.ihe.xds.transform.hl7;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.hl7.HL7;
import org.openehealth.ipf.commons.ihe.xds.hl7.HL7Delimiter;
import org.openehealth.ipf.commons.ihe.xds.metadata.AssigningAuthority;

/**
 * Transforms between HL7 HD and {@link AssigningAuthority}.
 * @author Jens Riemschneider
 */
public class AssigningAuthorityTransformer {

    /**
     * Transforms from HL7 HD into {@link AssigningAuthority}.
     * @param hl7HD
     *          the HD data. Can be <code>null</code>.
     * @return the assigning authority. <code>null</code> if no relevant 
     *          data was found in the HL7 string. 
     */
    public AssigningAuthority fromHL7(String hl7HD) {
        List<String> parts = HL7.parse(HL7Delimiter.SUBCOMPONENT, hl7HD);
        
        if (parts.isEmpty()) {
            return null;
        }
        
        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setNamespaceId(HL7.get(parts, 1, true));
        assigningAuthority.setUniversalId(HL7.get(parts, 2, true));
        assigningAuthority.setUniversalIdType(HL7.get(parts, 3, true));
        
        return assigningAuthority;
    }

    /**
     * Transforms from {@link AssigningAuthority} into HL7 HD.
     * @param assigningAuthority
     *          the assigning authority. Can be <code>null</code>.
     * @return the HL7 HD string. <code>null</code> if the input was <code>null</code> 
     *          or the resulting HL7 string would be empty.
     */
    public String toHL7(AssigningAuthority assigningAuthority) {
        if (assigningAuthority == null) {
            return null;
        }
        
        return HL7.render(HL7Delimiter.SUBCOMPONENT, 
                HL7.escape(assigningAuthority.getNamespaceId()),
                HL7.escape(assigningAuthority.getUniversalId()),
                HL7.escape(assigningAuthority.getUniversalIdType()));
    }
}
