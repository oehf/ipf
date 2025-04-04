/**
 * IntratrachealRoute.java
 * <p>
 * File generated from the voc::IntratrachealRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntratrachealRoute.
 *
 */

@XmlType(name = "IntratrachealRoute")
@XmlEnum
@XmlRootElement(name = "IntratrachealRoute")
public enum IntratrachealRoute {
	@XmlEnumValue("ITRACHINSTIL")
	ITRACHINSTIL("ITRACHINSTIL"),
	@XmlEnumValue("ITRACHMAB")
	ITRACHMAB("ITRACHMAB");
	
	private final String value;

    IntratrachealRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntratrachealRoute fromValue(String v) {
        for (IntratrachealRoute c: IntratrachealRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}