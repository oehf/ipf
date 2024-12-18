/**
 * OtherServiceProviderHIPAA.java
 * <p>
 * File generated from the voc::OtherServiceProviderHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration OtherServiceProviderHIPAA.
 *
 */

@XmlType(name = "OtherServiceProviderHIPAA")
@XmlEnum
@XmlRootElement(name = "OtherServiceProviderHIPAA")
public enum OtherServiceProviderHIPAA {
	@XmlEnumValue("171100000N")
	_171100000N("171100000N"),
	@XmlEnumValue("171WH0202N")
	_171WH0202N("171WH0202N"),
	@XmlEnumValue("172A00000N")
	_172A00000N("172A00000N"),
	@XmlEnumValue("173000000N")
	_173000000N("173000000N"),
	@XmlEnumValue("1744G0900N")
	_1744G0900N("1744G0900N"),
	@XmlEnumValue("1744P3200N")
	_1744P3200N("1744P3200N"),
	@XmlEnumValue("1744R1102N")
	_1744R1102N("1744R1102N"),
	@XmlEnumValue("1744R1103N")
	_1744R1103N("1744R1103N"),
	@XmlEnumValue("174MM1900N")
	_174MM1900N("174MM1900N"),
	@XmlEnumValue("175F00000N")
	_175F00000N("175F00000N"),
	@XmlEnumValue("175L00000N")
	_175L00000N("175L00000N"),
	@XmlEnumValue("175M00000N")
	_175M00000N("175M00000N"),
	@XmlEnumValue("176P00000N")
	_176P00000N("176P00000N");
	
	private final String value;

    OtherServiceProviderHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static OtherServiceProviderHIPAA fromValue(String v) {
        for (OtherServiceProviderHIPAA c: OtherServiceProviderHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}