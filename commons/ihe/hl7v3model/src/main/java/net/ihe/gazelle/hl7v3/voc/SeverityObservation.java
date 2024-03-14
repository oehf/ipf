/**
 * SeverityObservation.java
 *
 * File generated from the voc::SeverityObservation uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SeverityObservation.
 *
 */

@XmlType(name = "SeverityObservation")
@XmlEnum
@XmlRootElement(name = "SeverityObservation")
public enum SeverityObservation {
	@XmlEnumValue("H")
	H("H"),
	@XmlEnumValue("L")
	L("L"),
	@XmlEnumValue("M")
	M("M");
	
	private final String value;

    SeverityObservation(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SeverityObservation fromValue(String v) {
        for (SeverityObservation c: SeverityObservation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}