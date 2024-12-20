/**
 * HealthInformationTechnicianHIPAA.java
 * <p>
 * File generated from the voc::HealthInformationTechnicianHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration HealthInformationTechnicianHIPAA.
 *
 */

@XmlType(name = "HealthInformationTechnicianHIPAA")
@XmlEnum
@XmlRootElement(name = "HealthInformationTechnicianHIPAA")
public enum HealthInformationTechnicianHIPAA {
	@XmlEnumValue("2470A2800N")
	_2470A2800N("2470A2800N");
	
	private final String value;

    HealthInformationTechnicianHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static HealthInformationTechnicianHIPAA fromValue(String v) {
        for (HealthInformationTechnicianHIPAA c: HealthInformationTechnicianHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}