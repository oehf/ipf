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

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary.*;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Author;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Person;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7.PersonTransformer;

/**
 * Transforms between an {@link Author} instance and its representation in ebXML.
 * @author Jens Riemschneider
 */
public class AuthorTransformer {
    private PersonTransformer personTransformer = new PersonTransformer();
    private final EbXMLFactory factory;
    
    public AuthorTransformer(EbXMLFactory ebXMLFactory) {
        notNull(ebXMLFactory, "ebXMLFactory cannot be null");
        this.factory = ebXMLFactory; 
    }

    public Classification toEbXML(Author author) {
        if (author == null) {
            return null;
        }
        
        Classification classification = factory.createClassification();
        classification.setNodeRepresentation("");

        String hl7XCN = personTransformer.toHL7(author.getAuthorPerson());
        if (hl7XCN != null) {
            classification.addSlot(SLOT_NAME_AUTHOR_PERSON, hl7XCN);
        }
        
        classification.addSlot(SLOT_NAME_AUTHOR_INSTITUTION, 
                author.getAuthorInstitution().toArray(new String[0]));
        
        classification.addSlot(SLOT_NAME_AUTHOR_ROLE, 
                author.getAuthorRole().toArray(new String[0]));

        classification.addSlot(SLOT_NAME_AUTHOR_SPECIALTY, 
                author.getAuthorSpecialty().toArray(new String[0]));
        
        return classification;
    }
    
    public Author fromEbXML(Classification classification) {
        if (classification == null) {
            return null;        
        }
        
        List<String> persons = classification.getSlotValues(SLOT_NAME_AUTHOR_PERSON);
        List<String> institutions = classification.getSlotValues(SLOT_NAME_AUTHOR_INSTITUTION);
        List<String> roles = classification.getSlotValues(SLOT_NAME_AUTHOR_ROLE);
        List<String> specialties = classification.getSlotValues(SLOT_NAME_AUTHOR_SPECIALTY);
        
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
