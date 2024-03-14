/**
 * ParticipationMode.java
 *
 * File generated from the voc::ParticipationMode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ParticipationMode.
 *
 */

@XmlType(name = "ParticipationMode")
@XmlEnum
@XmlRootElement(name = "ParticipationMode")
public enum ParticipationMode {
	@XmlEnumValue("DICTATE")
	DICTATE("DICTATE"),
	@XmlEnumValue("ELECTRONIC")
	ELECTRONIC("ELECTRONIC"),
	@XmlEnumValue("EMAILWRIT")
	EMAILWRIT("EMAILWRIT"),
	@XmlEnumValue("FACE")
	FACE("FACE"),
	@XmlEnumValue("FAXWRIT")
	FAXWRIT("FAXWRIT"),
	@XmlEnumValue("HANDWRIT")
	HANDWRIT("HANDWRIT"),
	@XmlEnumValue("PHONE")
	PHONE("PHONE"),
	@XmlEnumValue("PHYSICAL")
	PHYSICAL("PHYSICAL"),
	@XmlEnumValue("REMOTE")
	REMOTE("REMOTE"),
	@XmlEnumValue("TYPEWRIT")
	TYPEWRIT("TYPEWRIT"),
	@XmlEnumValue("VERBAL")
	VERBAL("VERBAL"),
	@XmlEnumValue("VIDEOCONF")
	VIDEOCONF("VIDEOCONF"),
	@XmlEnumValue("WRITTEN")
	WRITTEN("WRITTEN");
	
	private final String value;

    ParticipationMode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ParticipationMode fromValue(String v) {
        for (ParticipationMode c: ParticipationMode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}