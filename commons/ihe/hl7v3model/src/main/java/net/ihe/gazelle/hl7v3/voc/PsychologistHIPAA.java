/**
 * PsychologistHIPAA.java
 *
 * File generated from the voc::PsychologistHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PsychologistHIPAA.
 *
 */

@XmlType(name = "PsychologistHIPAA")
@XmlEnum
@XmlRootElement(name = "PsychologistHIPAA")
public enum PsychologistHIPAA {
	@XmlEnumValue("103T00000N")
	_103T00000N("103T00000N"),
	@XmlEnumValue("103TA0400N")
	_103TA0400N("103TA0400N"),
	@XmlEnumValue("103TA0700N")
	_103TA0700N("103TA0700N"),
	@XmlEnumValue("103TB0200N")
	_103TB0200N("103TB0200N"),
	@XmlEnumValue("103TC0700N")
	_103TC0700N("103TC0700N"),
	@XmlEnumValue("103TC1900N")
	_103TC1900N("103TC1900N"),
	@XmlEnumValue("103TC2200N")
	_103TC2200N("103TC2200N"),
	@XmlEnumValue("103TE1000N")
	_103TE1000N("103TE1000N"),
	@XmlEnumValue("103TE1100N")
	_103TE1100N("103TE1100N"),
	@XmlEnumValue("103TF0000N")
	_103TF0000N("103TF0000N"),
	@XmlEnumValue("103TF0200N")
	_103TF0200N("103TF0200N"),
	@XmlEnumValue("103TH0100N")
	_103TH0100N("103TH0100N"),
	@XmlEnumValue("103TM1700N")
	_103TM1700N("103TM1700N"),
	@XmlEnumValue("103TM1800N")
	_103TM1800N("103TM1800N"),
	@XmlEnumValue("103TP2700N")
	_103TP2700N("103TP2700N"),
	@XmlEnumValue("103TP2701N")
	_103TP2701N("103TP2701N"),
	@XmlEnumValue("103TR0400N")
	_103TR0400N("103TR0400N"),
	@XmlEnumValue("103TS0200N")
	_103TS0200N("103TS0200N"),
	@XmlEnumValue("103TW0100N")
	_103TW0100N("103TW0100N");
	
	private final String value;

    PsychologistHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PsychologistHIPAA fromValue(String v) {
        for (PsychologistHIPAA c: PsychologistHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}