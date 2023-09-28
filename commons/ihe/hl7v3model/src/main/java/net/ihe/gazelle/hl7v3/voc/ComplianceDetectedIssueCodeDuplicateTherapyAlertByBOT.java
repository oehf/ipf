/**
 * ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT.java
 *
 * File generated from the voc::ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT.
 *
 */

@XmlType(name = "ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT")
@XmlEnum
@XmlRootElement(name = "ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT")
public enum ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT {
	@XmlEnumValue("DUPTHPCLS")
	DUPTHPCLS("DUPTHPCLS"),
	@XmlEnumValue("DUPTHPGEN")
	DUPTHPGEN("DUPTHPGEN"),
	@XmlEnumValue("DUPTHPY")
	DUPTHPY("DUPTHPY");
	
	private final String value;

    ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT fromValue(String v) {
        for (ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT c: ComplianceDetectedIssueCodeDuplicateTherapyAlertByBOT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}