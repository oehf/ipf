/**
 * RaceAmericanIndianComanche.java
 * <p>
 * File generated from the voc::RaceAmericanIndianComanche uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianComanche.
 *
 */

@XmlType(name = "RaceAmericanIndianComanche")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianComanche")
public enum RaceAmericanIndianComanche {
	@XmlEnumValue("1175-9")
	_11759("1175-9"),
	@XmlEnumValue("1176-7")
	_11767("1176-7");
	
	private final String value;

    RaceAmericanIndianComanche(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianComanche fromValue(String v) {
        for (RaceAmericanIndianComanche c: RaceAmericanIndianComanche.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}