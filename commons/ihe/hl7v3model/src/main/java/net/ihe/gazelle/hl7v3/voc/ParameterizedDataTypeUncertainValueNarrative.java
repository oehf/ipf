/**
 * ParameterizedDataTypeUncertainValueNarrative.java
 * <p>
 * File generated from the voc::ParameterizedDataTypeUncertainValueNarrative uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ParameterizedDataTypeUncertainValueNarrative.
 *
 */

@XmlType(name = "ParameterizedDataTypeUncertainValueNarrative")
@XmlEnum
@XmlRootElement(name = "ParameterizedDataTypeUncertainValueNarrative")
public enum ParameterizedDataTypeUncertainValueNarrative {
	@XmlEnumValue("UVN<T>")
	UVNT("UVN<T>");
	
	private final String value;

    ParameterizedDataTypeUncertainValueNarrative(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ParameterizedDataTypeUncertainValueNarrative fromValue(String v) {
        for (ParameterizedDataTypeUncertainValueNarrative c: ParameterizedDataTypeUncertainValueNarrative.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}