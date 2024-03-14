/**
 * EndocrinologyClinic.java
 *
 * File generated from the voc::EndocrinologyClinic uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration EndocrinologyClinic.
 *
 */

@XmlType(name = "EndocrinologyClinic")
@XmlEnum
@XmlRootElement(name = "EndocrinologyClinic")
public enum EndocrinologyClinic {
	@XmlEnumValue("ENDO")
	ENDO("ENDO"),
	@XmlEnumValue("PEDE")
	PEDE("PEDE");
	
	private final String value;

    EndocrinologyClinic(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static EndocrinologyClinic fromValue(String v) {
        for (EndocrinologyClinic c: EndocrinologyClinic.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}