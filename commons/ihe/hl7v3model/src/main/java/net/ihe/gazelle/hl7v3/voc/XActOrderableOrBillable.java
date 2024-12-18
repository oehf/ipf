/**
 * XActOrderableOrBillable.java
 * <p>
 * File generated from the voc::XActOrderableOrBillable uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XActOrderableOrBillable.
 *
 */

@XmlType(name = "XActOrderableOrBillable")
@XmlEnum
@XmlRootElement(name = "XActOrderableOrBillable")
public enum XActOrderableOrBillable {
	@XmlEnumValue("ACCM")
	ACCM("ACCM"),
	@XmlEnumValue("ENC")
	ENC("ENC"),
	@XmlEnumValue("PCPR")
	PCPR("PCPR"),
	@XmlEnumValue("SBADM")
	SBADM("SBADM"),
	@XmlEnumValue("TRNS")
	TRNS("TRNS");
	
	private final String value;

    XActOrderableOrBillable(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XActOrderableOrBillable fromValue(String v) {
        for (XActOrderableOrBillable c: XActOrderableOrBillable.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}