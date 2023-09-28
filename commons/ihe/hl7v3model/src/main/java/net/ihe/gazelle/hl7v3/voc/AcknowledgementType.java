/**
 * AcknowledgementType.java
 *
 * File generated from the voc::AcknowledgementType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration AcknowledgementType.
 *
 */

@XmlType(name = "AcknowledgementType")
@XmlEnum
@XmlRootElement(name = "AcknowledgementType")
public enum AcknowledgementType {
	@XmlEnumValue("AA")
	AA("AA"),
	@XmlEnumValue("AE")
	AE("AE"),
	@XmlEnumValue("AR")
	AR("AR"),
	@XmlEnumValue("CA")
	CA("CA"),
	@XmlEnumValue("CE")
	CE("CE"),
	@XmlEnumValue("CR")
	CR("CR");
	
	private final String value;

    AcknowledgementType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static AcknowledgementType fromValue(String v) {
        for (AcknowledgementType c: AcknowledgementType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}