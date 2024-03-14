/**
 * PharmacistProviderCodes.java
 *
 * File generated from the voc::PharmacistProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PharmacistProviderCodes.
 *
 */

@XmlType(name = "PharmacistProviderCodes")
@XmlEnum
@XmlRootElement(name = "PharmacistProviderCodes")
public enum PharmacistProviderCodes {
	@XmlEnumValue("183500000X")
	_183500000X("183500000X"),
	@XmlEnumValue("1835G0000X")
	_1835G0000X("1835G0000X"),
	@XmlEnumValue("1835N0905X")
	_1835N0905X("1835N0905X"),
	@XmlEnumValue("1835N1003X")
	_1835N1003X("1835N1003X"),
	@XmlEnumValue("1835P1200X")
	_1835P1200X("1835P1200X"),
	@XmlEnumValue("1835P1300X")
	_1835P1300X("1835P1300X");
	
	private final String value;

    PharmacistProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PharmacistProviderCodes fromValue(String v) {
        for (PharmacistProviderCodes c: PharmacistProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}