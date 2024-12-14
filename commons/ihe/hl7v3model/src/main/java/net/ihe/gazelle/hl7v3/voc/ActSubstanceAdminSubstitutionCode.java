/**
 * ActSubstanceAdminSubstitutionCode.java
 * <p>
 * File generated from the voc::ActSubstanceAdminSubstitutionCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActSubstanceAdminSubstitutionCode.
 *
 */

@XmlType(name = "ActSubstanceAdminSubstitutionCode")
@XmlEnum
@XmlRootElement(name = "ActSubstanceAdminSubstitutionCode")
public enum ActSubstanceAdminSubstitutionCode {
	@XmlEnumValue("F")
	F("F"),
	@XmlEnumValue("G")
	G("G"),
	@XmlEnumValue("N")
	N("N"),
	@XmlEnumValue("TE")
	TE("TE");
	
	private final String value;

    ActSubstanceAdminSubstitutionCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActSubstanceAdminSubstitutionCode fromValue(String v) {
        for (ActSubstanceAdminSubstitutionCode c: ActSubstanceAdminSubstitutionCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}