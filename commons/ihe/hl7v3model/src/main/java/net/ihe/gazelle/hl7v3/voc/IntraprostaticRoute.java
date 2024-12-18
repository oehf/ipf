/**
 * IntraprostaticRoute.java
 * <p>
 * File generated from the voc::IntraprostaticRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntraprostaticRoute.
 *
 */

@XmlType(name = "IntraprostaticRoute")
@XmlEnum
@XmlRootElement(name = "IntraprostaticRoute")
public enum IntraprostaticRoute {
	@XmlEnumValue("IPROSTINJ")
	IPROSTINJ("IPROSTINJ");
	
	private final String value;

    IntraprostaticRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntraprostaticRoute fromValue(String v) {
        for (IntraprostaticRoute c: IntraprostaticRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}