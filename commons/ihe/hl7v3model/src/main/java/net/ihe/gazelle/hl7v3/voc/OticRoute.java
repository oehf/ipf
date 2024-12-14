/**
 * OticRoute.java
 * <p>
 * File generated from the voc::OticRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration OticRoute.
 *
 */

@XmlType(name = "OticRoute")
@XmlEnum
@XmlRootElement(name = "OticRoute")
public enum OticRoute {
	@XmlEnumValue("OT")
	OT("OT");
	
	private final String value;

    OticRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static OticRoute fromValue(String v) {
        for (OticRoute c: OticRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}