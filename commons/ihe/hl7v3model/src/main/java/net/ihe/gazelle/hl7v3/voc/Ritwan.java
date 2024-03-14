/**
 * Ritwan.java
 *
 * File generated from the voc::Ritwan uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Ritwan.
 *
 */

@XmlType(name = "Ritwan")
@XmlEnum
@XmlRootElement(name = "Ritwan")
public enum Ritwan {
	@XmlEnumValue("x-YUR")
	XYUR("x-YUR");
	
	private final String value;

    Ritwan(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Ritwan fromValue(String v) {
        for (Ritwan c: Ritwan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}