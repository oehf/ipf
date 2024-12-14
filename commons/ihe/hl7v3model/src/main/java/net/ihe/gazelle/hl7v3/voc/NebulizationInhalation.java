/**
 * NebulizationInhalation.java
 * <p>
 * File generated from the voc::NebulizationInhalation uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration NebulizationInhalation.
 *
 */

@XmlType(name = "NebulizationInhalation")
@XmlEnum
@XmlRootElement(name = "NebulizationInhalation")
public enum NebulizationInhalation {
	@XmlEnumValue("NASNEB")
	NASNEB("NASNEB"),
	@XmlEnumValue("NEB")
	NEB("NEB"),
	@XmlEnumValue("ORNEB")
	ORNEB("ORNEB");
	
	private final String value;

    NebulizationInhalation(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static NebulizationInhalation fromValue(String v) {
        for (NebulizationInhalation c: NebulizationInhalation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}