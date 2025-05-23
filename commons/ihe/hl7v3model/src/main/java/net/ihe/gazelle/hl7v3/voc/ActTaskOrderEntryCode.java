/**
 * ActTaskOrderEntryCode.java
 * <p>
 * File generated from the voc::ActTaskOrderEntryCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActTaskOrderEntryCode.
 *
 */

@XmlType(name = "ActTaskOrderEntryCode")
@XmlEnum
@XmlRootElement(name = "ActTaskOrderEntryCode")
public enum ActTaskOrderEntryCode {
	@XmlEnumValue("LABOE")
	LABOE("LABOE"),
	@XmlEnumValue("MEDOE")
	MEDOE("MEDOE"),
	@XmlEnumValue("OE")
	OE("OE");
	
	private final String value;

    ActTaskOrderEntryCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActTaskOrderEntryCode fromValue(String v) {
        for (ActTaskOrderEntryCode c: ActTaskOrderEntryCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}