/**
 * IntrailealRoute.java
 *
 * File generated from the voc::IntrailealRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntrailealRoute.
 *
 */

@XmlType(name = "IntrailealRoute")
@XmlEnum
@XmlRootElement(name = "IntrailealRoute")
public enum IntrailealRoute {
	@XmlEnumValue("IILEALINJ")
	IILEALINJ("IILEALINJ"),
	@XmlEnumValue("IILEALTA")
	IILEALTA("IILEALTA");
	
	private final String value;

    IntrailealRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntrailealRoute fromValue(String v) {
        for (IntrailealRoute c: IntrailealRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}