/**
 * Nootkan.java
 *
 * File generated from the voc::Nootkan uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Nootkan.
 *
 */

@XmlType(name = "Nootkan")
@XmlEnum
@XmlRootElement(name = "Nootkan")
public enum Nootkan {
	@XmlEnumValue("x-MYH")
	XMYH("x-MYH");
	
	private final String value;

    Nootkan(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Nootkan fromValue(String v) {
        for (Nootkan c: Nootkan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}