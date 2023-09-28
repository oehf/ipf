/**
 * SchedulingActReason.java
 *
 * File generated from the voc::SchedulingActReason uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SchedulingActReason.
 *
 */

@XmlType(name = "SchedulingActReason")
@XmlEnum
@XmlRootElement(name = "SchedulingActReason")
public enum SchedulingActReason {
	@XmlEnumValue("BLK")
	BLK("BLK"),
	@XmlEnumValue("DEC")
	DEC("DEC"),
	@XmlEnumValue("FIN")
	FIN("FIN"),
	@XmlEnumValue("MED")
	MED("MED"),
	@XmlEnumValue("MTG")
	MTG("MTG"),
	@XmlEnumValue("PAT")
	PAT("PAT"),
	@XmlEnumValue("PHY")
	PHY("PHY");
	
	private final String value;

    SchedulingActReason(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SchedulingActReason fromValue(String v) {
        for (SchedulingActReason c: SchedulingActReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}