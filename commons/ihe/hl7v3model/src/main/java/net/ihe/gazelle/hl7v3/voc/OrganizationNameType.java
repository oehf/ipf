/**
 * OrganizationNameType.java
 *
 * File generated from the voc::OrganizationNameType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration OrganizationNameType.
 *
 */

@XmlType(name = "OrganizationNameType")
@XmlEnum
@XmlRootElement(name = "OrganizationNameType")
public enum OrganizationNameType {
	@XmlEnumValue("A")
	A("A"),
	@XmlEnumValue("L")
	L("L"),
	@XmlEnumValue("ST")
	ST("ST");
	
	private final String value;

    OrganizationNameType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static OrganizationNameType fromValue(String v) {
        for (OrganizationNameType c: OrganizationNameType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}