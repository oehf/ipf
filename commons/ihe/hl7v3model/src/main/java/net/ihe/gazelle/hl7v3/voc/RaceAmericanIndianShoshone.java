/**
 * RaceAmericanIndianShoshone.java
 *
 * File generated from the voc::RaceAmericanIndianShoshone uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAmericanIndianShoshone.
 *
 */

@XmlType(name = "RaceAmericanIndianShoshone")
@XmlEnum
@XmlRootElement(name = "RaceAmericanIndianShoshone")
public enum RaceAmericanIndianShoshone {
	@XmlEnumValue("1586-7")
	_15867("1586-7"),
	@XmlEnumValue("1587-5")
	_15875("1587-5"),
	@XmlEnumValue("1588-3")
	_15883("1588-3"),
	@XmlEnumValue("1589-1")
	_15891("1589-1"),
	@XmlEnumValue("1590-9")
	_15909("1590-9"),
	@XmlEnumValue("1591-7")
	_15917("1591-7"),
	@XmlEnumValue("1592-5")
	_15925("1592-5"),
	@XmlEnumValue("1593-3")
	_15933("1593-3"),
	@XmlEnumValue("1594-1")
	_15941("1594-1"),
	@XmlEnumValue("1595-8")
	_15958("1595-8"),
	@XmlEnumValue("1596-6")
	_15966("1596-6"),
	@XmlEnumValue("1597-4")
	_15974("1597-4"),
	@XmlEnumValue("1598-2")
	_15982("1598-2"),
	@XmlEnumValue("1599-0")
	_15990("1599-0"),
	@XmlEnumValue("1600-6")
	_16006("1600-6");
	
	private final String value;

    RaceAmericanIndianShoshone(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAmericanIndianShoshone fromValue(String v) {
        for (RaceAmericanIndianShoshone c: RaceAmericanIndianShoshone.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}