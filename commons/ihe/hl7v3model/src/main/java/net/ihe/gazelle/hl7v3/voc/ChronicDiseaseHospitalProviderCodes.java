/**
 * ChronicDiseaseHospitalProviderCodes.java
 * <p>
 * File generated from the voc::ChronicDiseaseHospitalProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ChronicDiseaseHospitalProviderCodes.
 *
 */

@XmlType(name = "ChronicDiseaseHospitalProviderCodes")
@XmlEnum
@XmlRootElement(name = "ChronicDiseaseHospitalProviderCodes")
public enum ChronicDiseaseHospitalProviderCodes {
	@XmlEnumValue("281P00000X")
	_281P00000X("281P00000X"),
	@XmlEnumValue("281PC2000X")
	_281PC2000X("281PC2000X");
	
	private final String value;

    ChronicDiseaseHospitalProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ChronicDiseaseHospitalProviderCodes fromValue(String v) {
        for (ChronicDiseaseHospitalProviderCodes c: ChronicDiseaseHospitalProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}