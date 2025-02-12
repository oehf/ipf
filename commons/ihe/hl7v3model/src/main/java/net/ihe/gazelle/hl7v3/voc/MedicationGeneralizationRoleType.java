/**
 * MedicationGeneralizationRoleType.java
 * <p>
 * File generated from the voc::MedicationGeneralizationRoleType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration MedicationGeneralizationRoleType.
 *
 */

@XmlType(name = "MedicationGeneralizationRoleType")
@XmlEnum
@XmlRootElement(name = "MedicationGeneralizationRoleType")
public enum MedicationGeneralizationRoleType {
	@XmlEnumValue("GD")
	GD("GD"),
	@XmlEnumValue("GDF")
	GDF("GDF"),
	@XmlEnumValue("GDS")
	GDS("GDS"),
	@XmlEnumValue("GDSF")
	GDSF("GDSF"),
	@XmlEnumValue("MGDSF")
	MGDSF("MGDSF");
	
	private final String value;

    MedicationGeneralizationRoleType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static MedicationGeneralizationRoleType fromValue(String v) {
        for (MedicationGeneralizationRoleType c: MedicationGeneralizationRoleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}