/**
 * RaceAmericanIndianChemakuan.java
 *
 * File generated from the voc::RaceAmericanIndianChemakuan uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianChemakuan.
 *
 */

@XmlType(name = "RaceAmericanIndianChemakuan")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianChemakuan")
public enum RaceAmericanIndianChemakuan {
	@XmlEnumValue("1082-7")
	_10827("1082-7"),
	@XmlEnumValue("1083-5")
	_10835("1083-5"),
	@XmlEnumValue("1084-3")
	_10843("1084-3");
	
	private final String value;

    RaceAmericanIndianChemakuan(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianChemakuan fromValue(String v) {
        for (RaceAmericanIndianChemakuan c: RaceAmericanIndianChemakuan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}