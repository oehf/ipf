/**
 * Insertion.java
 * <p>
 * File generated from the voc::Insertion uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Insertion.
 *
 */

@XmlType(name = "Insertion")
@XmlEnum
@XmlRootElement(name = "Insertion")
public enum Insertion {
	@XmlEnumValue("CERVINS")
	CERVINS("CERVINS"),
	@XmlEnumValue("IOSURGINS")
	IOSURGINS("IOSURGINS"),
	@XmlEnumValue("IU")
	IU("IU"),
	@XmlEnumValue("LPINS")
	LPINS("LPINS"),
	@XmlEnumValue("PR")
	PR("PR"),
	@XmlEnumValue("SQSURGINS")
	SQSURGINS("SQSURGINS"),
	@XmlEnumValue("URETHINS")
	URETHINS("URETHINS"),
	@XmlEnumValue("VAGINSI")
	VAGINSI("VAGINSI");
	
	private final String value;

    Insertion(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Insertion fromValue(String v) {
        for (Insertion c: Insertion.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}