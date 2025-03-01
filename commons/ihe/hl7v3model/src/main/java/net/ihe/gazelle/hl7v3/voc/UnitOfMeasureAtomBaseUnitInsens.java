/**
 * UnitOfMeasureAtomBaseUnitInsens.java
 * <p>
 * File generated from the voc::UnitOfMeasureAtomBaseUnitInsens uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration UnitOfMeasureAtomBaseUnitInsens.
 *
 */

@XmlType(name = "UnitOfMeasureAtomBaseUnitInsens")
@XmlEnum
@XmlRootElement(name = "UnitOfMeasureAtomBaseUnitInsens")
public enum UnitOfMeasureAtomBaseUnitInsens {
	@XmlEnumValue("C")
	C("C"),
	@XmlEnumValue("CD")
	CD("CD"),
	@XmlEnumValue("G")
	G("G"),
	@XmlEnumValue("K")
	K("K"),
	@XmlEnumValue("M")
	M("M"),
	@XmlEnumValue("RAD")
	RAD("RAD"),
	@XmlEnumValue("S")
	S("S");
	
	private final String value;

    UnitOfMeasureAtomBaseUnitInsens(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static UnitOfMeasureAtomBaseUnitInsens fromValue(String v) {
        for (UnitOfMeasureAtomBaseUnitInsens c: UnitOfMeasureAtomBaseUnitInsens.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}