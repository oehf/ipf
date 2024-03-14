/**
 * PatientProfileQueryReasonCode.java
 *
 * File generated from the voc::PatientProfileQueryReasonCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PatientProfileQueryReasonCode.
 *
 */

@XmlType(name = "PatientProfileQueryReasonCode")
@XmlEnum
@XmlRootElement(name = "PatientProfileQueryReasonCode")
public enum PatientProfileQueryReasonCode {
	@XmlEnumValue("ADMREV")
	ADMREV("ADMREV"),
	@XmlEnumValue("LEGAL")
	LEGAL("LEGAL"),
	@XmlEnumValue("PATCAR")
	PATCAR("PATCAR"),
	@XmlEnumValue("PATREQ")
	PATREQ("PATREQ"),
	@XmlEnumValue("PRCREV")
	PRCREV("PRCREV"),
	@XmlEnumValue("REGUL")
	REGUL("REGUL"),
	@XmlEnumValue("RSRCH")
	RSRCH("RSRCH"),
	@XmlEnumValue("VALIDATION")
	VALIDATION("VALIDATION");
	
	private final String value;

    PatientProfileQueryReasonCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PatientProfileQueryReasonCode fromValue(String v) {
        for (PatientProfileQueryReasonCode c: PatientProfileQueryReasonCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}