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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import static java.util.Objects.requireNonNull;

import ca.uhn.hl7v2.model.Composite;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Transforms between an {@link Author} instance and its representation in ebXML.
 * @author Jens Riemschneider
 */
public class AuthorTransformer {
    private final EbXMLFactory factory;

    /**
     * Constructs the transformer
     * @param ebXMLFactory
     *          factory for version independent ebXML objects.
     */
    public AuthorTransformer(EbXMLFactory ebXMLFactory) {
        factory = requireNonNull(ebXMLFactory, "ebXMLFactory cannot be null");
    }

    /**
     * Transforms an {@link Author} to a {@link EbXMLClassification}.
     * @param author
     *          the author. Can be <code>null</code>.
     * @param objectLibrary
     *          the object library to use.
     * @return the classification. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLClassification toEbXML(Author author, EbXMLObjectLibrary objectLibrary) {
        requireNonNull(objectLibrary, "objectLibrary cannot be null");

        if (author == null) {
            return null;
        }

        var classification = factory.createClassification(objectLibrary);
        classification.setNodeRepresentation("");

        var hl7XCN = Hl7v2Based.render(author.getAuthorPerson());
        if (hl7XCN != null) {
            classification.addSlot(SLOT_NAME_AUTHOR_PERSON, hl7XCN);
        }

        transformToHl7Slots(author.getAuthorInstitution(), classification, SLOT_NAME_AUTHOR_INSTITUTION);
        transformToHl7Slots(author.getAuthorRole(),        classification, SLOT_NAME_AUTHOR_ROLE);
        transformToHl7Slots(author.getAuthorSpecialty(),   classification, SLOT_NAME_AUTHOR_SPECIALTY);
        transformToHl7Slots(author.getAuthorTelecom(),     classification, SLOT_NAME_AUTHOR_TELECOM);

        return classification;
    }

    /**
     * Transforms an a {@link EbXMLClassification} to {@link Author}.
     * @param classification
     *          the classification. Can be <code>null</code>.
     * @return the author. <code>null</code> if the input was <code>null</code>.
     */
    public Author fromEbXML(EbXMLClassification classification) {
        if (classification == null) {
            return null;
        }

        var author = new Author();

        var persons = classification.getSlotValues(SLOT_NAME_AUTHOR_PERSON);
        if (!persons.isEmpty()) {
            var person = Hl7v2Based.parse(persons.get(0), Person.class);
            author.setAuthorPerson(person);
        }

        transformFromHl7Slots(classification, SLOT_NAME_AUTHOR_INSTITUTION, author.getAuthorInstitution(), Organization.class);
        transformFromHl7Slots(classification, SLOT_NAME_AUTHOR_ROLE,        author.getAuthorRole(),        Identifiable.class);
        transformFromHl7Slots(classification, SLOT_NAME_AUTHOR_SPECIALTY,   author.getAuthorSpecialty(),   Identifiable.class);
        transformFromHl7Slots(classification, SLOT_NAME_AUTHOR_TELECOM,     author.getAuthorTelecom(),     Telecom.class);

        return author;
    }


    /**
     * Extracts slots with the given name from the given classification,
     * transforms them into HL7-based XDS object model instances
     * of the given type and inserts into the given collection.
     *
     * @param sourceClassification
     *      source classification.
     * @param sourceSlotName
     *      slot name in the source classification.
     * @param targetCollection
     *      target collection of HL7-based object model instances.
     * @param targetClass
     *      target class of the HL7-based object model.
     * @param <T>
     *      target class of the HL7-based object model.
     * @param <C>
     *      composite HAPI type wrapped by {@link T}.
     */
    private static <C extends Composite, T extends Hl7v2Based<C>> void transformFromHl7Slots(
            EbXMLClassification sourceClassification,
            String sourceSlotName,
            List<T> targetCollection,
            Class<T> targetClass)
    {
        sourceClassification.getSlotValues(sourceSlotName).stream()
                .map(source -> Hl7v2Based.parse(source, targetClass))
                .filter(Objects::nonNull)
                .forEach(targetCollection::add);
    }


    /**
     * Renders HL7-based XDS object model instances contained in the given
     * collection and stores them into the given classification
     * as slot values with the given name.
     *
     * @param sourceCollection
     *      source collection of HL7-based object model instances.
     * @param targetClassification
     *      target classification.
     * @param targetSlotName
     *      slot number in the target classification.
     * @param <T>
     *      source class of the HL7-based object model.
     */
    private static <T extends Hl7v2Based<?>> void transformToHl7Slots(
            List<T> sourceCollection,
            EbXMLClassification targetClassification,
            String targetSlotName)
    {
        var targetCollection = new ArrayList<String>();
        sourceCollection.forEach(source -> {
            var target = Hl7v2Based.render(source);
            if (source != null) {
                targetCollection.add(target);
            }
        });

        var array = new String[targetCollection.size()];
        targetClassification.addSlot(targetSlotName, targetCollection.toArray(array));
    }

}
