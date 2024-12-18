/**
 * EncounterAdmissionSource.java
 * <p>
 * File generated from the voc::EncounterAdmissionSource uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration EncounterAdmissionSource.
 *
 */

@XmlType(name = "EncounterAdmissionSource")
@XmlEnum
@XmlRootElement(name = "EncounterAdmissionSource")
public enum EncounterAdmissionSource {
	@XmlEnumValue("E")
	E("E"),
	@XmlEnumValue("LD")
	LD("LD"),
	@XmlEnumValue("NB")
	NB("NB");
	
	private final String value;

    EncounterAdmissionSource(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static EncounterAdmissionSource fromValue(String v) {
        for (EncounterAdmissionSource c: EncounterAdmissionSource.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}