/**
 * XActMoodOrdPrms.java
 *
 * File generated from the voc::XActMoodOrdPrms uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XActMoodOrdPrms.
 *
 */

@XmlType(name = "XActMoodOrdPrms")
@XmlEnum
@XmlRootElement(name = "XActMoodOrdPrms")
public enum XActMoodOrdPrms {
	@XmlEnumValue("PRMS")
	PRMS("PRMS"),
	@XmlEnumValue("RQO")
	RQO("RQO");
	
	private final String value;

    XActMoodOrdPrms(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XActMoodOrdPrms fromValue(String v) {
        for (XActMoodOrdPrms c: XActMoodOrdPrms.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}