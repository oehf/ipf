/**
 * OralTablet.java
 *
 * File generated from the voc::OralTablet uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration OralTablet.
 *
 */

@XmlType(name = "OralTablet")
@XmlEnum
@XmlRootElement(name = "OralTablet")
public enum OralTablet {
	@XmlEnumValue("BUCTAB")
	BUCTAB("BUCTAB"),
	@XmlEnumValue("CAPLET")
	CAPLET("CAPLET"),
	@XmlEnumValue("CHEWTAB")
	CHEWTAB("CHEWTAB"),
	@XmlEnumValue("CPTAB")
	CPTAB("CPTAB"),
	@XmlEnumValue("DISINTAB")
	DISINTAB("DISINTAB"),
	@XmlEnumValue("DRTAB")
	DRTAB("DRTAB"),
	@XmlEnumValue("ECTAB")
	ECTAB("ECTAB"),
	@XmlEnumValue("ERECTAB")
	ERECTAB("ERECTAB"),
	@XmlEnumValue("ERTAB")
	ERTAB("ERTAB"),
	@XmlEnumValue("ERTAB12")
	ERTAB12("ERTAB12"),
	@XmlEnumValue("ERTAB24")
	ERTAB24("ERTAB24"),
	@XmlEnumValue("ORTAB")
	ORTAB("ORTAB"),
	@XmlEnumValue("ORTROCHE")
	ORTROCHE("ORTROCHE"),
	@XmlEnumValue("SLTAB")
	SLTAB("SLTAB"),
	@XmlEnumValue("SRBUCTAB")
	SRBUCTAB("SRBUCTAB");
	
	private final String value;

    OralTablet(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static OralTablet fromValue(String v) {
        for (OralTablet c: OralTablet.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}