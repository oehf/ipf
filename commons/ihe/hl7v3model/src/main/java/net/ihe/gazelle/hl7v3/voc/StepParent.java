/**
 * StepParent.java
 * <p>
 * File generated from the voc::StepParent uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration StepParent.
 *
 */

@XmlType(name = "StepParent")
@XmlEnum
@XmlRootElement(name = "StepParent")
public enum StepParent {
	@XmlEnumValue("STPFTH")
	STPFTH("STPFTH"),
	@XmlEnumValue("STPMTH")
	STPMTH("STPMTH"),
	@XmlEnumValue("STPPRN")
	STPPRN("STPPRN");
	
	private final String value;

    StepParent(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static StepParent fromValue(String v) {
        for (StepParent c: StepParent.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}