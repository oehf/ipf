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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.metadata.Organization;
import org.openehealth.ipf.commons.ihe.xds.transform.hl7.OrganizationTransformer;

/**
 * Tests for {@link OrganizationTransformer}.
 * @author Jens Riemschneider
 */
public class OrganizationTransformerTest {
    @Test
    public void testToHL7() {
        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setNamespaceId("he&llo");
        assigningAuthority.setUniversalId("1.2&.3.4");
        assigningAuthority.setUniversalIdType("WU&RZ");

        Organization organization = new Organization();
        organization.setOrganizationName("Untere&Klinik");
        organization.setIdNumber("a|number");
        organization.setAssigningAuthority(assigningAuthority);
        
        assertEquals("Untere\\T\\Klinik^^^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ^^^^a\\F\\number", 
                new OrganizationTransformer().toHL7(organization));
    }

    @Test
    public void testToHL7Empty() {
        assertNull(new OrganizationTransformer().toHL7(new Organization()));
    }

    @Test
    public void testToHL7WithNullParam() {
        assertNull(new OrganizationTransformer().toHL7(null));
    }
    

    @Test
    public void testFromHL7() {
        Organization organization = new OrganizationTransformer().fromHL7(
                "Untere\\T\\Klinik^^^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ^^^^a\\F\\number");
        
        assertEquals("Untere&Klinik", organization.getOrganizationName());
        assertEquals("a|number", organization.getIdNumber());
        assertEquals("he&llo", organization.getAssigningAuthority().getNamespaceId());
        assertEquals("1.2&.3.4", organization.getAssigningAuthority().getUniversalId());
        assertEquals("WU&RZ", organization.getAssigningAuthority().getUniversalIdType());
    }

    @Test
    public void testFromHL7WithNullParam() {
        assertNull(new OrganizationTransformer().fromHL7(null));
    }    

    @Test
    public void testFromHL7WithEmptyParam() {
        assertNull(new OrganizationTransformer().fromHL7(""));
    }    
}
