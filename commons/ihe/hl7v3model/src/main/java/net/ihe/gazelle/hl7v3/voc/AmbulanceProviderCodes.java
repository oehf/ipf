/**
 * AmbulanceProviderCodes.java
 * <p>
 * File generated from the voc::AmbulanceProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration AmbulanceProviderCodes.
 *
 */

@XmlType(name = "AmbulanceProviderCodes")
@XmlEnum
@XmlRootElement(name = "AmbulanceProviderCodes")
public enum AmbulanceProviderCodes {
	@XmlEnumValue("341600000X")
	_341600000X("341600000X"),
	@XmlEnumValue("3416A0800X")
	_3416A0800X("3416A0800X"),
	@XmlEnumValue("3416L0300X")
	_3416L0300X("3416L0300X"),
	@XmlEnumValue("3416S0300X")
	_3416S0300X("3416S0300X");
	
	private final String value;

    AmbulanceProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static AmbulanceProviderCodes fromValue(String v) {
        for (AmbulanceProviderCodes c: AmbulanceProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}