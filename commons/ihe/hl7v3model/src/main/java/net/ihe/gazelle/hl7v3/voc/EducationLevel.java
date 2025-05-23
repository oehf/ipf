/**
 * EducationLevel.java
 * <p>
 * File generated from the voc::EducationLevel uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration EducationLevel.
 *
 */

@XmlType(name = "EducationLevel")
@XmlEnum
@XmlRootElement(name = "EducationLevel")
public enum EducationLevel {
	@XmlEnumValue("ASSOC")
	ASSOC("ASSOC"),
	@XmlEnumValue("BD")
	BD("BD"),
	@XmlEnumValue("ELEM")
	ELEM("ELEM"),
	@XmlEnumValue("GD")
	GD("GD"),
	@XmlEnumValue("HS")
	HS("HS"),
	@XmlEnumValue("PB")
	PB("PB"),
	@XmlEnumValue("POSTG")
	POSTG("POSTG"),
	@XmlEnumValue("SCOL")
	SCOL("SCOL"),
	@XmlEnumValue("SEC")
	SEC("SEC");
	
	private final String value;

    EducationLevel(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static EducationLevel fromValue(String v) {
        for (EducationLevel c: EducationLevel.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}