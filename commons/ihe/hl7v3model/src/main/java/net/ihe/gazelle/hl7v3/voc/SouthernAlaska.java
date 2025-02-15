/**
 * SouthernAlaska.java
 * <p>
 * File generated from the voc::SouthernAlaska uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SouthernAlaska.
 *
 */

@XmlType(name = "SouthernAlaska")
@XmlEnum
@XmlRootElement(name = "SouthernAlaska")
public enum SouthernAlaska {
	@XmlEnumValue("x-AHT")
	XAHT("x-AHT"),
	@XmlEnumValue("x-TFN")
	XTFN("x-TFN");
	
	private final String value;

    SouthernAlaska(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SouthernAlaska fromValue(String v) {
        for (SouthernAlaska c: SouthernAlaska.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}