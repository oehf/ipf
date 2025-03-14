/**
 * PublicHealthcareProgram.java
 * <p>
 * File generated from the voc::PublicHealthcareProgram uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PublicHealthcareProgram.
 *
 */

@XmlType(name = "PublicHealthcareProgram")
@XmlEnum
@XmlRootElement(name = "PublicHealthcareProgram")
public enum PublicHealthcareProgram {
	@XmlEnumValue("CANPRG")
	CANPRG("CANPRG"),
	@XmlEnumValue("DENTPRG")
	DENTPRG("DENTPRG"),
	@XmlEnumValue("DISEASEPRG")
	DISEASEPRG("DISEASEPRG"),
	@XmlEnumValue("ENDRENAL")
	ENDRENAL("ENDRENAL"),
	@XmlEnumValue("HIVAIDS")
	HIVAIDS("HIVAIDS"),
	@XmlEnumValue("MANDPOL")
	MANDPOL("MANDPOL"),
	@XmlEnumValue("MENTPRG")
	MENTPRG("MENTPRG"),
	@XmlEnumValue("PUBLICPOL")
	PUBLICPOL("PUBLICPOL"),
	@XmlEnumValue("SAFNET")
	SAFNET("SAFNET"),
	@XmlEnumValue("SUBPRG")
	SUBPRG("SUBPRG"),
	@XmlEnumValue("SUBSIDIZ")
	SUBSIDIZ("SUBSIDIZ"),
	@XmlEnumValue("SUBSIDMC")
	SUBSIDMC("SUBSIDMC"),
	@XmlEnumValue("SUBSUPP")
	SUBSUPP("SUBSUPP");
	
	private final String value;

    PublicHealthcareProgram(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PublicHealthcareProgram fromValue(String v) {
        for (PublicHealthcareProgram c: PublicHealthcareProgram.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}