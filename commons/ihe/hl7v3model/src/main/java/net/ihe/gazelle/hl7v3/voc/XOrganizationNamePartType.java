/**
 * XOrganizationNamePartType.java
 * <p>
 * File generated from the voc::XOrganizationNamePartType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XOrganizationNamePartType.
 *
 */

@XmlType(name = "XOrganizationNamePartType")
@XmlEnum
@XmlRootElement(name = "XOrganizationNamePartType")
public enum XOrganizationNamePartType {
	@XmlEnumValue("DEL")
	DEL("DEL"),
	@XmlEnumValue("PFX")
	PFX("PFX"),
	@XmlEnumValue("SFX")
	SFX("SFX");
	
	private final String value;

    XOrganizationNamePartType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XOrganizationNamePartType fromValue(String v) {
        for (XOrganizationNamePartType c: XOrganizationNamePartType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}