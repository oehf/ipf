/**
 * Swish.java
 * <p>
 * File generated from the voc::Swish uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Swish.
 *
 */

@XmlType(name = "Swish")
@XmlEnum
@XmlRootElement(name = "Swish")
public enum Swish {
	@XmlEnumValue("SWISHSPIT")
	SWISHSPIT("SWISHSPIT"),
	@XmlEnumValue("SWISHSWAL")
	SWISHSWAL("SWISHSWAL");
	
	private final String value;

    Swish(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Swish fromValue(String v) {
        for (Swish c: Swish.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}