/**
 * RaceAmericanIndianChumash.java
 *
 * File generated from the voc::RaceAmericanIndianChumash uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianChumash.
 *
 */

@XmlType(name = "RaceAmericanIndianChumash")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianChumash")
public enum RaceAmericanIndianChumash {
	@XmlEnumValue("1162-7")
	_11627("1162-7"),
	@XmlEnumValue("1163-5")
	_11635("1163-5");
	
	private final String value;

    RaceAmericanIndianChumash(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianChumash fromValue(String v) {
        for (RaceAmericanIndianChumash c: RaceAmericanIndianChumash.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}