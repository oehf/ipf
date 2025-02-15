/**
 * InteractionDetectedIssueCode.java
 * <p>
 * File generated from the voc::InteractionDetectedIssueCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration InteractionDetectedIssueCode.
 *
 */

@XmlType(name = "InteractionDetectedIssueCode")
@XmlEnum
@XmlRootElement(name = "InteractionDetectedIssueCode")
public enum InteractionDetectedIssueCode {
	@XmlEnumValue("DRG")
	DRG("DRG"),
	@XmlEnumValue("FOOD")
	FOOD("FOOD"),
	@XmlEnumValue("NHP")
	NHP("NHP"),
	@XmlEnumValue("NONRX")
	NONRX("NONRX"),
	@XmlEnumValue("TPROD")
	TPROD("TPROD");
	
	private final String value;

    InteractionDetectedIssueCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static InteractionDetectedIssueCode fromValue(String v) {
        for (InteractionDetectedIssueCode c: InteractionDetectedIssueCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}