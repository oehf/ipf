/**
 * ClaimantCoveredPartyRoleType.java
 *
 * File generated from the voc::ClaimantCoveredPartyRoleType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ClaimantCoveredPartyRoleType.
 *
 */

@XmlType(name = "ClaimantCoveredPartyRoleType")
@XmlEnum
@XmlRootElement(name = "ClaimantCoveredPartyRoleType")
public enum ClaimantCoveredPartyRoleType {
	@XmlEnumValue("CRIMEVIC")
	CRIMEVIC("CRIMEVIC"),
	@XmlEnumValue("INJ")
	INJ("INJ"),
	@XmlEnumValue("INJWKR")
	INJWKR("INJWKR");
	
	private final String value;

    ClaimantCoveredPartyRoleType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ClaimantCoveredPartyRoleType fromValue(String v) {
        for (ClaimantCoveredPartyRoleType c: ClaimantCoveredPartyRoleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}