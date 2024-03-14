/**
 * GasDrugForm.java
 *
 * File generated from the voc::GasDrugForm uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration GasDrugForm.
 *
 */

@XmlType(name = "GasDrugForm")
@XmlEnum
@XmlRootElement(name = "GasDrugForm")
public enum GasDrugForm {
	@XmlEnumValue("GASINHL")
	GASINHL("GASINHL");
	
	private final String value;

    GasDrugForm(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static GasDrugForm fromValue(String v) {
        for (GasDrugForm c: GasDrugForm.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}