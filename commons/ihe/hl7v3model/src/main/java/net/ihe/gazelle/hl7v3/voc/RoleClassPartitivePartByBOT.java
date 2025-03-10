/**
 * RoleClassPartitivePartByBOT.java
 * <p>
 * File generated from the voc::RoleClassPartitivePartByBOT uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RoleClassPartitivePartByBOT.
 *
 */

@XmlType(name = "RoleClassPartitivePartByBOT")
@XmlEnum
@XmlRootElement(name = "RoleClassPartitivePartByBOT")
public enum RoleClassPartitivePartByBOT {
	@XmlEnumValue("ACTM")
	ACTM("ACTM"),
	@XmlEnumValue("PART")
	PART("PART");
	
	private final String value;

    RoleClassPartitivePartByBOT(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RoleClassPartitivePartByBOT fromValue(String v) {
        for (RoleClassPartitivePartByBOT c: RoleClassPartitivePartByBOT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}