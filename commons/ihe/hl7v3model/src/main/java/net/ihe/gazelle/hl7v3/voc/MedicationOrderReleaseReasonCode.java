/**
 * MedicationOrderReleaseReasonCode.java
 * <p>
 * File generated from the voc::MedicationOrderReleaseReasonCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration MedicationOrderReleaseReasonCode.
 *
 */

@XmlType(name = "MedicationOrderReleaseReasonCode")
@XmlEnum
@XmlRootElement(name = "MedicationOrderReleaseReasonCode")
public enum MedicationOrderReleaseReasonCode {
	@XmlEnumValue("HOLDDONE")
	HOLDDONE("HOLDDONE"),
	@XmlEnumValue("HOLDINAP")
	HOLDINAP("HOLDINAP");
	
	private final String value;

    MedicationOrderReleaseReasonCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static MedicationOrderReleaseReasonCode fromValue(String v) {
        for (MedicationOrderReleaseReasonCode c: MedicationOrderReleaseReasonCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}