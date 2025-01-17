/**
 * GasSolidSpray.java
 * <p>
 * File generated from the voc::GasSolidSpray uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration GasSolidSpray.
 *
 */

@XmlType(name = "GasSolidSpray")
@XmlEnum
@XmlRootElement(name = "GasSolidSpray")
public enum GasSolidSpray {
	@XmlEnumValue("BAINHLPWD")
	BAINHLPWD("BAINHLPWD"),
	@XmlEnumValue("INHL")
	INHL("INHL"),
	@XmlEnumValue("INHLPWD")
	INHLPWD("INHLPWD"),
	@XmlEnumValue("MDINHLPWD")
	MDINHLPWD("MDINHLPWD"),
	@XmlEnumValue("NASINHL")
	NASINHL("NASINHL"),
	@XmlEnumValue("ORINHL")
	ORINHL("ORINHL"),
	@XmlEnumValue("PWDSPRY")
	PWDSPRY("PWDSPRY"),
	@XmlEnumValue("SPRYADAPT")
	SPRYADAPT("SPRYADAPT");
	
	private final String value;

    GasSolidSpray(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static GasSolidSpray fromValue(String v) {
        for (GasSolidSpray c: GasSolidSpray.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}