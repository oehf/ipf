/**
 * IntrasternalRoute.java
 * <p>
 * File generated from the voc::IntrasternalRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntrasternalRoute.
 *
 */

@XmlType(name = "IntrasternalRoute")
@XmlEnum
@XmlRootElement(name = "IntrasternalRoute")
public enum IntrasternalRoute {
	@XmlEnumValue("ISTERINJ")
	ISTERINJ("ISTERINJ");
	
	private final String value;

    IntrasternalRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntrasternalRoute fromValue(String v) {
        for (IntrasternalRoute c: IntrasternalRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}