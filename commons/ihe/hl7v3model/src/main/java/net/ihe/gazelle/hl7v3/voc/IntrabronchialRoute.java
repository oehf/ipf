/**
 * IntrabronchialRoute.java
 * <p>
 * File generated from the voc::IntrabronchialRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntrabronchialRoute.
 *
 */

@XmlType(name = "IntrabronchialRoute")
@XmlEnum
@XmlRootElement(name = "IntrabronchialRoute")
public enum IntrabronchialRoute {
	@XmlEnumValue("IBRONCHINSTIL")
	IBRONCHINSTIL("IBRONCHINSTIL");
	
	private final String value;

    IntrabronchialRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntrabronchialRoute fromValue(String v) {
        for (IntrabronchialRoute c: IntrabronchialRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}