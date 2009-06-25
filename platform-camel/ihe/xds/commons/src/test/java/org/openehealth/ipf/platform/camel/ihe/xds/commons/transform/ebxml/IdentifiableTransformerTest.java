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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.IdentifiableTransformer;

/**
 * Tests for {@link IdentifiableTransformer}.
 * @author Jens Riemschneider
 */
public class IdentifiableTransformerTest {
    private IdentifiableTransformer transformer;
    private Identifiable identifiable;
    
    @Before
    public void setUp() {
        transformer = new IdentifiableTransformer();

        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setNamespaceId("namespace");
        assigningAuthority.setUniversalId("uni");
        assigningAuthority.setUniversalIdType("uniType");
        
        identifiable = new Identifiable();
        identifiable.setId("21&3");
        identifiable.setAssigningAuthority(assigningAuthority);
    }
    
    @Test
    public void testToEbXML21SourcePatient() {
        String result = transformer.toEbXML(identifiable);
        assertNotNull(result);
        
        assertEquals("21\\T\\3^^^namespace&uni&uniType", result);
    }

    @Test
    public void testToEbXML21SourcePatientNull() {
        assertNull(transformer.toEbXML(null));
    }

    @Test
    public void testToEbXML21SourcePatientEmpty() {
        String result = transformer.toEbXML(new Identifiable());
        assertNotNull(result);
        assertEquals("", result);
    }



    @Test
    public void testFromEbXML21SourcePatient() {
        String ebXML = transformer.toEbXML(identifiable);
        Identifiable result = transformer.fromEbXML(ebXML);       
        assertNotNull(result);
        
        assertEquals("21&3", result.getId());
        
        AssigningAuthority assigningAuthority = result.getAssigningAuthority();
        assertEquals("namespace", assigningAuthority.getNamespaceId());
        assertEquals("uni", assigningAuthority.getUniversalId());
        assertEquals("uniType", assigningAuthority.getUniversalIdType());
    }
    
    @Test
    public void testFromEbXML21SourcePatientNull() {
        assertNull(transformer.fromEbXML(null));
    }
    
    @Test
    public void testFromEbXML21SourcePatientEmpty() {
        Identifiable result = transformer.fromEbXML("");
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getAssigningAuthority());
    }
}
