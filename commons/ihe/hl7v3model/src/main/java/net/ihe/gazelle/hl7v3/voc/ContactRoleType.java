/**
 * ContactRoleType.java
 *
 * File generated from the voc::ContactRoleType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ContactRoleType.
 *
 */

@XmlType(name = "ContactRoleType")
@XmlEnum
@XmlRootElement(name = "ContactRoleType")
public enum ContactRoleType {
	@XmlEnumValue("BILL")
	BILL("BILL"),
	@XmlEnumValue("ECON")
	ECON("ECON"),
	@XmlEnumValue("NOK")
	NOK("NOK"),
	@XmlEnumValue("ORG")
	ORG("ORG"),
	@XmlEnumValue("PAYOR")
	PAYOR("PAYOR");
	
	private final String value;

    ContactRoleType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ContactRoleType fromValue(String v) {
        for (ContactRoleType c: ContactRoleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}