/**
 * GTSAbbreviationHolidaysUSNational.java
 *
 * File generated from the voc::GTSAbbreviationHolidaysUSNational uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration GTSAbbreviationHolidaysUSNational.
 *
 */

@XmlType(name = "GTSAbbreviationHolidaysUSNational")
@XmlEnum
@XmlRootElement(name = "GTSAbbreviationHolidaysUSNational")
public enum GTSAbbreviationHolidaysUSNational {
	@XmlEnumValue("JHNUS")
	JHNUS("JHNUS"),
	@XmlEnumValue("JHNUSCLM")
	JHNUSCLM("JHNUSCLM"),
	@XmlEnumValue("JHNUSIND")
	JHNUSIND("JHNUSIND"),
	@XmlEnumValue("JHNUSIND1")
	JHNUSIND1("JHNUSIND1"),
	@XmlEnumValue("JHNUSIND5")
	JHNUSIND5("JHNUSIND5"),
	@XmlEnumValue("JHNUSLBR")
	JHNUSLBR("JHNUSLBR"),
	@XmlEnumValue("JHNUSMEM")
	JHNUSMEM("JHNUSMEM"),
	@XmlEnumValue("JHNUSMEM5")
	JHNUSMEM5("JHNUSMEM5"),
	@XmlEnumValue("JHNUSMEM6")
	JHNUSMEM6("JHNUSMEM6"),
	@XmlEnumValue("JHNUSMLK")
	JHNUSMLK("JHNUSMLK"),
	@XmlEnumValue("JHNUSPRE")
	JHNUSPRE("JHNUSPRE"),
	@XmlEnumValue("JHNUSTKS")
	JHNUSTKS("JHNUSTKS"),
	@XmlEnumValue("JHNUSTKS5")
	JHNUSTKS5("JHNUSTKS5"),
	@XmlEnumValue("JHNUSVET")
	JHNUSVET("JHNUSVET");
	
	private final String value;

    GTSAbbreviationHolidaysUSNational(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static GTSAbbreviationHolidaysUSNational fromValue(String v) {
        for (GTSAbbreviationHolidaysUSNational c: GTSAbbreviationHolidaysUSNational.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}