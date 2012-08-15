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

import static org.junit.Assert.*;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;

/**
 * Tests for transformation between HL7 v2 and {@link AssigningAuthority}.
 * @author Jens Riemschneider
 */
public class AssigningAuthorityTransformerTest {

    @Test
    public void testToHL7() {
        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setUniversalId("nam&ID_ui^ID");
        assigningAuthority.setUniversalIdType("type|ID");
        assertEquals("&nam\\T\\ID_ui\\S\\ID&type\\F\\ID", Hl7v2Based.render(assigningAuthority));
    }
    
    @Test
    public void testToHL7OptionalParams() {
        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setNamespaceId("nam&ID");
        assigningAuthority.setUniversalIdType("type|ID");
        assertEquals("&&type\\F\\ID", Hl7v2Based.render(assigningAuthority));
    }
    
    @Test
    public void testToHL7NoParams() {
        assertNull(Hl7v2Based.render(new AssigningAuthority()));
    }

    @Test
    public void testToHL7Null() {
        assertNull(Hl7v2Based.render(null));
    }
    

    @Test
    public void testFromHL7() {
        AssigningAuthority assigningAuthority =
                Hl7v2Based.parse("nam\\T\\ID&ui\\S\\ID&type\\F\\ID", AssigningAuthority.class);
        assertEquals("nam&ID", assigningAuthority.getNamespaceId());
        assertEquals("ui^ID", assigningAuthority.getUniversalId());
        assertEquals("type|ID", assigningAuthority.getUniversalIdType());
    }
    
    @Test
    public void testFromHL7NoParams() {
        assertNull(Hl7v2Based.parse("", AssigningAuthority.class));
    }

    @Test
    public void testFromHL7OptionalParams() {
        AssigningAuthority assigningAuthority = Hl7v2Based.parse("nam\\T\\ID&&type\\F\\ID", AssigningAuthority.class);
        assertEquals("nam&ID", assigningAuthority.getNamespaceId());
        assertNull(assigningAuthority.getUniversalId());
        assertEquals("type|ID", assigningAuthority.getUniversalIdType());
    }
    
    @Test
    public void testFromHL7Null() {
        assertNull(Hl7v2Based.parse(null, AssigningAuthority.class));
    }

    @Test
    public void testFromHL7Nothing() {
        assertNull(Hl7v2Based.parse("", AssigningAuthority.class));
    }
}
