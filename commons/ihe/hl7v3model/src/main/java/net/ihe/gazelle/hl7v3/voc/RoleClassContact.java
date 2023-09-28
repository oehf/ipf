/**
 * RoleClassContact.java
 *
 * File generated from the voc::RoleClassContact uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RoleClassContact.
 *
 */

@XmlType(name = "RoleClassContact")
@XmlEnum
@XmlRootElement(name = "RoleClassContact")
public enum RoleClassContact {
	@XmlEnumValue("CON")
	CON("CON"),
	@XmlEnumValue("ECON")
	ECON("ECON"),
	@XmlEnumValue("NOK")
	NOK("NOK");
	
	private final String value;

    RoleClassContact(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RoleClassContact fromValue(String v) {
        for (RoleClassContact c: RoleClassContact.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}