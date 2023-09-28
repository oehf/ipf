/**
 * ExpectedSubset.java
 *
 * File generated from the voc::ExpectedSubset uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ExpectedSubset.
 *
 */

@XmlType(name = "ExpectedSubset")
@XmlEnum
@XmlRootElement(name = "ExpectedSubset")
public enum ExpectedSubset {
	@XmlEnumValue("FUTSUM")
	FUTSUM("FUTSUM"),
	@XmlEnumValue("FUTURE")
	FUTURE("FUTURE"),
	@XmlEnumValue("LAST")
	LAST("LAST"),
	@XmlEnumValue("NEXT")
	NEXT("NEXT");
	
	private final String value;

    ExpectedSubset(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ExpectedSubset fromValue(String v) {
        for (ExpectedSubset c: ExpectedSubset.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}