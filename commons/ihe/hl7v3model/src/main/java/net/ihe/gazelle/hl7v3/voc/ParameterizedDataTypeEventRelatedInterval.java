/**
 * ParameterizedDataTypeEventRelatedInterval.java
 *
 * File generated from the voc::ParameterizedDataTypeEventRelatedInterval uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ParameterizedDataTypeEventRelatedInterval.
 *
 */

@XmlType(name = "ParameterizedDataTypeEventRelatedInterval")
@XmlEnum
@XmlRootElement(name = "ParameterizedDataTypeEventRelatedInterval")
public enum ParameterizedDataTypeEventRelatedInterval {
	@XmlEnumValue("EIVL<T>")
	EIVLT("EIVL<T>");
	
	private final String value;

    ParameterizedDataTypeEventRelatedInterval(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ParameterizedDataTypeEventRelatedInterval fromValue(String v) {
        for (ParameterizedDataTypeEventRelatedInterval c: ParameterizedDataTypeEventRelatedInterval.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}