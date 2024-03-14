/**
 * LiquidCleanser.java
 *
 * File generated from the voc::LiquidCleanser uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration LiquidCleanser.
 *
 */

@XmlType(name = "LiquidCleanser")
@XmlEnum
@XmlRootElement(name = "LiquidCleanser")
public enum LiquidCleanser {
	@XmlEnumValue("LIQCLN")
	LIQCLN("LIQCLN"),
	@XmlEnumValue("LIQSOAP")
	LIQSOAP("LIQSOAP"),
	@XmlEnumValue("SHMP")
	SHMP("SHMP");
	
	private final String value;

    LiquidCleanser(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static LiquidCleanser fromValue(String v) {
        for (LiquidCleanser c: LiquidCleanser.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}