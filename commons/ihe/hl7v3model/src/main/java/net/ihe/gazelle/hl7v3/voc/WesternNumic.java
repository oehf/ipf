/**
 * WesternNumic.java
 * <p>
 * File generated from the voc::WesternNumic uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration WesternNumic.
 *
 */

@XmlType(name = "WesternNumic")
@XmlEnum
@XmlRootElement(name = "WesternNumic")
public enum WesternNumic {
	@XmlEnumValue("x-MON")
	XMON("x-MON"),
	@XmlEnumValue("x-PAO")
	XPAO("x-PAO");
	
	private final String value;

    WesternNumic(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static WesternNumic fromValue(String v) {
        for (WesternNumic c: WesternNumic.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}