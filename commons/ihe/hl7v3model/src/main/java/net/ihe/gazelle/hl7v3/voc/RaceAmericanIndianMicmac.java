/**
 * RaceAmericanIndianMicmac.java
 * <p>
 * File generated from the voc::RaceAmericanIndianMicmac uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianMicmac.
 *
 */

@XmlType(name = "RaceAmericanIndianMicmac")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianMicmac")
public enum RaceAmericanIndianMicmac {
	@XmlEnumValue("1365-6")
	_13656("1365-6"),
	@XmlEnumValue("1366-4")
	_13664("1366-4");
	
	private final String value;

    RaceAmericanIndianMicmac(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianMicmac fromValue(String v) {
        for (RaceAmericanIndianMicmac c: RaceAmericanIndianMicmac.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}