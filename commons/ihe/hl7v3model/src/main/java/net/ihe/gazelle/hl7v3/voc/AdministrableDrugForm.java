/**
 * AdministrableDrugForm.java
 * <p>
 * File generated from the voc::AdministrableDrugForm uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration AdministrableDrugForm.
 *
 */

@XmlType(name = "AdministrableDrugForm")
@XmlEnum
@XmlRootElement(name = "AdministrableDrugForm")
public enum AdministrableDrugForm {
	@XmlEnumValue("APPFUL")
	APPFUL("APPFUL"),
	@XmlEnumValue("DROP")
	DROP("DROP"),
	@XmlEnumValue("NDROP")
	NDROP("NDROP"),
	@XmlEnumValue("OPDROP")
	OPDROP("OPDROP"),
	@XmlEnumValue("ORDROP")
	ORDROP("ORDROP"),
	@XmlEnumValue("OTDROP")
	OTDROP("OTDROP"),
	@XmlEnumValue("PUFF")
	PUFF("PUFF"),
	@XmlEnumValue("SCOOP")
	SCOOP("SCOOP"),
	@XmlEnumValue("SPRY")
	SPRY("SPRY");
	
	private final String value;

    AdministrableDrugForm(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static AdministrableDrugForm fromValue(String v) {
        for (AdministrableDrugForm c: AdministrableDrugForm.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}