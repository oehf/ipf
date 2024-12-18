/**
 * ActMoodCompletionTrack.java
 * <p>
 * File generated from the voc::ActMoodCompletionTrack uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActMoodCompletionTrack.
 *
 */

@XmlType(name = "ActMoodCompletionTrack")
@XmlEnum
@XmlRootElement(name = "ActMoodCompletionTrack")
public enum ActMoodCompletionTrack {
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
	@XmlEnumValue("RMD")
	RMD("RMD"),
	@XmlEnumValue("RQO")
	RQO("RQO"),
	@XmlEnumValue("SLOT")
	SLOT("SLOT");
	
	private final String value;

    ActMoodCompletionTrack(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActMoodCompletionTrack fromValue(String v) {
        for (ActMoodCompletionTrack c: ActMoodCompletionTrack.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}