/**
 * CoverageEligibilityReason.java
 * <p>
 * File generated from the voc::CoverageEligibilityReason uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration CoverageEligibilityReason.
 *
 */

@XmlType(name = "CoverageEligibilityReason")
@XmlEnum
@XmlRootElement(name = "CoverageEligibilityReason")
public enum CoverageEligibilityReason {
	@XmlEnumValue("AGE")
	AGE("AGE"),
	@XmlEnumValue("CRIME")
	CRIME("CRIME"),
	@XmlEnumValue("DIS")
	DIS("DIS"),
	@XmlEnumValue("EMPLOY")
	EMPLOY("EMPLOY"),
	@XmlEnumValue("FINAN")
	FINAN("FINAN"),
	@XmlEnumValue("HEALTH")
	HEALTH("HEALTH"),
	@XmlEnumValue("MULTI")
	MULTI("MULTI"),
	@XmlEnumValue("PNC")
	PNC("PNC"),
	@XmlEnumValue("STATUTORY")
	STATUTORY("STATUTORY"),
	@XmlEnumValue("VEHIC")
	VEHIC("VEHIC"),
	@XmlEnumValue("WORK")
	WORK("WORK");
	
	private final String value;

    CoverageEligibilityReason(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static CoverageEligibilityReason fromValue(String v) {
        for (CoverageEligibilityReason c: CoverageEligibilityReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}