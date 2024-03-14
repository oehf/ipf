/**
 * OtherServiceProvidersProviderCodes.java
 *
 * File generated from the voc::OtherServiceProvidersProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration OtherServiceProvidersProviderCodes.
 *
 */

@XmlType(name = "OtherServiceProvidersProviderCodes")
@XmlEnum
@XmlRootElement(name = "OtherServiceProvidersProviderCodes")
public enum OtherServiceProvidersProviderCodes {
	@XmlEnumValue("170000000X")
	_170000000X("170000000X"),
	@XmlEnumValue("170100000X")
	_170100000X("170100000X"),
	@XmlEnumValue("171100000X")
	_171100000X("171100000X"),
	@XmlEnumValue("171W00000X")
	_171W00000X("171W00000X"),
	@XmlEnumValue("171WH0202X")
	_171WH0202X("171WH0202X"),
	@XmlEnumValue("171WV0202X")
	_171WV0202X("171WV0202X"),
	@XmlEnumValue("172A00000X")
	_172A00000X("172A00000X"),
	@XmlEnumValue("173000000X")
	_173000000X("173000000X"),
	@XmlEnumValue("174400000X")
	_174400000X("174400000X"),
	@XmlEnumValue("1744G0900X")
	_1744G0900X("1744G0900X"),
	@XmlEnumValue("1744P3200X")
	_1744P3200X("1744P3200X"),
	@XmlEnumValue("1744R1102X")
	_1744R1102X("1744R1102X"),
	@XmlEnumValue("1744R1103X")
	_1744R1103X("1744R1103X"),
	@XmlEnumValue("174M00000X")
	_174M00000X("174M00000X"),
	@XmlEnumValue("174MM1900X")
	_174MM1900X("174MM1900X"),
	@XmlEnumValue("175F00000X")
	_175F00000X("175F00000X"),
	@XmlEnumValue("175L00000X")
	_175L00000X("175L00000X"),
	@XmlEnumValue("175M00000X")
	_175M00000X("175M00000X"),
	@XmlEnumValue("176B00000X")
	_176B00000X("176B00000X"),
	@XmlEnumValue("176P00000X")
	_176P00000X("176P00000X"),
	@XmlEnumValue("177F00000X")
	_177F00000X("177F00000X");
	
	private final String value;

    OtherServiceProvidersProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static OtherServiceProvidersProviderCodes fromValue(String v) {
        for (OtherServiceProvidersProviderCodes c: OtherServiceProvidersProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}