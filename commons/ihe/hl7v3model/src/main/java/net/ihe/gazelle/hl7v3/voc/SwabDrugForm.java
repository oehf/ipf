/**
 * SwabDrugForm.java
 * <p>
 * File generated from the voc::SwabDrugForm uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SwabDrugForm.
 *
 */

@XmlType(name = "SwabDrugForm")
@XmlEnum
@XmlRootElement(name = "SwabDrugForm")
public enum SwabDrugForm {
	@XmlEnumValue("MEDSWAB")
	MEDSWAB("MEDSWAB"),
	@XmlEnumValue("SWAB")
	SWAB("SWAB");
	
	private final String value;

    SwabDrugForm(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SwabDrugForm fromValue(String v) {
        for (SwabDrugForm c: SwabDrugForm.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}