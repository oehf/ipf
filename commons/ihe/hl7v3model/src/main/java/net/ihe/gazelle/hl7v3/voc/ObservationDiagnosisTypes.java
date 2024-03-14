/**
 * ObservationDiagnosisTypes.java
 *
 * File generated from the voc::ObservationDiagnosisTypes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ObservationDiagnosisTypes.
 *
 */

@XmlType(name = "ObservationDiagnosisTypes")
@XmlEnum
@XmlRootElement(name = "ObservationDiagnosisTypes")
public enum ObservationDiagnosisTypes {
	@XmlEnumValue("ADMDX")
	ADMDX("ADMDX"),
	@XmlEnumValue("DISDX")
	DISDX("DISDX"),
	@XmlEnumValue("DX")
	DX("DX"),
	@XmlEnumValue("INTDX")
	INTDX("INTDX"),
	@XmlEnumValue("NOI")
	NOI("NOI");
	
	private final String value;

    ObservationDiagnosisTypes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ObservationDiagnosisTypes fromValue(String v) {
        for (ObservationDiagnosisTypes c: ObservationDiagnosisTypes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}