/**
 * SubstanceAbuseDisorderRehabilitationFacilityProviderCodes.java
 *
 * File generated from the voc::SubstanceAbuseDisorderRehabilitationFacilityProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SubstanceAbuseDisorderRehabilitationFacilityProviderCodes.
 *
 */

@XmlType(name = "SubstanceAbuseDisorderRehabilitationFacilityProviderCodes")
@XmlEnum
@XmlRootElement(name = "SubstanceAbuseDisorderRehabilitationFacilityProviderCodes")
public enum SubstanceAbuseDisorderRehabilitationFacilityProviderCodes {
	@XmlEnumValue("324500000X")
	_324500000X("324500000X"),
	@XmlEnumValue("3245S0500X")
	_3245S0500X("3245S0500X");
	
	private final String value;

    SubstanceAbuseDisorderRehabilitationFacilityProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SubstanceAbuseDisorderRehabilitationFacilityProviderCodes fromValue(String v) {
        for (SubstanceAbuseDisorderRehabilitationFacilityProviderCodes c: SubstanceAbuseDisorderRehabilitationFacilityProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}