/**
 * PsychologistProviderCodes.java
 * <p>
 * File generated from the voc::PsychologistProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PsychologistProviderCodes.
 *
 */

@XmlType(name = "PsychologistProviderCodes")
@XmlEnum
@XmlRootElement(name = "PsychologistProviderCodes")
public enum PsychologistProviderCodes {
	@XmlEnumValue("103T00000X")
	_103T00000X("103T00000X"),
	@XmlEnumValue("103TA0400X")
	_103TA0400X("103TA0400X"),
	@XmlEnumValue("103TA0700X")
	_103TA0700X("103TA0700X"),
	@XmlEnumValue("103TB0200X")
	_103TB0200X("103TB0200X"),
	@XmlEnumValue("103TC0700X")
	_103TC0700X("103TC0700X"),
	@XmlEnumValue("103TC1900X")
	_103TC1900X("103TC1900X"),
	@XmlEnumValue("103TC2200X")
	_103TC2200X("103TC2200X"),
	@XmlEnumValue("103TE1000X")
	_103TE1000X("103TE1000X"),
	@XmlEnumValue("103TE1100X")
	_103TE1100X("103TE1100X"),
	@XmlEnumValue("103TF0000X")
	_103TF0000X("103TF0000X"),
	@XmlEnumValue("103TF0200X")
	_103TF0200X("103TF0200X"),
	@XmlEnumValue("103TH0100X")
	_103TH0100X("103TH0100X"),
	@XmlEnumValue("103TM1700X")
	_103TM1700X("103TM1700X"),
	@XmlEnumValue("103TM1800X")
	_103TM1800X("103TM1800X"),
	@XmlEnumValue("103TP0814X")
	_103TP0814X("103TP0814X"),
	@XmlEnumValue("103TP2700X")
	_103TP2700X("103TP2700X"),
	@XmlEnumValue("103TP2701X")
	_103TP2701X("103TP2701X"),
	@XmlEnumValue("103TR0400X")
	_103TR0400X("103TR0400X"),
	@XmlEnumValue("103TS0200X")
	_103TS0200X("103TS0200X"),
	@XmlEnumValue("103TW0100X")
	_103TW0100X("103TW0100X");
	
	private final String value;

    PsychologistProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PsychologistProviderCodes fromValue(String v) {
        for (PsychologistProviderCodes c: PsychologistProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}