/**
 * RaceAmericanIndianHoopa.java
 * <p>
 * File generated from the voc::RaceAmericanIndianHoopa uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianHoopa.
 *
 */

@XmlType(name = "RaceAmericanIndianHoopa")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianHoopa")
public enum RaceAmericanIndianHoopa {
	@XmlEnumValue("1271-6")
	_12716("1271-6"),
	@XmlEnumValue("1272-4")
	_12724("1272-4"),
	@XmlEnumValue("1273-2")
	_12732("1273-2");
	
	private final String value;

    RaceAmericanIndianHoopa(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianHoopa fromValue(String v) {
        for (RaceAmericanIndianHoopa c: RaceAmericanIndianHoopa.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}