/**
 * AdjudicatedWithAdjustments.java
 *
 * File generated from the voc::AdjudicatedWithAdjustments uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration AdjudicatedWithAdjustments.
 *
 */

@XmlType(name = "AdjudicatedWithAdjustments")
@XmlEnum
@XmlRootElement(name = "AdjudicatedWithAdjustments")
public enum AdjudicatedWithAdjustments {
	@XmlEnumValue("AA")
	AA("AA"),
	@XmlEnumValue("ANF")
	ANF("ANF");
	
	private final String value;

    AdjudicatedWithAdjustments(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static AdjudicatedWithAdjustments fromValue(String v) {
        for (AdjudicatedWithAdjustments c: AdjudicatedWithAdjustments.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}