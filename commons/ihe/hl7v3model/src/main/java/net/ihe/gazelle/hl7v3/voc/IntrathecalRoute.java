/**
 * IntrathecalRoute.java
 * <p>
 * File generated from the voc::IntrathecalRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntrathecalRoute.
 *
 */

@XmlType(name = "IntrathecalRoute")
@XmlEnum
@XmlRootElement(name = "IntrathecalRoute")
public enum IntrathecalRoute {
	@XmlEnumValue("IT")
	IT("IT"),
	@XmlEnumValue("ITINJ")
	ITINJ("ITINJ");
	
	private final String value;

    IntrathecalRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntrathecalRoute fromValue(String v) {
        for (IntrathecalRoute c: IntrathecalRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}