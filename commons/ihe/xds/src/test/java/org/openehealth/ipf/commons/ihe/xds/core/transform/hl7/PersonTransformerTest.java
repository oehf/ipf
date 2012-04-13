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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Name;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Person;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.PersonTransformer;

/**
 * Tests for {@link PersonTransformer}.
 * @author Jens Riemschneider
 */
public class PersonTransformerTest {
    private PersonTransformer transformer;
    private Person person;

    @Before
    public void setUp() {
        transformer = new PersonTransformer();

        AssigningAuthority assigningAuthority = new AssigningAuthority("he&llo", "1.2&.3.4", "WU&RZ");
        Identifiable id = new Identifiable("u^fz", assigningAuthority);
        Name name = new Name("Seu&fzer", "Em&il", "Ant|on", "der&7.", "D&r.", null);
        person = new Person(id, name);
    }
    
    @Test
    public void testToHL7() {
        assertEquals("u\\S\\fz^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ", 
                transformer.toHL7(person));
    }

    @Test
    public void testToHL7NoName() {
        person.setName(null);
        assertEquals("u\\S\\fz^^^^^^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ", 
                transformer.toHL7(person));
    }

    @Test
    public void testToHL7NoID() {
        person.setId(null);
        assertEquals("^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.", transformer.toHL7(person));
    }

    @Test
    public void testToHL7Empty() {
        assertNull(transformer.toHL7(new Person()));
    }
    
    @Test
    public void testToHL7WithNullParam() {
        assertNull(transformer.toHL7(null));
    }
    

    @Test
    public void testFromHL7() {
        Person result = transformer.fromHL7(
                "u\\S\\fz^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ");

        assertEquals(person, result);
    }

    @Test
    public void testFromHL7NoIDNumber() {
        Person result = transformer.fromHL7("^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.");

        person.setId(null);
        assertEquals(person, result);
    }
    
    @Test
    public void testFromHL7NoName() {
        Person result = transformer.fromHL7("u\\S\\fz^^^^^^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ");

        person.setName(null);
        assertEquals(person, result);
    }
    
    @Test
    public void testFromHL7Empty() {
        assertNull(transformer.fromHL7(""));
    }

    @Test
    public void testFromHL7WithNullParam() {
        assertNull(transformer.fromHL7(null));
    }    
}
