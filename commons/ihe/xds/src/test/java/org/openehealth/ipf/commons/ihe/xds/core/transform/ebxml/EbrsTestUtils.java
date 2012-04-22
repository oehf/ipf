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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExternalIdentifier;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLInternationalString;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

/**
 * Test framework methods for ebXML related classes.
 * @author Jens Riemschneider
 */
public abstract class EbrsTestUtils {
    private EbrsTestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Asserts that a single external identifier matching the scheme is contained in the list. 
     * @param scheme
     *          the scheme to look for.
     * @param regEntry
     *          the registry entry containing the list of identifiers to check..
     * @param expectedValue
     *          the expected value of the identifier.
     * @param expectedName
     *          the expected name of the identifier.
     */
    public static void assertExternalIdentifier(
            String scheme, 
            EbXMLRegistryObject regEntry, 
            String expectedValue, 
            String expectedName) {
        
        int found = 0; 
        for (EbXMLExternalIdentifier identifier : regEntry.getExternalIdentifiers()) {
            if (identifier.getIdentificationScheme().equals(scheme)) {
                ++found;
                assertEquals(expectedValue, identifier.getValue());
                assertEquals(expectedName, toLocal(identifier.getName()).getValue());
            }
        }
        
        assertEquals(1, found);
    }

    /**
     * Asserts that a classification matching the given scheme is in the list. 
     * @param scheme
     *          the scheme to look for.
     * @param regEntry
     *          registry entry whose classifications are checked.
     * @param occurrence
     *          the index of the classification to check. The scheme can be matched
     *          multiple times. The occurrence is the index of the classification
     *          in the list of classification that match the scheme (not in the 
     *          list given by the parameter classification).
     * @param expectedNodeRepresentation
     *          the expected node representation.
     * @param expectedLocalizedIdxName
     *          the expected localized index of the name.
     * @return the classification that matched the assertion.
     */
    public static EbXMLClassification assertClassification(String scheme, EbXMLRegistryObject regEntry, int occurrence, String expectedNodeRepresentation, int expectedLocalizedIdxName) {
        List<EbXMLClassification> filtered = regEntry.getClassifications(scheme);
        assertTrue("Not enough classification matching the scheme: " + scheme, filtered.size() > occurrence);

        EbXMLClassification classification = filtered.get(occurrence);
        assertSame(regEntry.getId(), classification.getClassifiedObject());
        assertEquals(expectedNodeRepresentation, classification.getNodeRepresentation());
        if (expectedLocalizedIdxName > 0) {
            assertEquals(
                    createLocal(expectedLocalizedIdxName), 
                    toLocal(classification.getNameAsInternationalString()));
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
    public static void assertSlot(String slotName, List<EbXMLSlot> slots, String... expectedSlotValues) {
        int found = 0;
        for (EbXMLSlot slot : slots) {
            if (slot.getName().equals(slotName)) {
                List<String> values = slot.getValueList();
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
    public static LocalizedString toLocal(EbXMLInternationalString international) {
        assertEquals(1, international.getLocalizedStrings().size());
        return international.getSingleLocalizedString();
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
        return new XpnName("familyName " + idx, "givenName " + idx, "prefix " + idx, "second " + idx, "suffix " + idx, "degree " + idx);
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
