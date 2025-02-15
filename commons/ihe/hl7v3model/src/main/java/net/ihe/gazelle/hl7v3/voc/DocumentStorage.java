/**
 * DocumentStorage.java
 * <p>
 * File generated from the voc::DocumentStorage uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DocumentStorage.
 *
 */

@XmlType(name = "DocumentStorage")
@XmlEnum
@XmlRootElement(name = "DocumentStorage")
public enum DocumentStorage {
	@XmlEnumValue("AA")
	AA("AA"),
	@XmlEnumValue("AC")
	AC("AC"),
	@XmlEnumValue("AR")
	AR("AR"),
	@XmlEnumValue("PU")
	PU("PU");
	
	private final String value;

    DocumentStorage(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DocumentStorage fromValue(String v) {
        for (DocumentStorage c: DocumentStorage.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}