/**
 * PathologySpecialistOrTechnologistHIPAA.java
 *
 * File generated from the voc::PathologySpecialistOrTechnologistHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PathologySpecialistOrTechnologistHIPAA.
 *
 */

@XmlType(name = "PathologySpecialistOrTechnologistHIPAA")
@XmlEnum
@XmlRootElement(name = "PathologySpecialistOrTechnologistHIPAA")
public enum PathologySpecialistOrTechnologistHIPAA {
	@XmlEnumValue("246QB0000N")
	_246QB0000N("246QB0000N"),
	@XmlEnumValue("246QC1000N")
	_246QC1000N("246QC1000N"),
	@XmlEnumValue("246QC2700N")
	_246QC2700N("246QC2700N"),
	@XmlEnumValue("246QH0000N")
	_246QH0000N("246QH0000N"),
	@XmlEnumValue("246QH0401N")
	_246QH0401N("246QH0401N"),
	@XmlEnumValue("246QH0600N")
	_246QH0600N("246QH0600N"),
	@XmlEnumValue("246QI0000N")
	_246QI0000N("246QI0000N"),
	@XmlEnumValue("246QL0900N")
	_246QL0900N("246QL0900N"),
	@XmlEnumValue("246QL0901N")
	_246QL0901N("246QL0901N"),
	@XmlEnumValue("246QM0706N")
	_246QM0706N("246QM0706N"),
	@XmlEnumValue("246QM0900N")
	_246QM0900N("246QM0900N");
	
	private final String value;

    PathologySpecialistOrTechnologistHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PathologySpecialistOrTechnologistHIPAA fromValue(String v) {
        for (PathologySpecialistOrTechnologistHIPAA c: PathologySpecialistOrTechnologistHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}