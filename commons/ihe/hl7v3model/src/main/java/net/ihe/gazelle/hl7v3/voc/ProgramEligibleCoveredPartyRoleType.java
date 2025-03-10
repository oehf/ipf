/**
 * ProgramEligibleCoveredPartyRoleType.java
 * <p>
 * File generated from the voc::ProgramEligibleCoveredPartyRoleType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ProgramEligibleCoveredPartyRoleType.
 *
 */

@XmlType(name = "ProgramEligibleCoveredPartyRoleType")
@XmlEnum
@XmlRootElement(name = "ProgramEligibleCoveredPartyRoleType")
public enum ProgramEligibleCoveredPartyRoleType {
	@XmlEnumValue("ACTMIL")
	ACTMIL("ACTMIL"),
	@XmlEnumValue("CRIMEVIC")
	CRIMEVIC("CRIMEVIC"),
	@XmlEnumValue("DIFFABL")
	DIFFABL("DIFFABL"),
	@XmlEnumValue("INDIG")
	INDIG("INDIG"),
	@XmlEnumValue("INJWKR")
	INJWKR("INJWKR"),
	@XmlEnumValue("MIL")
	MIL("MIL"),
	@XmlEnumValue("RETMIL")
	RETMIL("RETMIL"),
	@XmlEnumValue("VET")
	VET("VET"),
	@XmlEnumValue("WARD")
	WARD("WARD");
	
	private final String value;

    ProgramEligibleCoveredPartyRoleType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ProgramEligibleCoveredPartyRoleType fromValue(String v) {
        for (ProgramEligibleCoveredPartyRoleType c: ProgramEligibleCoveredPartyRoleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}