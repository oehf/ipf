/**
 * StepSibling.java
 * <p>
 * File generated from the voc::StepSibling uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration StepSibling.
 *
 */

@XmlType(name = "StepSibling")
@XmlEnum
@XmlRootElement(name = "StepSibling")
public enum StepSibling {
	@XmlEnumValue("STPBRO")
	STPBRO("STPBRO"),
	@XmlEnumValue("STPSIB")
	STPSIB("STPSIB"),
	@XmlEnumValue("STPSIS")
	STPSIS("STPSIS");
	
	private final String value;

    StepSibling(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static StepSibling fromValue(String v) {
        for (StepSibling c: StepSibling.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}