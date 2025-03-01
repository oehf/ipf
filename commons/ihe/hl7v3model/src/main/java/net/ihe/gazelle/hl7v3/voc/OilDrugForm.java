/**
 * OilDrugForm.java
 * <p>
 * File generated from the voc::OilDrugForm uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration OilDrugForm.
 *
 */

@XmlType(name = "OilDrugForm")
@XmlEnum
@XmlRootElement(name = "OilDrugForm")
public enum OilDrugForm {
	@XmlEnumValue("OIL")
	OIL("OIL"),
	@XmlEnumValue("TOPOIL")
	TOPOIL("TOPOIL");
	
	private final String value;

    OilDrugForm(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static OilDrugForm fromValue(String v) {
        for (OilDrugForm c: OilDrugForm.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}