/**
 * ActTaskClinicalNoteEntryCode.java
 * <p>
 * File generated from the voc::ActTaskClinicalNoteEntryCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActTaskClinicalNoteEntryCode.
 *
 */

@XmlType(name = "ActTaskClinicalNoteEntryCode")
@XmlEnum
@XmlRootElement(name = "ActTaskClinicalNoteEntryCode")
public enum ActTaskClinicalNoteEntryCode {
	@XmlEnumValue("CLINNOTEE")
	CLINNOTEE("CLINNOTEE"),
	@XmlEnumValue("DIAGLISTE")
	DIAGLISTE("DIAGLISTE"),
	@XmlEnumValue("DISCHSUME")
	DISCHSUME("DISCHSUME"),
	@XmlEnumValue("PATREPE")
	PATREPE("PATREPE"),
	@XmlEnumValue("PROBLISTE")
	PROBLISTE("PROBLISTE"),
	@XmlEnumValue("RADREPE")
	RADREPE("RADREPE");
	
	private final String value;

    ActTaskClinicalNoteEntryCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActTaskClinicalNoteEntryCode fromValue(String v) {
        for (ActTaskClinicalNoteEntryCode c: ActTaskClinicalNoteEntryCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}