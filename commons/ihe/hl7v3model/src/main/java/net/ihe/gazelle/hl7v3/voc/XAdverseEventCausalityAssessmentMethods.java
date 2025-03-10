/**
 * XAdverseEventCausalityAssessmentMethods.java
 * <p>
 * File generated from the voc::XAdverseEventCausalityAssessmentMethods uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XAdverseEventCausalityAssessmentMethods.
 *
 */

@XmlType(name = "XAdverseEventCausalityAssessmentMethods")
@XmlEnum
@XmlRootElement(name = "XAdverseEventCausalityAssessmentMethods")
public enum XAdverseEventCausalityAssessmentMethods {
	@XmlEnumValue("ALGM")
	ALGM("ALGM"),
	@XmlEnumValue("BYCL")
	BYCL("BYCL"),
	@XmlEnumValue("GINT")
	GINT("GINT");
	
	private final String value;

    XAdverseEventCausalityAssessmentMethods(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XAdverseEventCausalityAssessmentMethods fromValue(String v) {
        for (XAdverseEventCausalityAssessmentMethods c: XAdverseEventCausalityAssessmentMethods.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}