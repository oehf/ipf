/**
 * PayorParticipationFunction.java
 * <p>
 * File generated from the voc::PayorParticipationFunction uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PayorParticipationFunction.
 *
 */

@XmlType(name = "PayorParticipationFunction")
@XmlEnum
@XmlRootElement(name = "PayorParticipationFunction")
public enum PayorParticipationFunction {
	@XmlEnumValue("CLMADJ")
	CLMADJ("CLMADJ"),
	@XmlEnumValue("ENROLL")
	ENROLL("ENROLL"),
	@XmlEnumValue("FFSMGT")
	FFSMGT("FFSMGT"),
	@XmlEnumValue("MCMGT")
	MCMGT("MCMGT"),
	@XmlEnumValue("PROVMGT")
	PROVMGT("PROVMGT"),
	@XmlEnumValue("UMGT")
	UMGT("UMGT");
	
	private final String value;

    PayorParticipationFunction(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PayorParticipationFunction fromValue(String v) {
        for (PayorParticipationFunction c: PayorParticipationFunction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}