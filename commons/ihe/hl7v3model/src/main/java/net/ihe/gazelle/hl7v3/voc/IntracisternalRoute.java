/**
 * IntracisternalRoute.java
 * <p>
 * File generated from the voc::IntracisternalRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntracisternalRoute.
 *
 */

@XmlType(name = "IntracisternalRoute")
@XmlEnum
@XmlRootElement(name = "IntracisternalRoute")
public enum IntracisternalRoute {
	@XmlEnumValue("ICISTERNINJ")
	ICISTERNINJ("ICISTERNINJ");
	
	private final String value;

    IntracisternalRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntracisternalRoute fromValue(String v) {
        for (IntracisternalRoute c: IntracisternalRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}