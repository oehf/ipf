/**
 * NursingServiceRelatedProviderHIPAA.java
 * <p>
 * File generated from the voc::NursingServiceRelatedProviderHIPAA uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration NursingServiceRelatedProviderHIPAA.
 *
 */

@XmlType(name = "NursingServiceRelatedProviderHIPAA")
@XmlEnum
@XmlRootElement(name = "NursingServiceRelatedProviderHIPAA")
public enum NursingServiceRelatedProviderHIPAA {
	@XmlEnumValue("374700000N")
	_374700000N("374700000N"),
	@XmlEnumValue("3747P1801N")
	_3747P1801N("3747P1801N");
	
	private final String value;

    NursingServiceRelatedProviderHIPAA(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static NursingServiceRelatedProviderHIPAA fromValue(String v) {
        for (NursingServiceRelatedProviderHIPAA c: NursingServiceRelatedProviderHIPAA.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}