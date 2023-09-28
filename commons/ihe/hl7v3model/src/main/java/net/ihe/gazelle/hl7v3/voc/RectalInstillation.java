/**
 * RectalInstillation.java
 *
 * File generated from the voc::RectalInstillation uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RectalInstillation.
 *
 */

@XmlType(name = "RectalInstillation")
@XmlEnum
@XmlRootElement(name = "RectalInstillation")
public enum RectalInstillation {
	@XmlEnumValue("RECINSTL")
	RECINSTL("RECINSTL"),
	@XmlEnumValue("RECTINSTL")
	RECTINSTL("RECTINSTL");
	
	private final String value;

    RectalInstillation(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RectalInstillation fromValue(String v) {
        for (RectalInstillation c: RectalInstillation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}