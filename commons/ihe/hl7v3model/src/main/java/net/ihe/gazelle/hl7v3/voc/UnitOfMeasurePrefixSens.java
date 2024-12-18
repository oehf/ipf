/**
 * UnitOfMeasurePrefixSens.java
 * <p>
 * File generated from the voc::UnitOfMeasurePrefixSens uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration UnitOfMeasurePrefixSens.
 *
 */

@XmlType(name = "UnitOfMeasurePrefixSens")
@XmlEnum
@XmlRootElement(name = "UnitOfMeasurePrefixSens")
public enum UnitOfMeasurePrefixSens {
	@XmlEnumValue("E")
	E("E"),
	@XmlEnumValue("G")
	G("G"),
	@XmlEnumValue("Gi")
	GI("Gi"),
	@XmlEnumValue("Ki")
	KI("Ki"),
	@XmlEnumValue("M")
	M("M"),
	@XmlEnumValue("Mi")
	MI("Mi"),
	@XmlEnumValue("P")
	P("P"),
	@XmlEnumValue("T")
	T("T"),
	@XmlEnumValue("Ti")
	TI("Ti"),
	@XmlEnumValue("Y")
	Y("Y"),
	@XmlEnumValue("Z")
	Z("Z"),
	@XmlEnumValue("a")
	A("a"),
	@XmlEnumValue("c")
	C("c"),
	@XmlEnumValue("d")
	D("d"),
	@XmlEnumValue("da")
	DA("da"),
	@XmlEnumValue("f")
	F("f"),
	@XmlEnumValue("h")
	H("h"),
	@XmlEnumValue("k")
	K("k"),
	@XmlEnumValue("m")
	M1("m"),
	@XmlEnumValue("n")
	N("n"),
	@XmlEnumValue("p")
	P1("p"),
	@XmlEnumValue("u")
	U("u"),
	@XmlEnumValue("y")
	Y1("y"),
	@XmlEnumValue("z")
	Z1("z");
	
	private final String value;

    UnitOfMeasurePrefixSens(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static UnitOfMeasurePrefixSens fromValue(String v) {
        for (UnitOfMeasurePrefixSens c: UnitOfMeasurePrefixSens.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}