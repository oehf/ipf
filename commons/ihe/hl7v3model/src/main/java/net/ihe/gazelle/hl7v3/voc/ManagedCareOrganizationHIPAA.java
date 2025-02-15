/**
 * ManagedCareOrganizationHIPAA.java
 * <p>
 * File generated from the voc::ManagedCareOrganizationHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ManagedCareOrganizationHIPAA.
 *
 */

@XmlType(name = "ManagedCareOrganizationHIPAA")
@XmlEnum
@XmlRootElement(name = "ManagedCareOrganizationHIPAA")
public enum ManagedCareOrganizationHIPAA {
	@XmlEnumValue("302F00000N")
	_302F00000N("302F00000N"),
	@XmlEnumValue("302R00000N")
	_302R00000N("302R00000N"),
	@XmlEnumValue("305R00000N")
	_305R00000N("305R00000N"),
	@XmlEnumValue("305S00000N")
	_305S00000N("305S00000N");
	
	private final String value;

    ManagedCareOrganizationHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ManagedCareOrganizationHIPAA fromValue(String v) {
        for (ManagedCareOrganizationHIPAA c: ManagedCareOrganizationHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}