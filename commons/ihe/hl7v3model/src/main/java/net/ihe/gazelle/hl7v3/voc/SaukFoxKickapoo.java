/**
 * SaukFoxKickapoo.java
 *
 * File generated from the voc::SaukFoxKickapoo uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SaukFoxKickapoo.
 *
 */

@XmlType(name = "SaukFoxKickapoo")
@XmlEnum
@XmlRootElement(name = "SaukFoxKickapoo")
public enum SaukFoxKickapoo {
	@XmlEnumValue("x-KIC")
	XKIC("x-KIC"),
	@XmlEnumValue("x-SAC")
	XSAC("x-SAC"),
	@XmlEnumValue("x-SJW")
	XSJW("x-SJW");
	
	private final String value;

    SaukFoxKickapoo(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SaukFoxKickapoo fromValue(String v) {
        for (SaukFoxKickapoo c: SaukFoxKickapoo.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}