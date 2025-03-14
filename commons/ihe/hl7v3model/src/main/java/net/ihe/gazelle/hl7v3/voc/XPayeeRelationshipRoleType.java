/**
 * XPayeeRelationshipRoleType.java
 * <p>
 * File generated from the voc::XPayeeRelationshipRoleType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XPayeeRelationshipRoleType.
 *
 */

@XmlType(name = "XPayeeRelationshipRoleType")
@XmlEnum
@XmlRootElement(name = "XPayeeRelationshipRoleType")
public enum XPayeeRelationshipRoleType {
	@XmlEnumValue("FM")
	FM("FM"),
	@XmlEnumValue("GT")
	GT("GT"),
	@XmlEnumValue("PH")
	PH("PH"),
	@XmlEnumValue("PT")
	PT("PT");
	
	private final String value;

    XPayeeRelationshipRoleType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XPayeeRelationshipRoleType fromValue(String v) {
        for (XPayeeRelationshipRoleType c: XPayeeRelationshipRoleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}