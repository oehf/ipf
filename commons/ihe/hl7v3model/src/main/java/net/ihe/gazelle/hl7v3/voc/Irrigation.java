/**
 * Irrigation.java
 *
 * File generated from the voc::Irrigation uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Irrigation.
 *
 */

@XmlType(name = "Irrigation")
@XmlEnum
@XmlRootElement(name = "Irrigation")
public enum Irrigation {
	@XmlEnumValue("BLADIRR")
	BLADIRR("BLADIRR"),
	@XmlEnumValue("BLADIRRC")
	BLADIRRC("BLADIRRC"),
	@XmlEnumValue("BLADIRRT")
	BLADIRRT("BLADIRRT"),
	@XmlEnumValue("GUIRR")
	GUIRR("GUIRR"),
	@XmlEnumValue("IGASTIRR")
	IGASTIRR("IGASTIRR"),
	@XmlEnumValue("ILESIRR")
	ILESIRR("ILESIRR"),
	@XmlEnumValue("IOIRR")
	IOIRR("IOIRR"),
	@XmlEnumValue("RECIRR")
	RECIRR("RECIRR");
	
	private final String value;

    Irrigation(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Irrigation fromValue(String v) {
        for (Irrigation c: Irrigation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}