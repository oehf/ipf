/**
 * ResidentialTreatmentFacilitiesProviderCodes.java
 *
 * File generated from the voc::ResidentialTreatmentFacilitiesProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ResidentialTreatmentFacilitiesProviderCodes.
 *
 */

@XmlType(name = "ResidentialTreatmentFacilitiesProviderCodes")
@XmlEnum
@XmlRootElement(name = "ResidentialTreatmentFacilitiesProviderCodes")
public enum ResidentialTreatmentFacilitiesProviderCodes {
	@XmlEnumValue("320000000X")
	_320000000X("320000000X"),
	@XmlEnumValue("320600000X")
	_320600000X("320600000X"),
	@XmlEnumValue("320700000X")
	_320700000X("320700000X"),
	@XmlEnumValue("320800000X")
	_320800000X("320800000X"),
	@XmlEnumValue("320900000X")
	_320900000X("320900000X"),
	@XmlEnumValue("322D00000X")
	_322D00000X("322D00000X"),
	@XmlEnumValue("323P00000X")
	_323P00000X("323P00000X"),
	@XmlEnumValue("324500000X")
	_324500000X("324500000X"),
	@XmlEnumValue("3245S0500X")
	_3245S0500X("3245S0500X");
	
	private final String value;

    ResidentialTreatmentFacilitiesProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ResidentialTreatmentFacilitiesProviderCodes fromValue(String v) {
        for (ResidentialTreatmentFacilitiesProviderCodes c: ResidentialTreatmentFacilitiesProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}