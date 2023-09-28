/**
 * ActTaskPatientInformationReviewCode.java
 *
 * File generated from the voc::ActTaskPatientInformationReviewCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActTaskPatientInformationReviewCode.
 *
 */

@XmlType(name = "ActTaskPatientInformationReviewCode")
@XmlEnum
@XmlRootElement(name = "ActTaskPatientInformationReviewCode")
public enum ActTaskPatientInformationReviewCode {
	@XmlEnumValue("CLINNOTEREV")
	CLINNOTEREV("CLINNOTEREV"),
	@XmlEnumValue("DIAGLISTREV")
	DIAGLISTREV("DIAGLISTREV"),
	@XmlEnumValue("DISCHSUMREV")
	DISCHSUMREV("DISCHSUMREV"),
	@XmlEnumValue("FALLRISK")
	FALLRISK("FALLRISK"),
	@XmlEnumValue("LABRREV")
	LABRREV("LABRREV"),
	@XmlEnumValue("MARWLREV")
	MARWLREV("MARWLREV"),
	@XmlEnumValue("MICROORGRREV")
	MICROORGRREV("MICROORGRREV"),
	@XmlEnumValue("MICRORREV")
	MICRORREV("MICRORREV"),
	@XmlEnumValue("MICROSENSRREV")
	MICROSENSRREV("MICROSENSRREV"),
	@XmlEnumValue("MLREV")
	MLREV("MLREV"),
	@XmlEnumValue("OREV")
	OREV("OREV"),
	@XmlEnumValue("PATINFO")
	PATINFO("PATINFO"),
	@XmlEnumValue("PATREPREV")
	PATREPREV("PATREPREV"),
	@XmlEnumValue("PROBLISTREV")
	PROBLISTREV("PROBLISTREV"),
	@XmlEnumValue("RADREPREV")
	RADREPREV("RADREPREV"),
	@XmlEnumValue("RISKASSESS")
	RISKASSESS("RISKASSESS");
	
	private final String value;

    ActTaskPatientInformationReviewCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActTaskPatientInformationReviewCode fromValue(String v) {
        for (ActTaskPatientInformationReviewCode c: ActTaskPatientInformationReviewCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}