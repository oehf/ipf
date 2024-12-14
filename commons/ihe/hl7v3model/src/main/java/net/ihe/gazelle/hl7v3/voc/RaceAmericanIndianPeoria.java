/**
 * RaceAmericanIndianPeoria.java
 * <p>
 * File generated from the voc::RaceAmericanIndianPeoria uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianPeoria.
 *
 */

@XmlType(name = "RaceAmericanIndianPeoria")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianPeoria")
public enum RaceAmericanIndianPeoria {
	@XmlEnumValue("1450-6")
	_14506("1450-6"),
	@XmlEnumValue("1451-4")
	_14514("1451-4");
	
	private final String value;

    RaceAmericanIndianPeoria(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianPeoria fromValue(String v) {
        for (RaceAmericanIndianPeoria c: RaceAmericanIndianPeoria.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}