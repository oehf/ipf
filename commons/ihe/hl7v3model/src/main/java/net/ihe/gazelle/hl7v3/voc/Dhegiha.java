/**
 * Dhegiha.java
 *
 * File generated from the voc::Dhegiha uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Dhegiha.
 *
 */

@XmlType(name = "Dhegiha")
@XmlEnum
@XmlRootElement(name = "Dhegiha")
public enum Dhegiha {
	@XmlEnumValue("x-KAA")
	XKAA("x-KAA"),
	@XmlEnumValue("x-OMA")
	XOMA("x-OMA"),
	@XmlEnumValue("x-OSA")
	XOSA("x-OSA"),
	@XmlEnumValue("x-QUA")
	XQUA("x-QUA");
	
	private final String value;

    Dhegiha(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Dhegiha fromValue(String v) {
        for (Dhegiha c: Dhegiha.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}