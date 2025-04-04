/**
 * RehabilitationHospital.java
 * <p>
 * File generated from the voc::RehabilitationHospital uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RehabilitationHospital.
 *
 */

@XmlType(name = "RehabilitationHospital")
@XmlEnum
@XmlRootElement(name = "RehabilitationHospital")
public enum RehabilitationHospital {
	@XmlEnumValue("283XC2000N")
	_283XC2000N("283XC2000N"),
	@XmlEnumValue("RH")
	RH("RH");
	
	private final String value;

    RehabilitationHospital(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RehabilitationHospital fromValue(String v) {
        for (RehabilitationHospital c: RehabilitationHospital.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}