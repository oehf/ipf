/**
 * ActMoodProposal.java
 * <p>
 * File generated from the voc::ActMoodProposal uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActMoodProposal.
 *
 */

@XmlType(name = "ActMoodProposal")
@XmlEnum
@XmlRootElement(name = "ActMoodProposal")
public enum ActMoodProposal {
	@XmlEnumValue("PRP")
	PRP("PRP"),
	@XmlEnumValue("RMD")
	RMD("RMD");
	
	private final String value;

    ActMoodProposal(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActMoodProposal fromValue(String v) {
        for (ActMoodProposal c: ActMoodProposal.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}