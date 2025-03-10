/**
 * AdministrationMedicalDevice.java
 * <p>
 * File generated from the voc::AdministrationMedicalDevice uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration AdministrationMedicalDevice.
 *
 */

@XmlType(name = "AdministrationMedicalDevice")
@XmlEnum
@XmlRootElement(name = "AdministrationMedicalDevice")
public enum AdministrationMedicalDevice {
	@XmlEnumValue("AINJ")
	AINJ("AINJ"),
	@XmlEnumValue("APLCTR")
	APLCTR("APLCTR"),
	@XmlEnumValue("DSKS")
	DSKS("DSKS"),
	@XmlEnumValue("DSKUNH")
	DSKUNH("DSKUNH"),
	@XmlEnumValue("INH")
	INH("INH"),
	@XmlEnumValue("PEN")
	PEN("PEN"),
	@XmlEnumValue("PMP")
	PMP("PMP"),
	@XmlEnumValue("SYR")
	SYR("SYR"),
	@XmlEnumValue("TRBINH")
	TRBINH("TRBINH");
	
	private final String value;

    AdministrationMedicalDevice(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static AdministrationMedicalDevice fromValue(String v) {
        for (AdministrationMedicalDevice c: AdministrationMedicalDevice.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}