/**
 * IntraduralRoute.java
 * <p>
 * File generated from the voc::IntraduralRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntraduralRoute.
 *
 */

@XmlType(name = "IntraduralRoute")
@XmlEnum
@XmlRootElement(name = "IntraduralRoute")
public enum IntraduralRoute {
	@XmlEnumValue("IDURINJ")
	IDURINJ("IDURINJ");
	
	private final String value;

    IntraduralRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntraduralRoute fromValue(String v) {
        for (IntraduralRoute c: IntraduralRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}