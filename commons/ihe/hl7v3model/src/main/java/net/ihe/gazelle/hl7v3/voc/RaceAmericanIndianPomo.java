/**
 * RaceAmericanIndianPomo.java
 *
 * File generated from the voc::RaceAmericanIndianPomo uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianPomo.
 *
 */

@XmlType(name = "RaceAmericanIndianPomo")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianPomo")
public enum RaceAmericanIndianPomo {
	@XmlEnumValue("1464-7")
	_14647("1464-7"),
	@XmlEnumValue("1465-4")
	_14654("1465-4"),
	@XmlEnumValue("1466-2")
	_14662("1466-2"),
	@XmlEnumValue("1467-0")
	_14670("1467-0"),
	@XmlEnumValue("1468-8")
	_14688("1468-8"),
	@XmlEnumValue("1469-6")
	_14696("1469-6"),
	@XmlEnumValue("1470-4")
	_14704("1470-4"),
	@XmlEnumValue("1471-2")
	_14712("1471-2"),
	@XmlEnumValue("1472-0")
	_14720("1472-0");
	
	private final String value;

    RaceAmericanIndianPomo(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianPomo fromValue(String v) {
        for (RaceAmericanIndianPomo c: RaceAmericanIndianPomo.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}