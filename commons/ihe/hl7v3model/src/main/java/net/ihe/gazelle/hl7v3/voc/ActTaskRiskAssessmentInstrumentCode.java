/**
 * ActTaskRiskAssessmentInstrumentCode.java
 *
 * File generated from the voc::ActTaskRiskAssessmentInstrumentCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActTaskRiskAssessmentInstrumentCode.
 *
 */

@XmlType(name = "ActTaskRiskAssessmentInstrumentCode")
@XmlEnum
@XmlRootElement(name = "ActTaskRiskAssessmentInstrumentCode")
public enum ActTaskRiskAssessmentInstrumentCode {
	@XmlEnumValue("FALLRISK")
	FALLRISK("FALLRISK"),
	@XmlEnumValue("RISKASSESS")
	RISKASSESS("RISKASSESS");
	
	private final String value;

    ActTaskRiskAssessmentInstrumentCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActTaskRiskAssessmentInstrumentCode fromValue(String v) {
        for (ActTaskRiskAssessmentInstrumentCode c: ActTaskRiskAssessmentInstrumentCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}