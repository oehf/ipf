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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Name;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Organization;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Person;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.InternationalStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.SlotType1;

/**
 * Test framework methods for ebXML 3.0 related classes.
 * @author Jens Riemschneider
 */
public abstract class Ebrs30TestUtils {
    private Ebrs30TestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Asserts that a single external identifier matching the scheme is contained in the list. 
     * @param scheme
     *          the scheme to look for.
     * @param identifiers
     *          the list of identifiers to check..
     * @param expectedValue
     *          the expected value of the identifier.
     * @param expectedName
     *          the expected name of the identifier.
     */
    public static void assertExternalIdentifier(
            String scheme, 
            List<ExternalIdentifierType> identifiers, 
            String expectedValue, 
            String expectedName) {
        
        int found = 0; 
        for (ExternalIdentifierType identifier : identifiers) {
            if (identifier.getIdentificationScheme().equals(scheme)) {
                ++found;
                assertEquals(expectedValue, identifier.getValue());
                // in 2.1: "en-us", in 3.0: "en-US" (last two letters capitalized) 
                assertEquals(new LocalizedString(expectedName, "en-US", "UTF-8"), toLocal(identifier.getName()));
            }
        }
        
        assertEquals(1, found);
    }

    /**
     * Asserts that a classification matching the given scheme is in the list. 
     * @param scheme
     *          the scheme to look for.
     * @param classifications
     *          the list of classifications to check.
     * @param occurrence
     *          the index of the classification to check. The scheme can be matched
     *          multiple times. The occurrence is the index of the classification
     *          in the list of classification that match the scheme (not in the 
     *          list given by the parameter classification).
     * @param expectedClassifiedObject
     *          the expected classified object reference.
     * @param expectedNodeRepresentation
     *          the expected node representation.
     * @param expectedLocalizedIdxName
     *          the expected localized index of the name.
     * @return the classification that matched the assertion.
     */
    public static ClassificationType assertClassification(String scheme, List<ClassificationType> classifications, int occurrence, String expectedClassifiedObject, String expectedNodeRepresentation, int expectedLocalizedIdxName) {
        List<ClassificationType> filtered = Ebrs30.getClassifications(classifications, scheme);
        assertTrue(filtered.size() > occurrence);

        ClassificationType classification = filtered.get(occurrence);
        assertEquals(expectedClassifiedObject, classification.getClassifiedObject());
        assertEquals(expectedNodeRepresentation, classification.getNodeRepresentation());
        if (expectedLocalizedIdxName > 0) {
            assertEquals(createLocal(expectedLocalizedIdxName), toLocal(classification.getName()));
        }
        
        return classification;
    }

    /**
     * Asserts that a single slot matching the slot name has the expected values.
     * @param slotName
     *          the name of the slot to check.
     * @param slots
     *          the slot list to check.
     * @param expectedSlotValues
     *          the expected values of the slot.
     */
    public static void assertSlot(String slotName, List<SlotType1> slots, String... expectedSlotValues) {
        int found = 0;
        for (SlotType1 slot : slots) {
            if (slot.getName().equals(slotName)) {
                List<String> values = slot.getValueList().getValue();
                for (String expectedValue : expectedSlotValues) {
                    assertTrue("Not found: " + expectedValue + ", was: " + values, values.contains(expectedValue));
                }
                assertEquals(values.size(), expectedSlotValues.length);
                ++found;
            }
        }
        
        assertEquals(1, found);
    }

    /**
     * Retrieves the localized string from an international string and asserts 
     * that this string is available.
     * @param international
     *          the international string.
     * @return the localized string.
     */
    public static LocalizedString toLocal(InternationalStringType international) {
        assertEquals(1, international.getLocalizedString().size());
        return Ebrs30.getSingleLocalizedString(international);
    }
    
    /**
     * Creates a standard person.
     * @param idx
     *          the index of the person. This index is used in all attributes of the
     *          person and any sub structures.
     * @return the new person instance.
     */
    public static Person createPerson(int idx) {
        Person person = new Person();
        person.setId(createIdentifiable(idx));
        person.setName(createName(idx));
        return person;
    }
    
    /**
     * Creates a standard organization.
     * @param idx
     *          the index of the organization. This index is used in all attributes of the
     *          organization and any sub structures.
     * @return the new organization instance.
     */
    public static Organization createOrganization(int idx) {
        Organization organization = new Organization();
        organization.setAssigningAuthority(createAssigningAuthority(idx));
        organization.setIdNumber("id " + idx);
        organization.setOrganizationName("orgName " + idx);
        return organization;        
    }

    /**
     * Creates a standard name.
     * @param idx
     *          the index of the name. This index is used in all attributes of the
     *          name.
     * @return the new name instance.
     */
    public static Name createName(int idx) {
        return new Name("familyName " + idx, "givenName " + idx, "prefix " + idx, "second " + idx, "suffix " + idx);
    }

    /**
     * Creates a standard identifiable.
     * @param idx
     *          the index of the identifiable. This index is used in all attributes
     *          of the identifiable and its sub structures.
     * @return the new identifiable.
     */
    public static Identifiable createIdentifiable(int idx) {
        return new Identifiable("id " + idx, createAssigningAuthority(idx));
    }

    /**
     * Creates a standard assigning authority.
     * @param idx
     *          the index of the assigning authority. This index is used in all
     *          attributes of the assigning authority.
     * @return the new assigning authority instance.
     */
    public static AssigningAuthority createAssigningAuthority(int idx) {
        return new AssigningAuthority("namespace " + idx, "uni " + idx, "uniType " + idx);
    }

    /**
     * Creates a standard code.
     * @param idx
     *          the index of the code. This index is used in all attributes of the code
     *          and its sub structure.
     * @return the new code instance.
     */
    public static Code createCode(int idx) {
        return new Code("code " + idx, createLocal(idx), "scheme " + idx);
    }

    /**
     * Creates a standard localized string. 
     * @param idx
     *          the index of the localized string. This index is used in all attributes 
     *          of the localized string.
     * @return the new localized string instance.
     */
    public static LocalizedString createLocal(int idx) {
        return new LocalizedString("value " + idx, "lang " + idx, "charset " + idx);
    }    
}
