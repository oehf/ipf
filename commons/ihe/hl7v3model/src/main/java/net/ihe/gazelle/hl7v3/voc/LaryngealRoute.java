/**
 * LaryngealRoute.java
 *
 * File generated from the voc::LaryngealRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration LaryngealRoute.
 *
 */

@XmlType(name = "LaryngealRoute")
@XmlEnum
@XmlRootElement(name = "LaryngealRoute")
public enum LaryngealRoute {
	@XmlEnumValue("LARYNGINSTIL")
	LARYNGINSTIL("LARYNGINSTIL"),
	@XmlEnumValue("LARYNGTA")
	LARYNGTA("LARYNGTA");
	
	private final String value;

    LaryngealRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static LaryngealRoute fromValue(String v) {
        for (LaryngealRoute c: LaryngealRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}