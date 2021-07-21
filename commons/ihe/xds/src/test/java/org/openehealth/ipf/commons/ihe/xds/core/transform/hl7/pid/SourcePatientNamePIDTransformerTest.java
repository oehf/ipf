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
package org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Name;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XcnName;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XpnName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for patient name transformation in SourcePatientInfo.
 * @author Jens Riemschneider
 */
public class SourcePatientNamePIDTransformerTest {

    @Test
    public void testToHL7() {
        var patientInfo = new PatientInfo();
        Name name = new XpnName();
        name.setFamilyName("Jo|man");
        name.setGivenName("Jo|chen");
        name.setSecondAndFurtherGivenNames("Jo|achim");
        name.setSuffix("von Jo|del");
        name.setPrefix("Jo|dler");
        patientInfo.getNames().add(name);

        var iterator = patientInfo.getHl7FieldIterator("PID-5");
        assertEquals("Jo\\F\\man^Jo\\F\\chen^Jo\\F\\achim^von Jo\\F\\del^Jo\\F\\dler", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testToHL7EmptyName() {
        var patientInfo = new PatientInfo();
        patientInfo.getNames().add(new XcnName());
        var iterator = patientInfo.getHl7FieldIterator("PID-5");
        assertEquals("", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testToHL7NoName() {
        var patientInfo = new PatientInfo();
        assertFalse(patientInfo.getHl7FieldIterator("PID-5").hasNext());
    }

    
    @Test
    public void testFromHL7() {
        var patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-5").add("Jo\\F\\man^Jo\\F\\chen^Jo\\F\\achim^von Jo\\F\\del^Jo\\F\\dler");

        var names = patientInfo.getNames();
        var name = names.next();
        assertNotNull(name);
        assertEquals("Jo|man", name.getFamilyName());
        assertEquals("Jo|chen", name.getGivenName());
        assertEquals("Jo|achim", name.getSecondAndFurtherGivenNames());
        assertEquals("von Jo|del", name.getSuffix());
        assertEquals("Jo|dler", name.getPrefix());

        assertFalse(names.hasNext());
    }

    @Test
    public void testFromHL7Empty() {
        var patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-5").add("^^");
        var names = patientInfo.getNames();
        assertNull(names.next());
        assertFalse(names.hasNext());
    }

    @Test
    public void testFromHL7Null() {
        var patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-5").add(null);
        var names = patientInfo.getNames();
        assertNull(names.next());
        assertFalse(names.hasNext());
    }
}
