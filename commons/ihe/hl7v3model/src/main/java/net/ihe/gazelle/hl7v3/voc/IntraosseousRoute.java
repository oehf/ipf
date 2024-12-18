/**
 * IntraosseousRoute.java
 * <p>
 * File generated from the voc::IntraosseousRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntraosseousRoute.
 *
 */

@XmlType(name = "IntraosseousRoute")
@XmlEnum
@XmlRootElement(name = "IntraosseousRoute")
public enum IntraosseousRoute {
	@XmlEnumValue("IOSSC")
	IOSSC("IOSSC"),
	@XmlEnumValue("IOSSINJ")
	IOSSINJ("IOSSINJ");
	
	private final String value;

    IntraosseousRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntraosseousRoute fromValue(String v) {
        for (IntraosseousRoute c: IntraosseousRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}