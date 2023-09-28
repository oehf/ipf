/**
 * SoftTissueRoute.java
 *
 * File generated from the voc::SoftTissueRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SoftTissueRoute.
 *
 */

@XmlType(name = "SoftTissueRoute")
@XmlEnum
@XmlRootElement(name = "SoftTissueRoute")
public enum SoftTissueRoute {
	@XmlEnumValue("SOFTISINJ")
	SOFTISINJ("SOFTISINJ"),
	@XmlEnumValue("SOFTISINSTIL")
	SOFTISINSTIL("SOFTISINSTIL");
	
	private final String value;

    SoftTissueRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SoftTissueRoute fromValue(String v) {
        for (SoftTissueRoute c: SoftTissueRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}