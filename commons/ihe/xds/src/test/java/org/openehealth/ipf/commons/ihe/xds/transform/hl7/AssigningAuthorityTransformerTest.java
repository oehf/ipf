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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.transform.hl7.AssigningAuthorityTransformer;

/**
 * Tests for {@link AssigningAuthorityTransformer}.
 * @author Jens Riemschneider
 */
public class AssigningAuthorityTransformerTest {
    private AssigningAuthorityTransformer transformer;
    
    @Before
    public void setUp() {
        transformer = new AssigningAuthorityTransformer();        
    }

    @Test
    public void testToHL7() {
        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setNamespaceId("nam&ID");
        assigningAuthority.setUniversalId("ui^ID");
        assigningAuthority.setUniversalIdType("type|ID");
        assertEquals("nam\\T\\ID&ui\\S\\ID&type\\F\\ID", transformer.toHL7(assigningAuthority));
    }
    
    @Test
    public void testToHL7OptionalParams() {
        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setNamespaceId("nam&ID");
        assigningAuthority.setUniversalIdType("type|ID");
        assertEquals("nam\\T\\ID&&type\\F\\ID", transformer.toHL7(assigningAuthority));
    }
    
    @Test
    public void testToHL7NoParams() {
        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assertNull(transformer.toHL7(assigningAuthority));
    }

    @Test
    public void testToHL7Null() {
        assertNull(transformer.toHL7(null));
    }
    

    @Test
    public void testFromHL7() {
        AssigningAuthority assigningAuthority = transformer.fromHL7("nam\\T\\ID&ui\\S\\ID&type\\F\\ID");
        assertEquals("nam&ID", assigningAuthority.getNamespaceId());
        assertEquals("ui^ID", assigningAuthority.getUniversalId());
        assertEquals("type|ID", assigningAuthority.getUniversalIdType());
    }
    
    @Test
    public void testFromHL7NoParams() {
        assertNull(transformer.fromHL7(""));
    }

    @Test
    public void testFromHL7OptionalParams() {
        AssigningAuthority assigningAuthority = transformer.fromHL7("nam\\T\\ID&&type\\F\\ID");
        assertEquals("nam&ID", assigningAuthority.getNamespaceId());
        assertNull(assigningAuthority.getUniversalId());
        assertEquals("type|ID", assigningAuthority.getUniversalIdType());
    }
    
    @Test
    public void testFromHL7Null() {
        assertNull(transformer.fromHL7(null));
    }

    @Test
    public void testFromHL7Nothing() {
        assertNull(transformer.fromHL7(""));
    }
}
