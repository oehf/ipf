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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

/**
 * Tests for transformations between HL7 v2 and {@link Person}.
 * @author Jens Riemschneider
 */
public class PersonTransformerTest {
    private Person person;

    @Before
    public void setUp() {
        AssigningAuthority assigningAuthority = new AssigningAuthority("he&llo", "1.2&.3.4", "WU&RZ");
        Identifiable id = new Identifiable("u^fz", assigningAuthority);
        Name name = new XcnName("Seu&fzer", "Em&il", "Ant|on", "der&7.", "D&r.", null);
        person = new Person(id, name);
    }
    
    @Test
    public void testToHL7() {
        assertEquals("u\\S\\fz^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ", 
                Hl7v2Based.render(person));
    }

    @Test
    public void testToHL7NoName() {
        person.setName(null);
        assertEquals("u\\S\\fz^^^^^^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ", 
                Hl7v2Based.render(person));
    }

    @Test
    public void testToHL7NoID() {
        person.setId(null);
        assertEquals("^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.", Hl7v2Based.render(person));
    }

    @Test
    public void testToHL7Empty() {
        assertNull(Hl7v2Based.render(new Person()));
    }
    
    @Test
    public void testToHL7WithNullParam() {
        assertNull(Hl7v2Based.render(null));
    }
    

    @Test
    public void testFromHL7() {
        Person result = Hl7v2Based.parse(
                "u\\S\\fz^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ",
                Person.class);

        assertEquals(person, result);
    }

    @Test
    public void testFromHL7NoIDNumber() {
        Person result = Hl7v2Based.parse("^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.", Person.class);

        person.setId(null);
        assertEquals(person, result);
    }
    
    @Test
    public void testFromHL7NoName() {
        Person result = Hl7v2Based.parse("u\\S\\fz^^^^^^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ", Person.class);

        person.setName(null);
        assertEquals(person, result);
    }
    
    @Test
    public void testFromHL7Empty() {
        assertNull(Hl7v2Based.parse("", Person.class));
    }

    @Test
    public void testFromHL7WithNullParam() {
        assertNull(Hl7v2Based.parse(null, Person.class));
    }    
}
