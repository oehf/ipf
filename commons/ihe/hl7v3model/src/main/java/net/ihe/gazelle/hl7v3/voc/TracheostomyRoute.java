/**
 * TracheostomyRoute.java
 *
 * File generated from the voc::TracheostomyRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration TracheostomyRoute.
 *
 */

@XmlType(name = "TracheostomyRoute")
@XmlEnum
@XmlRootElement(name = "TracheostomyRoute")
public enum TracheostomyRoute {
	@XmlEnumValue("TRACH")
	TRACH("TRACH"),
	@XmlEnumValue("TRACHINSTL")
	TRACHINSTL("TRACHINSTL");
	
	private final String value;

    TracheostomyRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static TracheostomyRoute fromValue(String v) {
        for (TracheostomyRoute c: TracheostomyRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}