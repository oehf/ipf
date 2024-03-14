/**
 * EntityClassManufacturedMaterial.java
 *
 * File generated from the voc::EntityClassManufacturedMaterial uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration EntityClassManufacturedMaterial.
 *
 */

@XmlType(name = "EntityClassManufacturedMaterial")
@XmlEnum
@XmlRootElement(name = "EntityClassManufacturedMaterial")
public enum EntityClassManufacturedMaterial {
	@XmlEnumValue("CER")
	CER("CER"),
	@XmlEnumValue("CONT")
	CONT("CONT"),
	@XmlEnumValue("DEV")
	DEV("DEV"),
	@XmlEnumValue("HOLD")
	HOLD("HOLD"),
	@XmlEnumValue("MMAT")
	MMAT("MMAT"),
	@XmlEnumValue("MODDV")
	MODDV("MODDV");
	
	private final String value;

    EntityClassManufacturedMaterial(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static EntityClassManufacturedMaterial fromValue(String v) {
        for (EntityClassManufacturedMaterial c: EntityClassManufacturedMaterial.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}