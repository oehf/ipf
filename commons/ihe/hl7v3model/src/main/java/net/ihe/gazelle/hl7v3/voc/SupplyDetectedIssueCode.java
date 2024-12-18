/**
 * SupplyDetectedIssueCode.java
 * <p>
 * File generated from the voc::SupplyDetectedIssueCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SupplyDetectedIssueCode.
 *
 */

@XmlType(name = "SupplyDetectedIssueCode")
@XmlEnum
@XmlRootElement(name = "SupplyDetectedIssueCode")
public enum SupplyDetectedIssueCode {
	@XmlEnumValue("TOOLATE")
	TOOLATE("TOOLATE"),
	@XmlEnumValue("TOOSOON")
	TOOSOON("TOOSOON");
	
	private final String value;

    SupplyDetectedIssueCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SupplyDetectedIssueCode fromValue(String v) {
        for (SupplyDetectedIssueCode c: SupplyDetectedIssueCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}