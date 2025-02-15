/**
 * RoleClassEquivalentEntity.java
 * <p>
 * File generated from the voc::RoleClassEquivalentEntity uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RoleClassEquivalentEntity.
 *
 */

@XmlType(name = "RoleClassEquivalentEntity")
@XmlEnum
@XmlRootElement(name = "RoleClassEquivalentEntity")
public enum RoleClassEquivalentEntity {
	@XmlEnumValue("SAME")
	SAME("SAME"),
	@XmlEnumValue("SUBY")
	SUBY("SUBY");
	
	private final String value;

    RoleClassEquivalentEntity(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RoleClassEquivalentEntity fromValue(String v) {
        for (RoleClassEquivalentEntity c: RoleClassEquivalentEntity.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}