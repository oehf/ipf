/**
 * LingualRoute.java
 * <p>
 * File generated from the voc::LingualRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration LingualRoute.
 *
 */

@XmlType(name = "LingualRoute")
@XmlEnum
@XmlRootElement(name = "LingualRoute")
public enum LingualRoute {
	@XmlEnumValue("TRNSLING")
	TRNSLING("TRNSLING");
	
	private final String value;

    LingualRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static LingualRoute fromValue(String v) {
        for (LingualRoute c: LingualRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}