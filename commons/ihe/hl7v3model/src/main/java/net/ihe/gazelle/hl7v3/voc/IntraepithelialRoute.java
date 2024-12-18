/**
 * IntraepithelialRoute.java
 * <p>
 * File generated from the voc::IntraepithelialRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IntraepithelialRoute.
 *
 */

@XmlType(name = "IntraepithelialRoute")
@XmlEnum
@XmlRootElement(name = "IntraepithelialRoute")
public enum IntraepithelialRoute {
	@XmlEnumValue("IEPITHINJ")
	IEPITHINJ("IEPITHINJ");
	
	private final String value;

    IntraepithelialRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IntraepithelialRoute fromValue(String v) {
        for (IntraepithelialRoute c: IntraepithelialRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}