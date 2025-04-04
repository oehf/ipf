/**
 * CapsuleDrugForm.java
 * <p>
 * File generated from the voc::CapsuleDrugForm uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration CapsuleDrugForm.
 *
 */

@XmlType(name = "CapsuleDrugForm")
@XmlEnum
@XmlRootElement(name = "CapsuleDrugForm")
public enum CapsuleDrugForm {
	@XmlEnumValue("CAP")
	CAP("CAP"),
	@XmlEnumValue("ENTCAP")
	ENTCAP("ENTCAP"),
	@XmlEnumValue("ERCAP")
	ERCAP("ERCAP"),
	@XmlEnumValue("ERCAP12")
	ERCAP12("ERCAP12"),
	@XmlEnumValue("ERCAP24")
	ERCAP24("ERCAP24"),
	@XmlEnumValue("ERECCAP")
	ERECCAP("ERECCAP"),
	@XmlEnumValue("ERENTCAP")
	ERENTCAP("ERENTCAP"),
	@XmlEnumValue("ORCAP")
	ORCAP("ORCAP");
	
	private final String value;

    CapsuleDrugForm(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static CapsuleDrugForm fromValue(String v) {
        for (CapsuleDrugForm c: CapsuleDrugForm.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}