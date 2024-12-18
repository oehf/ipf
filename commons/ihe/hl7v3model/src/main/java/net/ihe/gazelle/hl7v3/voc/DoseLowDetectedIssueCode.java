/**
 * DoseLowDetectedIssueCode.java
 * <p>
 * File generated from the voc::DoseLowDetectedIssueCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DoseLowDetectedIssueCode.
 *
 */

@XmlType(name = "DoseLowDetectedIssueCode")
@XmlEnum
@XmlRootElement(name = "DoseLowDetectedIssueCode")
public enum DoseLowDetectedIssueCode {
	@XmlEnumValue("DOSEL")
	DOSEL("DOSEL"),
	@XmlEnumValue("DOSELIND")
	DOSELIND("DOSELIND"),
	@XmlEnumValue("DOSELINDA")
	DOSELINDA("DOSELINDA"),
	@XmlEnumValue("DOSELINDSA")
	DOSELINDSA("DOSELINDSA"),
	@XmlEnumValue("DOSELINDW")
	DOSELINDW("DOSELINDW");
	
	private final String value;

    DoseLowDetectedIssueCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DoseLowDetectedIssueCode fromValue(String v) {
        for (DoseLowDetectedIssueCode c: DoseLowDetectedIssueCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}