/**
 * InternalMedicineProviderCodes.java
 *
 * File generated from the voc::InternalMedicineProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration InternalMedicineProviderCodes.
 *
 */

@XmlType(name = "InternalMedicineProviderCodes")
@XmlEnum
@XmlRootElement(name = "InternalMedicineProviderCodes")
public enum InternalMedicineProviderCodes {
	@XmlEnumValue("207R00000X")
	_207R00000X("207R00000X"),
	@XmlEnumValue("207RA0000X")
	_207RA0000X("207RA0000X"),
	@XmlEnumValue("207RA0201X")
	_207RA0201X("207RA0201X"),
	@XmlEnumValue("207RA0401X")
	_207RA0401X("207RA0401X"),
	@XmlEnumValue("207RC0000X")
	_207RC0000X("207RC0000X"),
	@XmlEnumValue("207RC0001X")
	_207RC0001X("207RC0001X"),
	@XmlEnumValue("207RC0200X")
	_207RC0200X("207RC0200X"),
	@XmlEnumValue("207RE0101X")
	_207RE0101X("207RE0101X"),
	@XmlEnumValue("207RG0100X")
	_207RG0100X("207RG0100X"),
	@XmlEnumValue("207RG0300X")
	_207RG0300X("207RG0300X"),
	@XmlEnumValue("207RH0000X")
	_207RH0000X("207RH0000X"),
	@XmlEnumValue("207RH0003X")
	_207RH0003X("207RH0003X"),
	@XmlEnumValue("207RI0001X")
	_207RI0001X("207RI0001X"),
	@XmlEnumValue("207RI0008X")
	_207RI0008X("207RI0008X"),
	@XmlEnumValue("207RI0011X")
	_207RI0011X("207RI0011X"),
	@XmlEnumValue("207RI0200X")
	_207RI0200X("207RI0200X"),
	@XmlEnumValue("207RM1200X")
	_207RM1200X("207RM1200X"),
	@XmlEnumValue("207RN0300X")
	_207RN0300X("207RN0300X"),
	@XmlEnumValue("207RP1001X")
	_207RP1001X("207RP1001X"),
	@XmlEnumValue("207RR0500X")
	_207RR0500X("207RR0500X"),
	@XmlEnumValue("207RS0010X")
	_207RS0010X("207RS0010X"),
	@XmlEnumValue("207RX0202X")
	_207RX0202X("207RX0202X");
	
	private final String value;

    InternalMedicineProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static InternalMedicineProviderCodes fromValue(String v) {
        for (InternalMedicineProviderCodes c: InternalMedicineProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}