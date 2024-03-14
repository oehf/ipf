/**
 * ParticipationVerifier.java
 *
 * File generated from the voc::ParticipationVerifier uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ParticipationVerifier.
 *
 */

@XmlType(name = "ParticipationVerifier")
@XmlEnum
@XmlRootElement(name = "ParticipationVerifier")
public enum ParticipationVerifier {
	@XmlEnumValue("AUTHEN")
	AUTHEN("AUTHEN"),
	@XmlEnumValue("LA")
	LA("LA"),
	@XmlEnumValue("VRF")
	VRF("VRF");
	
	private final String value;

    ParticipationVerifier(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ParticipationVerifier fromValue(String v) {
        for (ParticipationVerifier c: ParticipationVerifier.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}