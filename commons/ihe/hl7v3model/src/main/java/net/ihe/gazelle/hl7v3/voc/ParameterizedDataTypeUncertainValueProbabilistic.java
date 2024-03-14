/**
 * ParameterizedDataTypeUncertainValueProbabilistic.java
 *
 * File generated from the voc::ParameterizedDataTypeUncertainValueProbabilistic uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ParameterizedDataTypeUncertainValueProbabilistic.
 *
 */

@XmlType(name = "ParameterizedDataTypeUncertainValueProbabilistic")
@XmlEnum
@XmlRootElement(name = "ParameterizedDataTypeUncertainValueProbabilistic")
public enum ParameterizedDataTypeUncertainValueProbabilistic {
	@XmlEnumValue("UVP<T>")
	UVPT("UVP<T>");
	
	private final String value;

    ParameterizedDataTypeUncertainValueProbabilistic(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ParameterizedDataTypeUncertainValueProbabilistic fromValue(String v) {
        for (ParameterizedDataTypeUncertainValueProbabilistic c: ParameterizedDataTypeUncertainValueProbabilistic.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}