/**
 * Cahitan.java
 * <p>
 * File generated from the voc::Cahitan uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Cahitan.
 *
 */

@XmlType(name = "Cahitan")
@XmlEnum
@XmlRootElement(name = "Cahitan")
public enum Cahitan {
	@XmlEnumValue("x-YAQ")
	XYAQ("x-YAQ");
	
	private final String value;

    Cahitan(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Cahitan fromValue(String v) {
        for (Cahitan c: Cahitan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}