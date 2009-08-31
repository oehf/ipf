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
import org.openehealth.ipf.commons.ihe.xds.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.metadata.Name;
import org.openehealth.ipf.commons.ihe.xds.metadata.Person;

/**
 * Transformation logic for a {@link Person}.
 * <p>
 * This class offers transformation between {@link Person} and an HL7v2.5
 * XCN string.
 * @author Jens Riemschneider
 */
public class PersonTransformer {
    private final AssigningAuthorityTransformer assigningAuthorityTransformer = 
        new AssigningAuthorityTransformer();
    
    /**
     * Creates a person instance via an HL7 XCN string.
     * @param hl7Value
     *          the HL7 XCN string. Can be <code>null</code>.
     * @return the created Person instance. <code>null</code> if no relevant 
     *          data was found in the HL7 string or the input was <code>null</code>.
     */
    public Person fromHL7(String hl7XCN) {
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7XCN);
        List<String> fn = HL7.parse(HL7Delimiter.SUBCOMPONENT, HL7.get(parts, 2, false)); 

        String idNumber = HL7.get(parts, 1, true);
        AssigningAuthority assigningAuthority = 
            assigningAuthorityTransformer.fromHL7(HL7.get(parts, 9, false));
        
        Identifiable id = null;
        if (idNumber != null || assigningAuthority != null) {            
            id = new Identifiable();
            id.setId(idNumber);
            id.setAssigningAuthority(assigningAuthority);
        }
        
        String familyName = HL7.get(fn, 1, true);
        String givenName = HL7.get(parts, 3, true);
        String secondAndFurtherGivenNames = HL7.get(parts, 4, true);
        String suffix = HL7.get(parts, 5, true);
        String prefix = HL7.get(parts, 6, true);
        
        Name name = null;
        if (familyName != null || givenName != null || secondAndFurtherGivenNames != null 
                || suffix != null || prefix != null) {
            name = new Name();
            name.setFamilyName(familyName);
            name.setGivenName(givenName);
            name.setSecondAndFurtherGivenNames(secondAndFurtherGivenNames);
            name.setSuffix(suffix);
            name.setPrefix(prefix);
        }

        if (id != null || name != null) {
            Person person = new Person();
            person.setId(id);
            person.setName(name);
            return person;
        }
        
        return null;
    }
    
    /**
     * Transforms a person instance into an HL7v2 XCN string.
     * @param person
     *          the person to transform. Can be <code>null</code>.
     * @return the HL7 representation. <code>null</code> if the input was <code>null</code> 
     *          or the resulting HL7 string would be empty.
     */
    public String toHL7(Person person) {
        if (person == null) {
            return null;
        }
        
        String assigningAuthority = null;
        String idAsString = null;
        
        Identifiable id = person.getId();        
        if (id != null) {
            assigningAuthority = assigningAuthorityTransformer.toHL7(id.getAssigningAuthority());
            idAsString = id.getId();
        }
        
        Name name = person.getName();
        if (name == null) {
            return HL7.render(HL7Delimiter.COMPONENT,
                    HL7.escape(idAsString), 
                    null, null, null, null, null, null, null,
                    assigningAuthority);
        }
        
        return HL7.render(HL7Delimiter.COMPONENT,
                HL7.escape(idAsString), 
                HL7.escape(name.getFamilyName()), 
                HL7.escape(name.getGivenName()), 
                HL7.escape(name.getSecondAndFurtherGivenNames()),
                HL7.escape(name.getSuffix()), 
                HL7.escape(name.getPrefix()),
                null,
                null,
                assigningAuthority);
    }
}
