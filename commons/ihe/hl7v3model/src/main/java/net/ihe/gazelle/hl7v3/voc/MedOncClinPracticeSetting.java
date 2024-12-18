/**
 * MedOncClinPracticeSetting.java
 * <p>
 * File generated from the voc::MedOncClinPracticeSetting uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration MedOncClinPracticeSetting.
 *
 */

@XmlType(name = "MedOncClinPracticeSetting")
@XmlEnum
@XmlRootElement(name = "MedOncClinPracticeSetting")
public enum MedOncClinPracticeSetting {
	@XmlEnumValue("ONCL")
	ONCL("ONCL"),
	@XmlEnumValue("PEDHO")
	PEDHO("PEDHO");
	
	private final String value;

    MedOncClinPracticeSetting(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static MedOncClinPracticeSetting fromValue(String v) {
        for (MedOncClinPracticeSetting c: MedOncClinPracticeSetting.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}