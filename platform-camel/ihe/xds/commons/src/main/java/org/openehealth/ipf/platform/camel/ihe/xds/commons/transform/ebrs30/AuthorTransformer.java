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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30;

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary.*;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Author;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Person;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.SlotType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7.PersonTransformer;

/**
 * Transforms between an {@link Author} instance and its representation in ebXML.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class AuthorTransformer {
    private PersonTransformer personTransformer = new PersonTransformer();        

    public ClassificationType toEbXML30(Author author) {
        if(author == null) {
            return null;
        }
        
        ClassificationType classification = Ebrs30.createClassification(DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        List<SlotType1> slots = classification.getSlot();
        
        String s = personTransformer.toHL7(author.getAuthorPerson());
        if(s != null) {
            Ebrs30.addSlot(slots, SLOT_NAME_AUTHOR_PERSON, s);
        }
        
        Ebrs30.addSlot(slots, SLOT_NAME_AUTHOR_INSTITUTION, 
                author.getAuthorInstitution().toArray(new String[0]));
        
        Ebrs30.addSlot(slots, SLOT_NAME_AUTHOR_ROLE, 
                author.getAuthorRole().toArray(new String[0]));

        Ebrs30.addSlot(slots, SLOT_NAME_AUTHOR_SPECIALTY, 
                author.getAuthorSpecialty().toArray(new String[0]));
        
        return classification;
    }
    
    
    public Author fromEbXML30(ClassificationType classification) {
        if (classification == null) {
            return null;        
        }
        
        List<String> persons = Ebrs30.getSlotValues(classification, SLOT_NAME_AUTHOR_PERSON);
        List<String> institutions = Ebrs30.getSlotValues(classification, SLOT_NAME_AUTHOR_INSTITUTION);
        List<String> roles = Ebrs30.getSlotValues(classification, SLOT_NAME_AUTHOR_ROLE);
        List<String> specialties = Ebrs30.getSlotValues(classification, SLOT_NAME_AUTHOR_SPECIALTY);
        
        Person person = null;
        if (persons.size() > 0 && persons.get(0) != null) {
            person = personTransformer.fromHL7(persons.get(0));
        }
        
        Author author = new Author();        
        author.setAuthorPerson(person);
        author.getAuthorInstitution().addAll(institutions);
        author.getAuthorRole().addAll(roles);
        author.getAuthorSpecialty().addAll(specialties);
        
        return author;
    }
}
