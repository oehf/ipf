/**
 * SignificantOtherRoleType.java
 *
 * File generated from the voc::SignificantOtherRoleType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SignificantOtherRoleType.
 *
 */

@XmlType(name = "SignificantOtherRoleType")
@XmlEnum
@XmlRootElement(name = "SignificantOtherRoleType")
public enum SignificantOtherRoleType {
	@XmlEnumValue("HUSB")
	HUSB("HUSB"),
	@XmlEnumValue("SIGOTHR")
	SIGOTHR("SIGOTHR"),
	@XmlEnumValue("SPS")
	SPS("SPS"),
	@XmlEnumValue("WIFE")
	WIFE("WIFE");
	
	private final String value;

    SignificantOtherRoleType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SignificantOtherRoleType fromValue(String v) {
        for (SignificantOtherRoleType c: SignificantOtherRoleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}