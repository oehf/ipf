/**
 * ActPolicyType.java
 *
 * File generated from the voc::ActPolicyType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActPolicyType.
 *
 */

@XmlType(name = "ActPolicyType")
@XmlEnum
@XmlRootElement(name = "ActPolicyType")
public enum ActPolicyType {
	@XmlEnumValue("COVPOL")
	COVPOL("COVPOL");
	
	private final String value;

    ActPolicyType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActPolicyType fromValue(String v) {
        for (ActPolicyType c: ActPolicyType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}