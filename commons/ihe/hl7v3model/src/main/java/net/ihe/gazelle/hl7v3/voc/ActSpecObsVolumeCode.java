/**
 * ActSpecObsVolumeCode.java
 * <p>
 * File generated from the voc::ActSpecObsVolumeCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActSpecObsVolumeCode.
 *
 */

@XmlType(name = "ActSpecObsVolumeCode")
@XmlEnum
@XmlRootElement(name = "ActSpecObsVolumeCode")
public enum ActSpecObsVolumeCode {
	@XmlEnumValue("AVAILABLE")
	AVAILABLE("AVAILABLE"),
	@XmlEnumValue("CONSUMPTION")
	CONSUMPTION("CONSUMPTION"),
	@XmlEnumValue("CURRENT")
	CURRENT("CURRENT"),
	@XmlEnumValue("INITIAL")
	INITIAL("INITIAL"),
	@XmlEnumValue("VOLUME")
	VOLUME("VOLUME");
	
	private final String value;

    ActSpecObsVolumeCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActSpecObsVolumeCode fromValue(String v) {
        for (ActSpecObsVolumeCode c: ActSpecObsVolumeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}