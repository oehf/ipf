/**
 * RaceAmericanIndianMiami.java
 *
 * File generated from the voc::RaceAmericanIndianMiami uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianMiami.
 *
 */

@XmlType(name = "RaceAmericanIndianMiami")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianMiami")
public enum RaceAmericanIndianMiami {
	@XmlEnumValue("1358-1")
	_13581("1358-1"),
	@XmlEnumValue("1359-9")
	_13599("1359-9"),
	@XmlEnumValue("1360-7")
	_13607("1360-7"),
	@XmlEnumValue("1361-5")
	_13615("1361-5");
	
	private final String value;

    RaceAmericanIndianMiami(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianMiami fromValue(String v) {
        for (RaceAmericanIndianMiami c: RaceAmericanIndianMiami.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}