/**
 * Tiwa.java
 *
 * File generated from the voc::Tiwa uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Tiwa.
 *
 */

@XmlType(name = "Tiwa")
@XmlEnum
@XmlRootElement(name = "Tiwa")
public enum Tiwa {
	@XmlEnumValue("x-TAO")
	XTAO("x-TAO"),
	@XmlEnumValue("x-TIX")
	XTIX("x-TIX");
	
	private final String value;

    Tiwa(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Tiwa fromValue(String v) {
        for (Tiwa c: Tiwa.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}