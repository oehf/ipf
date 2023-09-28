/**
 * DocumentCompletion.java
 *
 * File generated from the voc::DocumentCompletion uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DocumentCompletion.
 *
 */

@XmlType(name = "DocumentCompletion")
@XmlEnum
@XmlRootElement(name = "DocumentCompletion")
public enum DocumentCompletion {
	@XmlEnumValue("AU")
	AU("AU"),
	@XmlEnumValue("DI")
	DI("DI"),
	@XmlEnumValue("DO")
	DO("DO"),
	@XmlEnumValue("IN")
	IN("IN"),
	@XmlEnumValue("IP")
	IP("IP"),
	@XmlEnumValue("LA")
	LA("LA"),
	@XmlEnumValue("PA")
	PA("PA");
	
	private final String value;

    DocumentCompletion(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DocumentCompletion fromValue(String v) {
        for (DocumentCompletion c: DocumentCompletion.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}