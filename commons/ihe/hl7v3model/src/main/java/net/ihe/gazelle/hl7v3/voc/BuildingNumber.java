/**
 * BuildingNumber.java
 * <p>
 * File generated from the voc::BuildingNumber uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration BuildingNumber.
 *
 */

@XmlType(name = "BuildingNumber")
@XmlEnum
@XmlRootElement(name = "BuildingNumber")
public enum BuildingNumber {
	@XmlEnumValue("BNN")
	BNN("BNN"),
	@XmlEnumValue("BNR")
	BNR("BNR"),
	@XmlEnumValue("BNS")
	BNS("BNS"),
	@XmlEnumValue("POB")
	POB("POB");
	
	private final String value;

    BuildingNumber(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static BuildingNumber fromValue(String v) {
        for (BuildingNumber c: BuildingNumber.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}