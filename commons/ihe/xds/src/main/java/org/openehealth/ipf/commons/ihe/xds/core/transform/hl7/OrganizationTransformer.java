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
package org.openehealth.ipf.commons.ihe.xds.core.transform.hl7;

import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7;
import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7Delimiter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Organization;

import java.util.List;

/**
 * Transformation logic for a {@link Organization}.
 * <p>
 * This class offers transformation between {@link Organization} and an HL7v2.5
 * XON string.
 * @author Jens Riemschneider
 */
public class OrganizationTransformer {
    private final AssigningAuthorityTransformer assigningAuthorityTransformer = 
        new AssigningAuthorityTransformer();
    
    /**
     * Creates an organization instance via an HL7 XON string.
     * @param hl7XON
     *          the HL7 XON string. Can be <code>null</code>.
     * @return the created Organization instance. <code>null</code> if no relevant 
     *          data was found in the HL7 string.
     */
    public Organization fromHL7(String hl7XON) {
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7XON);

        String organizationName = HL7.get(parts, 1, true);
        String idNumber = HL7.get(parts, 10, true);
        if (idNumber == null) {
            idNumber = HL7.get(parts, 3, true);
        }
        AssigningAuthority assigningAuthority = 
            assigningAuthorityTransformer.fromHL7(HL7.get(parts, 6, false));
        
        if (organizationName == null && idNumber == null && assigningAuthority == null) {
            return null;
        }
        
        Organization organization = new Organization();
        organization.setOrganizationName(organizationName);
        organization.setIdNumber(idNumber);
        organization.setAssigningAuthority(assigningAuthority);
        
        return organization;
    }
    
    /**
     * Transforms an organization instance into an HL7v2.5 XON string.
     * @param organization
     *          the organization to transform. Can be <code>null</code>.
     * @return the HL7 representation. <code>null</code> if the input was <code>null</code> 
     *          or the resulting HL7 string would be empty.
     */
    public String toHL7(Organization organization) {
        if (organization == null) {
            return null;
        }
        
        String assigningAuthority = assigningAuthorityTransformer.toHL7(organization.getAssigningAuthority());
        
        return HL7.render(HL7Delimiter.COMPONENT,
                HL7.escape(organization.getOrganizationName()), 
                null, 
                null,
                null,
                null,
                assigningAuthority,
                null,
                null,
                null,
                HL7.escape(organization.getIdNumber()));
    }
}
