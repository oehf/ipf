/**
 * XActRelationshipExternalReference.java
 *
 * File generated from the voc::XActRelationshipExternalReference uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XActRelationshipExternalReference.
 *
 */

@XmlType(name = "XActRelationshipExternalReference")
@XmlEnum
@XmlRootElement(name = "XActRelationshipExternalReference")
public enum XActRelationshipExternalReference {
	@XmlEnumValue("ELNK")
	ELNK("ELNK"),
	@XmlEnumValue("REFR")
	REFR("REFR"),
	@XmlEnumValue("RPLC")
	RPLC("RPLC"),
	@XmlEnumValue("SPRT")
	SPRT("SPRT"),
	@XmlEnumValue("SUBJ")
	SUBJ("SUBJ"),
	@XmlEnumValue("XCRPT")
	XCRPT("XCRPT");
	
	private final String value;

    XActRelationshipExternalReference(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XActRelationshipExternalReference fromValue(String v) {
        for (XActRelationshipExternalReference c: XActRelationshipExternalReference.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}