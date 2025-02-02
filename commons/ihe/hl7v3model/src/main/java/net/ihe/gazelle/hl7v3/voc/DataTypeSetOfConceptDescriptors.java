/**
 * DataTypeSetOfConceptDescriptors.java
 * <p>
 * File generated from the voc::DataTypeSetOfConceptDescriptors uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DataTypeSetOfConceptDescriptors.
 *
 */

@XmlType(name = "DataTypeSetOfConceptDescriptors")
@XmlEnum
@XmlRootElement(name = "DataTypeSetOfConceptDescriptors")
public enum DataTypeSetOfConceptDescriptors {
	@XmlEnumValue("NPPD<CD>")
	NPPDCD("NPPD<CD>"),
	@XmlEnumValue("SET<CD>")
	SETCD("SET<CD>"),
	@XmlEnumValue("SET<CE>")
	SETCE("SET<CE>"),
	@XmlEnumValue("SET<CS>")
	SETCS("SET<CS>"),
	@XmlEnumValue("SET<CV>")
	SETCV("SET<CV>"),
	@XmlEnumValue("SET<UVP<CD>>")
	SETUVPCD("SET<UVP<CD>>");
	
	private final String value;

    DataTypeSetOfConceptDescriptors(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DataTypeSetOfConceptDescriptors fromValue(String v) {
        for (DataTypeSetOfConceptDescriptors c: DataTypeSetOfConceptDescriptors.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}