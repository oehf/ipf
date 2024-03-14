/**
 * AutomobileInsurancePolicy.java
 *
 * File generated from the voc::AutomobileInsurancePolicy uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration AutomobileInsurancePolicy.
 *
 */

@XmlType(name = "AutomobileInsurancePolicy")
@XmlEnum
@XmlRootElement(name = "AutomobileInsurancePolicy")
public enum AutomobileInsurancePolicy {
	@XmlEnumValue("AUTOPOL")
	AUTOPOL("AUTOPOL"),
	@XmlEnumValue("COL")
	COL("COL"),
	@XmlEnumValue("UNINSMOT")
	UNINSMOT("UNINSMOT");
	
	private final String value;

    AutomobileInsurancePolicy(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static AutomobileInsurancePolicy fromValue(String v) {
        for (AutomobileInsurancePolicy c: AutomobileInsurancePolicy.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}