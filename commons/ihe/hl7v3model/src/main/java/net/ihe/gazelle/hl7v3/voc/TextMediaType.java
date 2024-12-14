/**
 * TextMediaType.java
 * <p>
 * File generated from the voc::TextMediaType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration TextMediaType.
 *
 */

@XmlType(name = "TextMediaType")
@XmlEnum
@XmlRootElement(name = "TextMediaType")
public enum TextMediaType {
	@XmlEnumValue("text/html")
	TEXTHTML("text/html"),
	@XmlEnumValue("text/plain")
	TEXTPLAIN("text/plain"),
	@XmlEnumValue("text/rtf")
	TEXTRTF("text/rtf"),
	@XmlEnumValue("text/sgml")
	TEXTSGML("text/sgml"),
	@XmlEnumValue("text/x-hl7-ft")
	TEXTXHL7FT("text/x-hl7-ft"),
	@XmlEnumValue("text/xml")
	TEXTXML("text/xml");
	
	private final String value;

    TextMediaType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static TextMediaType fromValue(String v) {
        for (TextMediaType c: TextMediaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}