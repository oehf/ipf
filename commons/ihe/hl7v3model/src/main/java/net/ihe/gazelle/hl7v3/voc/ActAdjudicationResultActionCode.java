/**
 * ActAdjudicationResultActionCode.java
 * <p>
 * File generated from the voc::ActAdjudicationResultActionCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActAdjudicationResultActionCode.
 *
 */

@XmlType(name = "ActAdjudicationResultActionCode")
@XmlEnum
@XmlRootElement(name = "ActAdjudicationResultActionCode")
public enum ActAdjudicationResultActionCode {
	@XmlEnumValue("DISPLAY")
	DISPLAY("DISPLAY"),
	@XmlEnumValue("FORM")
	FORM("FORM");
	
	private final String value;

    ActAdjudicationResultActionCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActAdjudicationResultActionCode fromValue(String v) {
        for (ActAdjudicationResultActionCode c: ActAdjudicationResultActionCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}