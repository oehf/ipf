/**
 * Maiduan.java
 * <p>
 * File generated from the voc::Maiduan uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Maiduan.
 *
 */

@XmlType(name = "Maiduan")
@XmlEnum
@XmlRootElement(name = "Maiduan")
public enum Maiduan {
	@XmlEnumValue("x-MAI")
	XMAI("x-MAI"),
	@XmlEnumValue("x-NMU")
	XNMU("x-NMU"),
	@XmlEnumValue("x-NSZ")
	XNSZ("x-NSZ");
	
	private final String value;

    Maiduan(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Maiduan fromValue(String v) {
        for (Maiduan c: Maiduan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}