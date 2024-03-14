/**
 * RoleClassIsSpeciesEntity.java
 *
 * File generated from the voc::RoleClassIsSpeciesEntity uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RoleClassIsSpeciesEntity.
 *
 */

@XmlType(name = "RoleClassIsSpeciesEntity")
@XmlEnum
@XmlRootElement(name = "RoleClassIsSpeciesEntity")
public enum RoleClassIsSpeciesEntity {
	@XmlEnumValue("GEN")
	GEN("GEN"),
	@XmlEnumValue("GRIC")
	GRIC("GRIC");
	
	private final String value;

    RoleClassIsSpeciesEntity(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RoleClassIsSpeciesEntity fromValue(String v) {
        for (RoleClassIsSpeciesEntity c: RoleClassIsSpeciesEntity.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}