/**
 * TableFrame.java
 *
 * File generated from the voc::TableFrame uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration TableFrame.
 *
 */

@XmlType(name = "TableFrame")
@XmlEnum
@XmlRootElement(name = "TableFrame")
public enum TableFrame {
	@XmlEnumValue("above")
	ABOVE("above"),
	@XmlEnumValue("below")
	BELOW("below"),
	@XmlEnumValue("border")
	BORDER("border"),
	@XmlEnumValue("box")
	BOX("box"),
	@XmlEnumValue("hsides")
	HSIDES("hsides"),
	@XmlEnumValue("lhs")
	LHS("lhs"),
	@XmlEnumValue("rhs")
	RHS("rhs"),
	@XmlEnumValue("void")
	VOID("void"),
	@XmlEnumValue("vsides")
	VSIDES("vsides");
	
	private final String value;

    TableFrame(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static TableFrame fromValue(String v) {
        for (TableFrame c: TableFrame.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}