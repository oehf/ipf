/**
 * ActClassComposition.java
 * <p>
 * File generated from the voc::ActClassComposition uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActClassComposition.
 *
 */

@XmlType(name = "ActClassComposition")
@XmlEnum
@XmlRootElement(name = "ActClassComposition")
public enum ActClassComposition {
	@XmlEnumValue("CDALVLONE")
	CDALVLONE("CDALVLONE"),
	@XmlEnumValue("COMPOSITION")
	COMPOSITION("COMPOSITION"),
	@XmlEnumValue("DOC")
	DOC("DOC"),
	@XmlEnumValue("DOCCLIN")
	DOCCLIN("DOCCLIN");
	
	private final String value;

    ActClassComposition(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActClassComposition fromValue(String v) {
        for (ActClassComposition c: ActClassComposition.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}