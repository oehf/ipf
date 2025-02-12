/**
 * ObservationEnvironmentalIntoleranceType.java
 * <p>
 * File generated from the voc::ObservationEnvironmentalIntoleranceType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ObservationEnvironmentalIntoleranceType.
 *
 */

@XmlType(name = "ObservationEnvironmentalIntoleranceType")
@XmlEnum
@XmlRootElement(name = "ObservationEnvironmentalIntoleranceType")
public enum ObservationEnvironmentalIntoleranceType {
	@XmlEnumValue("EALG")
	EALG("EALG"),
	@XmlEnumValue("EINT")
	EINT("EINT"),
	@XmlEnumValue("ENAINT")
	ENAINT("ENAINT");
	
	private final String value;

    ObservationEnvironmentalIntoleranceType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ObservationEnvironmentalIntoleranceType fromValue(String v) {
        for (ObservationEnvironmentalIntoleranceType c: ObservationEnvironmentalIntoleranceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}