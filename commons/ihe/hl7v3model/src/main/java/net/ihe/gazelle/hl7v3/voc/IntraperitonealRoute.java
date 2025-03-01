/**
 * IntraperitonealRoute.java
 * <p>
 * File generated from the voc::IntraperitonealRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntraperitonealRoute.
 *
 */

@XmlType(name = "IntraperitonealRoute")
@XmlEnum
@XmlRootElement(name = "IntraperitonealRoute")
public enum IntraperitonealRoute {
	@XmlEnumValue("CAPDINSTL")
	CAPDINSTL("CAPDINSTL"),
	@XmlEnumValue("IPERINJ")
	IPERINJ("IPERINJ"),
	@XmlEnumValue("PDPINJ")
	PDPINJ("PDPINJ"),
	@XmlEnumValue("PDPINSTL")
	PDPINSTL("PDPINSTL");
	
	private final String value;

    IntraperitonealRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntraperitonealRoute fromValue(String v) {
        for (IntraperitonealRoute c: IntraperitonealRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}