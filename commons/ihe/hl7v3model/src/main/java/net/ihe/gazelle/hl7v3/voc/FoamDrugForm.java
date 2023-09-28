/**
 * FoamDrugForm.java
 *
 * File generated from the voc::FoamDrugForm uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration FoamDrugForm.
 *
 */

@XmlType(name = "FoamDrugForm")
@XmlEnum
@XmlRootElement(name = "FoamDrugForm")
public enum FoamDrugForm {
	@XmlEnumValue("FOAM")
	FOAM("FOAM"),
	@XmlEnumValue("FOAMAPL")
	FOAMAPL("FOAMAPL"),
	@XmlEnumValue("RECFORM")
	RECFORM("RECFORM"),
	@XmlEnumValue("VAGFOAM")
	VAGFOAM("VAGFOAM"),
	@XmlEnumValue("VAGFOAMAPL")
	VAGFOAMAPL("VAGFOAMAPL");
	
	private final String value;

    FoamDrugForm(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static FoamDrugForm fromValue(String v) {
        for (FoamDrugForm c: FoamDrugForm.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}