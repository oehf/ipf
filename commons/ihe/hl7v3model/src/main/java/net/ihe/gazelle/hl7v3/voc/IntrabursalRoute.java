/**
 * IntrabursalRoute.java
 * <p>
 * File generated from the voc::IntrabursalRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntrabursalRoute.
 *
 */

@XmlType(name = "IntrabursalRoute")
@XmlEnum
@XmlRootElement(name = "IntrabursalRoute")
public enum IntrabursalRoute {
	@XmlEnumValue("IBURSINJ")
	IBURSINJ("IBURSINJ");
	
	private final String value;

    IntrabursalRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntrabursalRoute fromValue(String v) {
        for (IntrabursalRoute c: IntrabursalRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}