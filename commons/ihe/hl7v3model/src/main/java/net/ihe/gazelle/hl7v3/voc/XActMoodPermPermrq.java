/**
 * XActMoodPermPermrq.java
 * <p>
 * File generated from the voc::XActMoodPermPermrq uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XActMoodPermPermrq.
 *
 */

@XmlType(name = "XActMoodPermPermrq")
@XmlEnum
@XmlRootElement(name = "XActMoodPermPermrq")
public enum XActMoodPermPermrq {
	@XmlEnumValue("PERM")
	PERM("PERM"),
	@XmlEnumValue("PERMRQ")
	PERMRQ("PERMRQ");
	
	private final String value;

    XActMoodPermPermrq(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XActMoodPermPermrq fromValue(String v) {
        for (XActMoodPermPermrq c: XActMoodPermPermrq.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}