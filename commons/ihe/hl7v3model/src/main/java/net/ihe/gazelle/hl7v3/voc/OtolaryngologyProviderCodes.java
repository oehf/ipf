/**
 * OtolaryngologyProviderCodes.java
 * <p>
 * File generated from the voc::OtolaryngologyProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration OtolaryngologyProviderCodes.
 *
 */

@XmlType(name = "OtolaryngologyProviderCodes")
@XmlEnum
@XmlRootElement(name = "OtolaryngologyProviderCodes")
public enum OtolaryngologyProviderCodes {
	@XmlEnumValue("207Y00000X")
	_207Y00000X("207Y00000X"),
	@XmlEnumValue("207YP0228X")
	_207YP0228X("207YP0228X"),
	@XmlEnumValue("207YS0123X")
	_207YS0123X("207YS0123X"),
	@XmlEnumValue("207YX0007X")
	_207YX0007X("207YX0007X"),
	@XmlEnumValue("207YX0602X")
	_207YX0602X("207YX0602X"),
	@XmlEnumValue("207YX0901X")
	_207YX0901X("207YX0901X"),
	@XmlEnumValue("207YX0905X")
	_207YX0905X("207YX0905X");
	
	private final String value;

    OtolaryngologyProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static OtolaryngologyProviderCodes fromValue(String v) {
        for (OtolaryngologyProviderCodes c: OtolaryngologyProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}