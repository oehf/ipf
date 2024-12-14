/**
 * DiagTherPracticeSetting.java
 * <p>
 * File generated from the voc::DiagTherPracticeSetting uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DiagTherPracticeSetting.
 *
 */

@XmlType(name = "DiagTherPracticeSetting")
@XmlEnum
@XmlRootElement(name = "DiagTherPracticeSetting")
public enum DiagTherPracticeSetting {
	@XmlEnumValue("261QE0800N")
	_261QE0800N("261QE0800N"),
	@XmlEnumValue("261QX0203N")
	_261QX0203N("261QX0203N"),
	@XmlEnumValue("CATH")
	CATH("CATH"),
	@XmlEnumValue("CVDX")
	CVDX("CVDX"),
	@XmlEnumValue("DX")
	DX("DX"),
	@XmlEnumValue("ECHO")
	ECHO("ECHO"),
	@XmlEnumValue("ENDOS")
	ENDOS("ENDOS"),
	@XmlEnumValue("GIDX")
	GIDX("GIDX"),
	@XmlEnumValue("RADDX")
	RADDX("RADDX"),
	@XmlEnumValue("RADO")
	RADO("RADO"),
	@XmlEnumValue("RNEU")
	RNEU("RNEU");
	
	private final String value;

    DiagTherPracticeSetting(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DiagTherPracticeSetting fromValue(String v) {
        for (DiagTherPracticeSetting c: DiagTherPracticeSetting.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}