/**
 * PersonNameUse.java
 * <p>
 * File generated from the voc::PersonNameUse uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PersonNameUse.
 *
 */

@XmlType(name = "PersonNameUse")
@XmlEnum
@XmlRootElement(name = "PersonNameUse")
public enum PersonNameUse {
	@XmlEnumValue("A")
	A("A"),
	@XmlEnumValue("ABC")
	ABC("ABC"),
	@XmlEnumValue("ASGN")
	ASGN("ASGN"),
	@XmlEnumValue("C")
	C("C"),
	@XmlEnumValue("I")
	I("I"),
	@XmlEnumValue("IDE")
	IDE("IDE"),
	@XmlEnumValue("L")
	L("L"),
	@XmlEnumValue("OR")
	OR("OR"),
	@XmlEnumValue("P")
	P("P"),
	@XmlEnumValue("PHON")
	PHON("PHON"),
	@XmlEnumValue("R")
	R("R"),
	@XmlEnumValue("SNDX")
	SNDX("SNDX"),
	@XmlEnumValue("SRCH")
	SRCH("SRCH"),
	@XmlEnumValue("SYL")
	SYL("SYL");
	
	private final String value;

    PersonNameUse(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PersonNameUse fromValue(String v) {
        for (PersonNameUse c: PersonNameUse.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}