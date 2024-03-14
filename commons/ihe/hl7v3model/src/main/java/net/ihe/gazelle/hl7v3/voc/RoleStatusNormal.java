/**
 * RoleStatusNormal.java
 *
 * File generated from the voc::RoleStatusNormal uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RoleStatusNormal.
 *
 */

@XmlType(name = "RoleStatusNormal")
@XmlEnum
@XmlRootElement(name = "RoleStatusNormal")
public enum RoleStatusNormal {
	@XmlEnumValue("active")
	ACTIVE("active"),
	@XmlEnumValue("cancelled")
	CANCELLED("cancelled"),
	@XmlEnumValue("normal")
	NORMAL("normal"),
	@XmlEnumValue("pending")
	PENDING("pending"),
	@XmlEnumValue("suspended")
	SUSPENDED("suspended"),
	@XmlEnumValue("terminated")
	TERMINATED("terminated");
	
	private final String value;

    RoleStatusNormal(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RoleStatusNormal fromValue(String v) {
        for (RoleStatusNormal c: RoleStatusNormal.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}