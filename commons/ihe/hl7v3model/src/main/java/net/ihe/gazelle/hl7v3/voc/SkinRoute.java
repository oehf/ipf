/**
 * SkinRoute.java
 * <p>
 * File generated from the voc::SkinRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SkinRoute.
 *
 */

@XmlType(name = "SkinRoute")
@XmlEnum
@XmlRootElement(name = "SkinRoute")
public enum SkinRoute {
	@XmlEnumValue("OCDRESTA")
	OCDRESTA("OCDRESTA"),
	@XmlEnumValue("SKIN")
	SKIN("SKIN");
	
	private final String value;

    SkinRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SkinRoute fromValue(String v) {
        for (SkinRoute c: SkinRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}