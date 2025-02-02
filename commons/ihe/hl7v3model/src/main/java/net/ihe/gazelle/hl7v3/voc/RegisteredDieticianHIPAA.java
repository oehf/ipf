/**
 * RegisteredDieticianHIPAA.java
 * <p>
 * File generated from the voc::RegisteredDieticianHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RegisteredDieticianHIPAA.
 *
 */

@XmlType(name = "RegisteredDieticianHIPAA")
@XmlEnum
@XmlRootElement(name = "RegisteredDieticianHIPAA")
public enum RegisteredDieticianHIPAA {
	@XmlEnumValue("133V00000N")
	_133V00000N("133V00000N"),
	@XmlEnumValue("133VN1004N")
	_133VN1004N("133VN1004N"),
	@XmlEnumValue("133VN1005N")
	_133VN1005N("133VN1005N"),
	@XmlEnumValue("133VN1006N")
	_133VN1006N("133VN1006N");
	
	private final String value;

    RegisteredDieticianHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RegisteredDieticianHIPAA fromValue(String v) {
        for (RegisteredDieticianHIPAA c: RegisteredDieticianHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}