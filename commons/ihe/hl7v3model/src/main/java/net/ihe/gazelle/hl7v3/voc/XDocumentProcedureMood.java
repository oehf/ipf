/**
 * XDocumentProcedureMood.java
 * <p>
 * File generated from the voc::XDocumentProcedureMood uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XDocumentProcedureMood.
 *
 */

@XmlType(name = "XDocumentProcedureMood")
@XmlEnum
@XmlRootElement(name = "XDocumentProcedureMood")
public enum XDocumentProcedureMood {
	@XmlEnumValue("APT")
	APT("APT"),
	@XmlEnumValue("ARQ")
	ARQ("ARQ"),
	@XmlEnumValue("DEF")
	DEF("DEF"),
	@XmlEnumValue("EVN")
	EVN("EVN"),
	@XmlEnumValue("INT")
	INT("INT"),
	@XmlEnumValue("PRMS")
	PRMS("PRMS"),
	@XmlEnumValue("PRP")
	PRP("PRP"),
	@XmlEnumValue("RQO")
	RQO("RQO");
	
	private final String value;

    XDocumentProcedureMood(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XDocumentProcedureMood fromValue(String v) {
        for (XDocumentProcedureMood c: XDocumentProcedureMood.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}