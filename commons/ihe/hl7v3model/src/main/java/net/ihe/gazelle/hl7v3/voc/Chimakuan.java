/**
 * Chimakuan.java
 * <p>
 * File generated from the voc::Chimakuan uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Chimakuan.
 *
 */

@XmlType(name = "Chimakuan")
@XmlEnum
@XmlRootElement(name = "Chimakuan")
public enum Chimakuan {
	@XmlEnumValue("x-QUI")
	XQUI("x-QUI");
	
	private final String value;

    Chimakuan(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Chimakuan fromValue(String v) {
        for (Chimakuan c: Chimakuan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}