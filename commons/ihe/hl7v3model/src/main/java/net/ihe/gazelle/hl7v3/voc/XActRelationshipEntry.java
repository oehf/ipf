/**
 * XActRelationshipEntry.java
 *
 * File generated from the voc::XActRelationshipEntry uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XActRelationshipEntry.
 *
 */

@XmlType(name = "XActRelationshipEntry")
@XmlEnum
@XmlRootElement(name = "XActRelationshipEntry")
public enum XActRelationshipEntry {
	@XmlEnumValue("COMP")
	COMP("COMP"),
	@XmlEnumValue("DRIV")
	DRIV("DRIV");
	
	private final String value;

    XActRelationshipEntry(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XActRelationshipEntry fromValue(String v) {
        for (XActRelationshipEntry c: XActRelationshipEntry.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}