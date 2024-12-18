/**
 * ClinicalResearchObservationReason.java
 * <p>
 * File generated from the voc::ClinicalResearchObservationReason uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ClinicalResearchObservationReason.
 *
 */

@XmlType(name = "ClinicalResearchObservationReason")
@XmlEnum
@XmlRootElement(name = "ClinicalResearchObservationReason")
public enum ClinicalResearchObservationReason {
	@XmlEnumValue("NPT")
	NPT("NPT"),
	@XmlEnumValue("PPT")
	PPT("PPT"),
	@XmlEnumValue("UPT")
	UPT("UPT");
	
	private final String value;

    ClinicalResearchObservationReason(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ClinicalResearchObservationReason fromValue(String v) {
        for (ClinicalResearchObservationReason c: ClinicalResearchObservationReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}