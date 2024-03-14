/**
 * ActNonObservationIndicationCode.java
 *
 * File generated from the voc::ActNonObservationIndicationCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActNonObservationIndicationCode.
 *
 */

@XmlType(name = "ActNonObservationIndicationCode")
@XmlEnum
@XmlRootElement(name = "ActNonObservationIndicationCode")
public enum ActNonObservationIndicationCode {
	@XmlEnumValue("IND01")
	IND01("IND01"),
	@XmlEnumValue("IND02")
	IND02("IND02"),
	@XmlEnumValue("IND03")
	IND03("IND03"),
	@XmlEnumValue("IND04")
	IND04("IND04"),
	@XmlEnumValue("IND05")
	IND05("IND05");
	
	private final String value;

    ActNonObservationIndicationCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActNonObservationIndicationCode fromValue(String v) {
        for (ActNonObservationIndicationCode c: ActNonObservationIndicationCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}