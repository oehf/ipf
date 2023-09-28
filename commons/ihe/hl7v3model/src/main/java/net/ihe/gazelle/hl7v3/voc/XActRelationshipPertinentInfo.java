/**
 * XActRelationshipPertinentInfo.java
 *
 * File generated from the voc::XActRelationshipPertinentInfo uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XActRelationshipPertinentInfo.
 *
 */

@XmlType(name = "XActRelationshipPertinentInfo")
@XmlEnum
@XmlRootElement(name = "XActRelationshipPertinentInfo")
public enum XActRelationshipPertinentInfo {
	@XmlEnumValue("CAUS")
	CAUS("CAUS"),
	@XmlEnumValue("MFST")
	MFST("MFST"),
	@XmlEnumValue("REFR")
	REFR("REFR"),
	@XmlEnumValue("SPRT")
	SPRT("SPRT"),
	@XmlEnumValue("SUBJ")
	SUBJ("SUBJ");
	
	private final String value;

    XActRelationshipPertinentInfo(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XActRelationshipPertinentInfo fromValue(String v) {
        for (XActRelationshipPertinentInfo c: XActRelationshipPertinentInfo.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}