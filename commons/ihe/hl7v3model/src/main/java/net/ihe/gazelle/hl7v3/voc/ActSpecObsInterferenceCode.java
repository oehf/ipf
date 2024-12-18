/**
 * ActSpecObsInterferenceCode.java
 * <p>
 * File generated from the voc::ActSpecObsInterferenceCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActSpecObsInterferenceCode.
 *
 */

@XmlType(name = "ActSpecObsInterferenceCode")
@XmlEnum
@XmlRootElement(name = "ActSpecObsInterferenceCode")
public enum ActSpecObsInterferenceCode {
	@XmlEnumValue("FIBRIN")
	FIBRIN("FIBRIN"),
	@XmlEnumValue("HEMOLYSIS")
	HEMOLYSIS("HEMOLYSIS"),
	@XmlEnumValue("ICTERUS")
	ICTERUS("ICTERUS"),
	@XmlEnumValue("INTFR")
	INTFR("INTFR"),
	@XmlEnumValue("LIPEMIA")
	LIPEMIA("LIPEMIA");
	
	private final String value;

    ActSpecObsInterferenceCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActSpecObsInterferenceCode fromValue(String v) {
        for (ActSpecObsInterferenceCode c: ActSpecObsInterferenceCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}