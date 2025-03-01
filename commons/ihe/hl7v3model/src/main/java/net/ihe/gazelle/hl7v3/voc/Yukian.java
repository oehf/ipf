/**
 * Yukian.java
 * <p>
 * File generated from the voc::Yukian uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Yukian.
 *
 */

@XmlType(name = "Yukian")
@XmlEnum
@XmlRootElement(name = "Yukian")
public enum Yukian {
	@XmlEnumValue("x-WAO")
	XWAO("x-WAO"),
	@XmlEnumValue("x-YUK")
	XYUK("x-YUK");
	
	private final String value;

    Yukian(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Yukian fromValue(String v) {
        for (Yukian c: Yukian.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}