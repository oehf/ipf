/**
 * GastricRoute.java
 *
 * File generated from the voc::GastricRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration GastricRoute.
 *
 */

@XmlType(name = "GastricRoute")
@XmlEnum
@XmlRootElement(name = "GastricRoute")
public enum GastricRoute {
	@XmlEnumValue("GBINJ")
	GBINJ("GBINJ"),
	@XmlEnumValue("GT")
	GT("GT"),
	@XmlEnumValue("NGT")
	NGT("NGT"),
	@XmlEnumValue("OGT")
	OGT("OGT");
	
	private final String value;

    GastricRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static GastricRoute fromValue(String v) {
        for (GastricRoute c: GastricRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}