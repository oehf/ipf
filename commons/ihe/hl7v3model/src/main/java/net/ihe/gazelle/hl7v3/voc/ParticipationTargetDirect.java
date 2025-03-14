/**
 * ParticipationTargetDirect.java
 * <p>
 * File generated from the voc::ParticipationTargetDirect uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ParticipationTargetDirect.
 *
 */

@XmlType(name = "ParticipationTargetDirect")
@XmlEnum
@XmlRootElement(name = "ParticipationTargetDirect")
public enum ParticipationTargetDirect {
	@XmlEnumValue("BBY")
	BBY("BBY"),
	@XmlEnumValue("CSM")
	CSM("CSM"),
	@XmlEnumValue("DEV")
	DEV("DEV"),
	@XmlEnumValue("DIR")
	DIR("DIR"),
	@XmlEnumValue("DON")
	DON("DON"),
	@XmlEnumValue("EXPAGNT")
	EXPAGNT("EXPAGNT"),
	@XmlEnumValue("EXPART")
	EXPART("EXPART"),
	@XmlEnumValue("EXPTRGT")
	EXPTRGT("EXPTRGT"),
	@XmlEnumValue("EXSRC")
	EXSRC("EXSRC"),
	@XmlEnumValue("NRD")
	NRD("NRD"),
	@XmlEnumValue("PRD")
	PRD("PRD"),
	@XmlEnumValue("RDV")
	RDV("RDV"),
	@XmlEnumValue("SBJ")
	SBJ("SBJ"),
	@XmlEnumValue("SPC")
	SPC("SPC");
	
	private final String value;

    ParticipationTargetDirect(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ParticipationTargetDirect fromValue(String v) {
        for (ParticipationTargetDirect c: ParticipationTargetDirect.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}