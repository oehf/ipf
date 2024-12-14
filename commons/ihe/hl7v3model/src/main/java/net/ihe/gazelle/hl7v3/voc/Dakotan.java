/**
 * Dakotan.java
 * <p>
 * File generated from the voc::Dakotan uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Dakotan.
 *
 */

@XmlType(name = "Dakotan")
@XmlEnum
@XmlRootElement(name = "Dakotan")
public enum Dakotan {
	@XmlEnumValue("x-ASB")
	XASB("x-ASB"),
	@XmlEnumValue("x-DHG")
	XDHG("x-DHG"),
	@XmlEnumValue("x-LKT")
	XLKT("x-LKT"),
	@XmlEnumValue("x-NKT")
	XNKT("x-NKT");
	
	private final String value;

    Dakotan(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Dakotan fromValue(String v) {
        for (Dakotan c: Dakotan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}