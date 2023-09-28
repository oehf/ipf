/**
 * IntratumorRoute.java
 *
 * File generated from the voc::IntratumorRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntratumorRoute.
 *
 */

@XmlType(name = "IntratumorRoute")
@XmlEnum
@XmlRootElement(name = "IntratumorRoute")
public enum IntratumorRoute {
	@XmlEnumValue("ITUMINJ")
	ITUMINJ("ITUMINJ");
	
	private final String value;

    IntratumorRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntratumorRoute fromValue(String v) {
        for (IntratumorRoute c: IntratumorRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}