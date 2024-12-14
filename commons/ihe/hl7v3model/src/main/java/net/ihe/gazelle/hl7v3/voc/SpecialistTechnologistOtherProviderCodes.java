/**
 * SpecialistTechnologistOtherProviderCodes.java
 * <p>
 * File generated from the voc::SpecialistTechnologistOtherProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SpecialistTechnologistOtherProviderCodes.
 *
 */

@XmlType(name = "SpecialistTechnologistOtherProviderCodes")
@XmlEnum
@XmlRootElement(name = "SpecialistTechnologistOtherProviderCodes")
public enum SpecialistTechnologistOtherProviderCodes {
	@XmlEnumValue("246Z00000X")
	_246Z00000X("246Z00000X"),
	@XmlEnumValue("246ZA2600X")
	_246ZA2600X("246ZA2600X"),
	@XmlEnumValue("246ZB0301X")
	_246ZB0301X("246ZB0301X"),
	@XmlEnumValue("246ZB0302X")
	_246ZB0302X("246ZB0302X"),
	@XmlEnumValue("246ZB0500X")
	_246ZB0500X("246ZB0500X"),
	@XmlEnumValue("246ZB0600X")
	_246ZB0600X("246ZB0600X"),
	@XmlEnumValue("246ZE0500X")
	_246ZE0500X("246ZE0500X"),
	@XmlEnumValue("246ZE0600X")
	_246ZE0600X("246ZE0600X"),
	@XmlEnumValue("246ZG0701X")
	_246ZG0701X("246ZG0701X"),
	@XmlEnumValue("246ZG1000X")
	_246ZG1000X("246ZG1000X"),
	@XmlEnumValue("246ZI1000X")
	_246ZI1000X("246ZI1000X"),
	@XmlEnumValue("246ZN0300X")
	_246ZN0300X("246ZN0300X"),
	@XmlEnumValue("246ZS0400X")
	_246ZS0400X("246ZS0400X");
	
	private final String value;

    SpecialistTechnologistOtherProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SpecialistTechnologistOtherProviderCodes fromValue(String v) {
        for (SpecialistTechnologistOtherProviderCodes c: SpecialistTechnologistOtherProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}