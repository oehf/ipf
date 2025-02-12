/**
 * WesternMuskogean.java
 * <p>
 * File generated from the voc::WesternMuskogean uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration WesternMuskogean.
 *
 */

@XmlType(name = "WesternMuskogean")
@XmlEnum
@XmlRootElement(name = "WesternMuskogean")
public enum WesternMuskogean {
	@XmlEnumValue("x-CCT")
	XCCT("x-CCT"),
	@XmlEnumValue("x-CIC")
	XCIC("x-CIC");
	
	private final String value;

    WesternMuskogean(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static WesternMuskogean fromValue(String v) {
        for (WesternMuskogean c: WesternMuskogean.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}