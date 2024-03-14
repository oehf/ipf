/**
 * ParameterizedDataTypeBag.java
 *
 * File generated from the voc::ParameterizedDataTypeBag uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ParameterizedDataTypeBag.
 *
 */

@XmlType(name = "ParameterizedDataTypeBag")
@XmlEnum
@XmlRootElement(name = "ParameterizedDataTypeBag")
public enum ParameterizedDataTypeBag {
	@XmlEnumValue("BAG<T>")
	BAGT("BAG<T>");
	
	private final String value;

    ParameterizedDataTypeBag(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ParameterizedDataTypeBag fromValue(String v) {
        for (ParameterizedDataTypeBag c: ParameterizedDataTypeBag.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}