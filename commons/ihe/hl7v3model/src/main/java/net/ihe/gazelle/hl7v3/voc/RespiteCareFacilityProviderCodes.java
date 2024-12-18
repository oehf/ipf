/**
 * RespiteCareFacilityProviderCodes.java
 * <p>
 * File generated from the voc::RespiteCareFacilityProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RespiteCareFacilityProviderCodes.
 *
 */

@XmlType(name = "RespiteCareFacilityProviderCodes")
@XmlEnum
@XmlRootElement(name = "RespiteCareFacilityProviderCodes")
public enum RespiteCareFacilityProviderCodes {
	@XmlEnumValue("380000000X")
	_380000000X("380000000X"),
	@XmlEnumValue("385H00000X")
	_385H00000X("385H00000X"),
	@XmlEnumValue("385HR2050X")
	_385HR2050X("385HR2050X"),
	@XmlEnumValue("385HR2055X")
	_385HR2055X("385HR2055X"),
	@XmlEnumValue("385HR2060X")
	_385HR2060X("385HR2060X"),
	@XmlEnumValue("385HR2065X")
	_385HR2065X("385HR2065X");
	
	private final String value;

    RespiteCareFacilityProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RespiteCareFacilityProviderCodes fromValue(String v) {
        for (RespiteCareFacilityProviderCodes c: RespiteCareFacilityProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}