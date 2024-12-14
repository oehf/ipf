/**
 * ObstetricsGynecologyProviderCodes.java
 * <p>
 * File generated from the voc::ObstetricsGynecologyProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ObstetricsGynecologyProviderCodes.
 *
 */

@XmlType(name = "ObstetricsGynecologyProviderCodes")
@XmlEnum
@XmlRootElement(name = "ObstetricsGynecologyProviderCodes")
public enum ObstetricsGynecologyProviderCodes {
	@XmlEnumValue("207V00000X")
	_207V00000X("207V00000X"),
	@XmlEnumValue("207VC0200X")
	_207VC0200X("207VC0200X"),
	@XmlEnumValue("207VE0102X")
	_207VE0102X("207VE0102X"),
	@XmlEnumValue("207VG0400X")
	_207VG0400X("207VG0400X"),
	@XmlEnumValue("207VM0101X")
	_207VM0101X("207VM0101X"),
	@XmlEnumValue("207VX0000X")
	_207VX0000X("207VX0000X"),
	@XmlEnumValue("207VX0201X")
	_207VX0201X("207VX0201X");
	
	private final String value;

    ObstetricsGynecologyProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ObstetricsGynecologyProviderCodes fromValue(String v) {
        for (ObstetricsGynecologyProviderCodes c: ObstetricsGynecologyProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}