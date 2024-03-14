/**
 * Keresan.java
 *
 * File generated from the voc::Keresan uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Keresan.
 *
 */

@XmlType(name = "Keresan")
@XmlEnum
@XmlRootElement(name = "Keresan")
public enum Keresan {
	@XmlEnumValue("x-KEE")
	XKEE("x-KEE"),
	@XmlEnumValue("x-KJQ")
	XKJQ("x-KJQ");
	
	private final String value;

    Keresan(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Keresan fromValue(String v) {
        for (Keresan c: Keresan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}