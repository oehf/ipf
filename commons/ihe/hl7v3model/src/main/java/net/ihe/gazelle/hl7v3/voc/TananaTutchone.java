/**
 * TananaTutchone.java
 * <p>
 * File generated from the voc::TananaTutchone uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration TananaTutchone.
 *
 */

@XmlType(name = "TananaTutchone")
@XmlEnum
@XmlRootElement(name = "TananaTutchone")
public enum TananaTutchone {
	@XmlEnumValue("x-KUU")
	XKUU("x-KUU"),
	@XmlEnumValue("x-TAA")
	XTAA("x-TAA"),
	@XmlEnumValue("x-TAU")
	XTAU("x-TAU"),
	@XmlEnumValue("x-TCB")
	XTCB("x-TCB");
	
	private final String value;

    TananaTutchone(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static TananaTutchone fromValue(String v) {
        for (TananaTutchone c: TananaTutchone.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}