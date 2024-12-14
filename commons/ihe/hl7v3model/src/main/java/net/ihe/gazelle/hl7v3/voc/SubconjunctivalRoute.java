/**
 * SubconjunctivalRoute.java
 * <p>
 * File generated from the voc::SubconjunctivalRoute uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration SubconjunctivalRoute.
 *
 */

@XmlType(name = "SubconjunctivalRoute")
@XmlEnum
@XmlRootElement(name = "SubconjunctivalRoute")
public enum SubconjunctivalRoute {
	@XmlEnumValue("SCINJ")
	SCINJ("SCINJ"),
	@XmlEnumValue("SUBCONJTA")
	SUBCONJTA("SUBCONJTA");
	
	private final String value;

    SubconjunctivalRoute(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static SubconjunctivalRoute fromValue(String v) {
        for (SubconjunctivalRoute c: SubconjunctivalRoute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}