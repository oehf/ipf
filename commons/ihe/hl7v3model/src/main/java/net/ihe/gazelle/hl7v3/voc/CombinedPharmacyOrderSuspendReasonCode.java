/**
 * CombinedPharmacyOrderSuspendReasonCode.java
 * <p>
 * File generated from the voc::CombinedPharmacyOrderSuspendReasonCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration CombinedPharmacyOrderSuspendReasonCode.
 *
 */

@XmlType(name = "CombinedPharmacyOrderSuspendReasonCode")
@XmlEnum
@XmlRootElement(name = "CombinedPharmacyOrderSuspendReasonCode")
public enum CombinedPharmacyOrderSuspendReasonCode {
	@XmlEnumValue("ALTCHOICE")
	ALTCHOICE("ALTCHOICE"),
	@XmlEnumValue("CLARIF")
	CLARIF("CLARIF"),
	@XmlEnumValue("DRUGHIGH")
	DRUGHIGH("DRUGHIGH"),
	@XmlEnumValue("HOSPADM")
	HOSPADM("HOSPADM"),
	@XmlEnumValue("LABINT")
	LABINT("LABINT"),
	@XmlEnumValue("NON-AVAIL")
	NONAVAIL("NON-AVAIL"),
	@XmlEnumValue("PREG")
	PREG("PREG"),
	@XmlEnumValue("SALG")
	SALG("SALG"),
	@XmlEnumValue("SDDI")
	SDDI("SDDI"),
	@XmlEnumValue("SDUPTHER")
	SDUPTHER("SDUPTHER"),
	@XmlEnumValue("SINTOL")
	SINTOL("SINTOL"),
	@XmlEnumValue("SURG")
	SURG("SURG"),
	@XmlEnumValue("WASHOUT")
	WASHOUT("WASHOUT");
	
	private final String value;

    CombinedPharmacyOrderSuspendReasonCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static CombinedPharmacyOrderSuspendReasonCode fromValue(String v) {
        for (CombinedPharmacyOrderSuspendReasonCode c: CombinedPharmacyOrderSuspendReasonCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}