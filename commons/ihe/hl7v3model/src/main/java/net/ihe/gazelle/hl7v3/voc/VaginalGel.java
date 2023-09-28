/**
 * VaginalGel.java
 *
 * File generated from the voc::VaginalGel uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration VaginalGel.
 *
 */

@XmlType(name = "VaginalGel")
@XmlEnum
@XmlRootElement(name = "VaginalGel")
public enum VaginalGel {
	@XmlEnumValue("VAGGEL")
	VAGGEL("VAGGEL"),
	@XmlEnumValue("VGELAPL")
	VGELAPL("VGELAPL");
	
	private final String value;

    VaginalGel(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static VaginalGel fromValue(String v) {
        for (VaginalGel c: VaginalGel.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}