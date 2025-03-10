/**
 * XRoleClassCoverage.java
 * <p>
 * File generated from the voc::XRoleClassCoverage uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XRoleClassCoverage.
 *
 */

@XmlType(name = "XRoleClassCoverage")
@XmlEnum
@XmlRootElement(name = "XRoleClassCoverage")
public enum XRoleClassCoverage {
	@XmlEnumValue("COVPTY")
	COVPTY("COVPTY"),
	@XmlEnumValue("POLHOLD")
	POLHOLD("POLHOLD"),
	@XmlEnumValue("SPNSR")
	SPNSR("SPNSR"),
	@XmlEnumValue("UNDWRT")
	UNDWRT("UNDWRT");
	
	private final String value;

    XRoleClassCoverage(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XRoleClassCoverage fromValue(String v) {
        for (XRoleClassCoverage c: XRoleClassCoverage.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}