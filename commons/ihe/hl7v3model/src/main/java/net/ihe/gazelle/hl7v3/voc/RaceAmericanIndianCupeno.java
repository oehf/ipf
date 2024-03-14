/**
 * RaceAmericanIndianCupeno.java
 *
 * File generated from the voc::RaceAmericanIndianCupeno uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianCupeno.
 *
 */

@XmlType(name = "RaceAmericanIndianCupeno")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianCupeno")
public enum RaceAmericanIndianCupeno {
	@XmlEnumValue("1211-2")
	_12112("1211-2"),
	@XmlEnumValue("1212-0")
	_12120("1212-0");
	
	private final String value;

    RaceAmericanIndianCupeno(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianCupeno fromValue(String v) {
        for (RaceAmericanIndianCupeno c: RaceAmericanIndianCupeno.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}