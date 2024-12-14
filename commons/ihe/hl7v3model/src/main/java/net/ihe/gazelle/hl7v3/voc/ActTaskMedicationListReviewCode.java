/**
 * ActTaskMedicationListReviewCode.java
 * <p>
 * File generated from the voc::ActTaskMedicationListReviewCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActTaskMedicationListReviewCode.
 *
 */

@XmlType(name = "ActTaskMedicationListReviewCode")
@XmlEnum
@XmlRootElement(name = "ActTaskMedicationListReviewCode")
public enum ActTaskMedicationListReviewCode {
	@XmlEnumValue("MARWLREV")
	MARWLREV("MARWLREV"),
	@XmlEnumValue("MLREV")
	MLREV("MLREV");
	
	private final String value;

    ActTaskMedicationListReviewCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActTaskMedicationListReviewCode fromValue(String v) {
        for (ActTaskMedicationListReviewCode c: ActTaskMedicationListReviewCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}