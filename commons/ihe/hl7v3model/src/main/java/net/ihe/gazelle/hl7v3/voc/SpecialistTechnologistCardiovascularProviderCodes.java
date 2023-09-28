/**
 * SpecialistTechnologistCardiovascularProviderCodes.java
 *
 * File generated from the voc::SpecialistTechnologistCardiovascularProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SpecialistTechnologistCardiovascularProviderCodes.
 *
 */

@XmlType(name = "SpecialistTechnologistCardiovascularProviderCodes")
@XmlEnum
@XmlRootElement(name = "SpecialistTechnologistCardiovascularProviderCodes")
public enum SpecialistTechnologistCardiovascularProviderCodes {
	@XmlEnumValue("246X00000X")
	_246X00000X("246X00000X"),
	@XmlEnumValue("246XC2901X")
	_246XC2901X("246XC2901X"),
	@XmlEnumValue("246XC2903X")
	_246XC2903X("246XC2903X"),
	@XmlEnumValue("246XS1301X")
	_246XS1301X("246XS1301X");
	
	private final String value;

    SpecialistTechnologistCardiovascularProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SpecialistTechnologistCardiovascularProviderCodes fromValue(String v) {
        for (SpecialistTechnologistCardiovascularProviderCodes c: SpecialistTechnologistCardiovascularProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}