/**
 * OccupationalTherapistProviderCodes.java
 * <p>
 * File generated from the voc::OccupationalTherapistProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration OccupationalTherapistProviderCodes.
 *
 */

@XmlType(name = "OccupationalTherapistProviderCodes")
@XmlEnum
@XmlRootElement(name = "OccupationalTherapistProviderCodes")
public enum OccupationalTherapistProviderCodes {
	@XmlEnumValue("225X00000X")
	_225X00000X("225X00000X"),
	@XmlEnumValue("225XE1200X")
	_225XE1200X("225XE1200X"),
	@XmlEnumValue("225XH1200X")
	_225XH1200X("225XH1200X"),
	@XmlEnumValue("225XH1300X")
	_225XH1300X("225XH1300X"),
	@XmlEnumValue("225XN1300X")
	_225XN1300X("225XN1300X"),
	@XmlEnumValue("225XP0200X")
	_225XP0200X("225XP0200X"),
	@XmlEnumValue("225XR0403X")
	_225XR0403X("225XR0403X");
	
	private final String value;

    OccupationalTherapistProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static OccupationalTherapistProviderCodes fromValue(String v) {
        for (OccupationalTherapistProviderCodes c: OccupationalTherapistProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}