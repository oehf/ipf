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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Name;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Person;

/**
 * Tests for {@link PersonTransformer}.
 * @author Jens Riemschneider
 */
public class PersonTransformerTest {
    @Test
    public void testToHL7() {
        Identifiable id = new Identifiable();
        id.setId("u^fz");
        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setNamespaceId("he&llo");
        assigningAuthority.setUniversalId("1.2&.3.4");
        assigningAuthority.setUniversalIdType("WU&RZ");
        id.setAssigningAuthority(assigningAuthority);

        Person person = new Person();
        person.setId(id);
        Name name = new Name();
        name.setFamilyName("Seu&fzer");
        name.setGivenName("Em&il");
        name.setPrefix("D&r.");
        name.setSuffix("der&7.");
        name.setSecondAndFurtherGivenNames("Ant|on");
        person.setName(name);
        
        assertEquals("u\\S\\fz^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ", 
                new PersonTransformer().toHL7(person));
    }

    @Test
    public void testToHL7NoName() {
        Identifiable id = new Identifiable();
        id.setId("ufz");
        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setUniversalId("1.2.3.4");
        assigningAuthority.setUniversalIdType("ISO");
        id.setAssigningAuthority(assigningAuthority);
        
        Person person = new Person();
        person.setId(id);
        Name name = new Name();
        name.setGivenName("Emil");
        name.setPrefix("Dr.");
        name.setSuffix("der 7.");
        name.setSecondAndFurtherGivenNames("Anton");
        person.setName(name);
        
        assertEquals("ufz^^Emil^Anton^der 7.^Dr.^^^&1.2.3.4&ISO", new PersonTransformer().toHL7(person));
    }

    @Test
    public void testToHL7NoID() {
        Person person = new Person();
        Name name = new Name();
        name.setFamilyName("Seufzer");
        name.setGivenName("Emil");
        name.setPrefix("Dr.");
        name.setSuffix("der 7.");
        name.setSecondAndFurtherGivenNames("Anton");
        person.setName(name);

        assertEquals("^Seufzer^Emil^Anton^der 7.^Dr.", new PersonTransformer().toHL7(person));
    }

    @Test
    public void testToHL7NoIDAndName() {
        Person person = new Person();
        Name name = new Name();
        name.setGivenName("Emil");
        name.setPrefix("Dr.");
        name.setSuffix("der 7.");
        name.setSecondAndFurtherGivenNames("Anton");
        person.setName(name);

        assertEquals("^^Emil^Anton^der 7.^Dr.", new PersonTransformer().toHL7(person));
    }
    
    @Test
    public void testToHL7WithNullParam() {
        assertNull(new PersonTransformer().toHL7(null));
    }
    

    @Test
    public void testFromHL7() {
        Person person = new PersonTransformer().fromHL7(
                "u\\S\\fz^Seu\\T\\fzer^Em\\T\\il^Ant\\F\\on^der\\T\\7.^D\\T\\r.^^^he\\T\\llo&1.2\\T\\.3.4&WU\\T\\RZ");
        
        Name name = person.getName();
        assertEquals("Seu&fzer", name.getFamilyName());
        assertEquals("Em&il", name.getGivenName());
        assertEquals("D&r.", name.getPrefix());
        assertEquals("der&7.", name.getSuffix());
        assertEquals("Ant|on", name.getSecondAndFurtherGivenNames());
        assertEquals("u^fz", person.getId().getId());
        assertEquals("he&llo", person.getId().getAssigningAuthority().getNamespaceId());
        assertEquals("1.2&.3.4", person.getId().getAssigningAuthority().getUniversalId());
        assertEquals("WU&RZ", person.getId().getAssigningAuthority().getUniversalIdType());
    }

    @Test
    public void testFromHL7NoIDNumber() {
        Person person = new PersonTransformer().fromHL7("^Seufzer^Emil^Anton^der 7.^Dr.");
        Name name = person.getName();
        assertEquals("Seufzer", name.getFamilyName());
        assertEquals("Emil", name.getGivenName());
        assertEquals("Dr.", name.getPrefix());
        assertEquals("der 7.", name.getSuffix());
        assertEquals("Anton", name.getSecondAndFurtherGivenNames());
        assertNull(person.getId());
    }
    
    @Test
    public void testFromHL7NoIDNumberAndName() {
        Person person = new PersonTransformer().fromHL7("^^Emil^Anton^der 7.^Dr.");
        Name name = person.getName();
        assertNull(name.getFamilyName());
        assertEquals("Emil", name.getGivenName());
        assertEquals("Dr.", name.getPrefix());
        assertEquals("der 7.", name.getSuffix());
        assertEquals("Anton", name.getSecondAndFurtherGivenNames());
        assertNull(person.getId());
    }

    @Test
    public void testFromHL7WithNullParam() {
        Person person = new PersonTransformer().fromHL7(null);
        assertNull(person.getId());
        assertNull(person.getName());
    }    
}
