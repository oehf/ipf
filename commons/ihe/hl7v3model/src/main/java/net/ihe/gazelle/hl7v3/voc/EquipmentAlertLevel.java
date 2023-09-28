/**
 * EquipmentAlertLevel.java
 *
 * File generated from the voc::EquipmentAlertLevel uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration EquipmentAlertLevel.
 *
 */

@XmlType(name = "EquipmentAlertLevel")
@XmlEnum
@XmlRootElement(name = "EquipmentAlertLevel")
public enum EquipmentAlertLevel {
	@XmlEnumValue("C")
	C("C"),
	@XmlEnumValue("N")
	N("N"),
	@XmlEnumValue("S")
	S("S"),
	@XmlEnumValue("W")
	W("W");
	
	private final String value;

    EquipmentAlertLevel(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static EquipmentAlertLevel fromValue(String v) {
        for (EquipmentAlertLevel c: EquipmentAlertLevel.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}