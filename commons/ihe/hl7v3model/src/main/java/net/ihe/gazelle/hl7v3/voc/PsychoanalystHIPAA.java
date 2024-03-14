/**
 * PsychoanalystHIPAA.java
 *
 * File generated from the voc::PsychoanalystHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PsychoanalystHIPAA.
 *
 */

@XmlType(name = "PsychoanalystHIPAA")
@XmlEnum
@XmlRootElement(name = "PsychoanalystHIPAA")
public enum PsychoanalystHIPAA {
	@XmlEnumValue("103S00000N")
	_103S00000N("103S00000N"),
	@XmlEnumValue("103SA1400N")
	_103SA1400N("103SA1400N"),
	@XmlEnumValue("103SA1800N")
	_103SA1800N("103SA1800N");
	
	private final String value;

    PsychoanalystHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PsychoanalystHIPAA fromValue(String v) {
        for (PsychoanalystHIPAA c: PsychoanalystHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}