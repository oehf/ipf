/**
 * RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA.java
 *
 * File generated from the voc::RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA.
 *
 */

@XmlType(name = "RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA")
@XmlEnum
@XmlRootElement(name = "RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA")
public enum RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA {
	@XmlEnumValue("221700000N")
	_221700000N("221700000N"),
	@XmlEnumValue("222Z00000N")
	_222Z00000N("222Z00000N"),
	@XmlEnumValue("224P00000N")
	_224P00000N("224P00000N"),
	@XmlEnumValue("224Z00000N")
	_224Z00000N("224Z00000N"),
	@XmlEnumValue("225000000N")
	_225000000N("225000000N"),
	@XmlEnumValue("225100000N")
	_225100000N("225100000N"),
	@XmlEnumValue("2251C0400N")
	_2251C0400N("2251C0400N"),
	@XmlEnumValue("2251C2600N")
	_2251C2600N("2251C2600N"),
	@XmlEnumValue("2251E1200N")
	_2251E1200N("2251E1200N"),
	@XmlEnumValue("2251E1300N")
	_2251E1300N("2251E1300N"),
	@XmlEnumValue("2251G0304N")
	_2251G0304N("2251G0304N"),
	@XmlEnumValue("2251H1200N")
	_2251H1200N("2251H1200N"),
	@XmlEnumValue("2251H1300N")
	_2251H1300N("2251H1300N"),
	@XmlEnumValue("2251N0400N")
	_2251N0400N("2251N0400N"),
	@XmlEnumValue("2251P0200N")
	_2251P0200N("2251P0200N"),
	@XmlEnumValue("2251S0007N")
	_2251S0007N("2251S0007N"),
	@XmlEnumValue("2251X0800N")
	_2251X0800N("2251X0800N"),
	@XmlEnumValue("225200000N")
	_225200000N("225200000N"),
	@XmlEnumValue("225400000N")
	_225400000N("225400000N"),
	@XmlEnumValue("2255A2300N")
	_2255A2300N("2255A2300N"),
	@XmlEnumValue("2255R0406N")
	_2255R0406N("2255R0406N"),
	@XmlEnumValue("225600000N")
	_225600000N("225600000N"),
	@XmlEnumValue("225700000N")
	_225700000N("225700000N"),
	@XmlEnumValue("225800000N")
	_225800000N("225800000N"),
	@XmlEnumValue("225900000N")
	_225900000N("225900000N"),
	@XmlEnumValue("2259P1700N")
	_2259P1700N("2259P1700N"),
	@XmlEnumValue("225A00000N")
	_225A00000N("225A00000N"),
	@XmlEnumValue("225B00000N")
	_225B00000N("225B00000N"),
	@XmlEnumValue("225C00000N")
	_225C00000N("225C00000N"),
	@XmlEnumValue("225CA2400N")
	_225CA2400N("225CA2400N"),
	@XmlEnumValue("225X00000N")
	_225X00000N("225X00000N"),
	@XmlEnumValue("225XC0400N")
	_225XC0400N("225XC0400N"),
	@XmlEnumValue("225XE1200N")
	_225XE1200N("225XE1200N"),
	@XmlEnumValue("225XH1200N")
	_225XH1200N("225XH1200N"),
	@XmlEnumValue("225XH1300N")
	_225XH1300N("225XH1300N"),
	@XmlEnumValue("225XN1300N")
	_225XN1300N("225XN1300N"),
	@XmlEnumValue("225XP0200N")
	_225XP0200N("225XP0200N"),
	@XmlEnumValue("225XR0403N")
	_225XR0403N("225XR0403N"),
	@XmlEnumValue("226300000N")
	_226300000N("226300000N");
	
	private final String value;

    RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA fromValue(String v) {
        for (RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA c: RespiratoryAndOrRehabilitativeAndOrRestorativeProviderHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}