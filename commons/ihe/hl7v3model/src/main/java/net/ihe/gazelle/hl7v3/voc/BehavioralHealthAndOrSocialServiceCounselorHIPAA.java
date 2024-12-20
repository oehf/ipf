/**
 * BehavioralHealthAndOrSocialServiceCounselorHIPAA.java
 * <p>
 * File generated from the voc::BehavioralHealthAndOrSocialServiceCounselorHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration BehavioralHealthAndOrSocialServiceCounselorHIPAA.
 *
 */

@XmlType(name = "BehavioralHealthAndOrSocialServiceCounselorHIPAA")
@XmlEnum
@XmlRootElement(name = "BehavioralHealthAndOrSocialServiceCounselorHIPAA")
public enum BehavioralHealthAndOrSocialServiceCounselorHIPAA {
	@XmlEnumValue("101Y00000N")
	_101Y00000N("101Y00000N"),
	@XmlEnumValue("101YA0400N")
	_101YA0400N("101YA0400N"),
	@XmlEnumValue("101YM0800N")
	_101YM0800N("101YM0800N"),
	@XmlEnumValue("101YP1600N")
	_101YP1600N("101YP1600N"),
	@XmlEnumValue("101YP2500N")
	_101YP2500N("101YP2500N"),
	@XmlEnumValue("101YS0200N")
	_101YS0200N("101YS0200N");
	
	private final String value;

    BehavioralHealthAndOrSocialServiceCounselorHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static BehavioralHealthAndOrSocialServiceCounselorHIPAA fromValue(String v) {
        for (BehavioralHealthAndOrSocialServiceCounselorHIPAA c: BehavioralHealthAndOrSocialServiceCounselorHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}