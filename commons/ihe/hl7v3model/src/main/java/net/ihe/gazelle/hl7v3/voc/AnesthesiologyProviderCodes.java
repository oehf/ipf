/**
 * AnesthesiologyProviderCodes.java
 * <p>
 * File generated from the voc::AnesthesiologyProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration AnesthesiologyProviderCodes.
 *
 */

@XmlType(name = "AnesthesiologyProviderCodes")
@XmlEnum
@XmlRootElement(name = "AnesthesiologyProviderCodes")
public enum AnesthesiologyProviderCodes {
	@XmlEnumValue("207L00000X")
	_207L00000X("207L00000X"),
	@XmlEnumValue("207LA0401X")
	_207LA0401X("207LA0401X"),
	@XmlEnumValue("207LC0200X")
	_207LC0200X("207LC0200X"),
	@XmlEnumValue("207LP2900X")
	_207LP2900X("207LP2900X");
	
	private final String value;

    AnesthesiologyProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static AnesthesiologyProviderCodes fromValue(String v) {
        for (AnesthesiologyProviderCodes c: AnesthesiologyProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}