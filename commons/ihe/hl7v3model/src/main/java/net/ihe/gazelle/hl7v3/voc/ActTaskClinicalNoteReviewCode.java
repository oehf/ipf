/**
 * ActTaskClinicalNoteReviewCode.java
 * <p>
 * File generated from the voc::ActTaskClinicalNoteReviewCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActTaskClinicalNoteReviewCode.
 *
 */

@XmlType(name = "ActTaskClinicalNoteReviewCode")
@XmlEnum
@XmlRootElement(name = "ActTaskClinicalNoteReviewCode")
public enum ActTaskClinicalNoteReviewCode {
	@XmlEnumValue("CLINNOTEREV")
	CLINNOTEREV("CLINNOTEREV"),
	@XmlEnumValue("DISCHSUMREV")
	DISCHSUMREV("DISCHSUMREV");
	
	private final String value;

    ActTaskClinicalNoteReviewCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActTaskClinicalNoteReviewCode fromValue(String v) {
        for (ActTaskClinicalNoteReviewCode c: ActTaskClinicalNoteReviewCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}