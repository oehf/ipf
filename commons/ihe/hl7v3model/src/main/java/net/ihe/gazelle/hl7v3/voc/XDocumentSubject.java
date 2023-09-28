/**
 * XDocumentSubject.java
 *
 * File generated from the voc::XDocumentSubject uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XDocumentSubject.
 *
 */

@XmlType(name = "XDocumentSubject")
@XmlEnum
@XmlRootElement(name = "XDocumentSubject")
public enum XDocumentSubject {
	@XmlEnumValue("PAT")
	PAT("PAT"),
	@XmlEnumValue("PRS")
	PRS("PRS");
	
	private final String value;

    XDocumentSubject(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XDocumentSubject fromValue(String v) {
        for (XDocumentSubject c: XDocumentSubject.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}