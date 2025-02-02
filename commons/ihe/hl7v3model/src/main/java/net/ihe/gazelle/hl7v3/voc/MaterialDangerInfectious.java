/**
 * MaterialDangerInfectious.java
 * <p>
 * File generated from the voc::MaterialDangerInfectious uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration MaterialDangerInfectious.
 *
 */

@XmlType(name = "MaterialDangerInfectious")
@XmlEnum
@XmlRootElement(name = "MaterialDangerInfectious")
public enum MaterialDangerInfectious {
	@XmlEnumValue("BHZ")
	BHZ("BHZ"),
	@XmlEnumValue("INF")
	INF("INF");
	
	private final String value;

    MaterialDangerInfectious(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static MaterialDangerInfectious fromValue(String v) {
        for (MaterialDangerInfectious c: MaterialDangerInfectious.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}