/**
 * ActInpatientEncounterCode.java
 * <p>
 * File generated from the voc::ActInpatientEncounterCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActInpatientEncounterCode.
 *
 */

@XmlType(name = "ActInpatientEncounterCode")
@XmlEnum
@XmlRootElement(name = "ActInpatientEncounterCode")
public enum ActInpatientEncounterCode {
	@XmlEnumValue("ACUTE")
	ACUTE("ACUTE"),
	@XmlEnumValue("IMP")
	IMP("IMP"),
	@XmlEnumValue("NONAC")
	NONAC("NONAC");
	
	private final String value;

    ActInpatientEncounterCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActInpatientEncounterCode fromValue(String v) {
        for (ActInpatientEncounterCode c: ActInpatientEncounterCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}