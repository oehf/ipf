/**
 * IntratesticularRoute.java
 * <p>
 * File generated from the voc::IntratesticularRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntratesticularRoute.
 *
 */

@XmlType(name = "IntratesticularRoute")
@XmlEnum
@XmlRootElement(name = "IntratesticularRoute")
public enum IntratesticularRoute {
	@XmlEnumValue("ITESTINJ")
	ITESTINJ("ITESTINJ");
	
	private final String value;

    IntratesticularRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntratesticularRoute fromValue(String v) {
        for (IntratesticularRoute c: IntratesticularRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}