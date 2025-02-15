/**
 * RaceAmericanIndianCaddo.java
 * <p>
 * File generated from the voc::RaceAmericanIndianCaddo uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianCaddo.
 *
 */

@XmlType(name = "RaceAmericanIndianCaddo")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianCaddo")
public enum RaceAmericanIndianCaddo {
	@XmlEnumValue("1041-3")
	_10413("1041-3"),
	@XmlEnumValue("1042-1")
	_10421("1042-1");
	
	private final String value;

    RaceAmericanIndianCaddo(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianCaddo fromValue(String v) {
        for (RaceAmericanIndianCaddo c: RaceAmericanIndianCaddo.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}