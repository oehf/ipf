/**
 * RacePacificIslandPolynesian.java
 *
 * File generated from the voc::RacePacificIslandPolynesian uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RacePacificIslandPolynesian.
 *
 */

@XmlType(name = "RacePacificIslandPolynesian")
@XmlEnum
@XmlRootElement(name = "RacePacificIslandPolynesian")
public enum RacePacificIslandPolynesian {
	@XmlEnumValue("2078-4")
	_20784("2078-4"),
	@XmlEnumValue("2079-2")
	_20792("2079-2"),
	@XmlEnumValue("2080-0")
	_20800("2080-0"),
	@XmlEnumValue("2081-8")
	_20818("2081-8"),
	@XmlEnumValue("2082-6")
	_20826("2082-6"),
	@XmlEnumValue("2083-4")
	_20834("2083-4");
	
	private final String value;

    RacePacificIslandPolynesian(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RacePacificIslandPolynesian fromValue(String v) {
        for (RacePacificIslandPolynesian c: RacePacificIslandPolynesian.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}