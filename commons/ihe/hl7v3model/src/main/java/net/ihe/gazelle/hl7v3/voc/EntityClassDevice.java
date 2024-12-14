/**
 * EntityClassDevice.java
 * <p>
 * File generated from the voc::EntityClassDevice uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration EntityClassDevice.
 *
 */

@XmlType(name = "EntityClassDevice")
@XmlEnum
@XmlRootElement(name = "EntityClassDevice")
public enum EntityClassDevice {
	@XmlEnumValue("CER")
	CER("CER"),
	@XmlEnumValue("DEV")
	DEV("DEV"),
	@XmlEnumValue("MODDV")
	MODDV("MODDV");
	
	private final String value;

    EntityClassDevice(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static EntityClassDevice fromValue(String v) {
        for (EntityClassDevice c: EntityClassDevice.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}