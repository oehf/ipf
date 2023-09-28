/**
 * ActPharmacySupplyType.java
 *
 * File generated from the voc::ActPharmacySupplyType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActPharmacySupplyType.
 *
 */

@XmlType(name = "ActPharmacySupplyType")
@XmlEnum
@XmlRootElement(name = "ActPharmacySupplyType")
public enum ActPharmacySupplyType {
	@XmlEnumValue("DF")
	DF("DF"),
	@XmlEnumValue("EM")
	EM("EM"),
	@XmlEnumValue("FF")
	FF("FF"),
	@XmlEnumValue("FFC")
	FFC("FFC"),
	@XmlEnumValue("FFCS")
	FFCS("FFCS"),
	@XmlEnumValue("FFP")
	FFP("FFP"),
	@XmlEnumValue("FFPS")
	FFPS("FFPS"),
	@XmlEnumValue("FFS")
	FFS("FFS"),
	@XmlEnumValue("FS")
	FS("FS"),
	@XmlEnumValue("MS")
	MS("MS"),
	@XmlEnumValue("RF")
	RF("RF"),
	@XmlEnumValue("RFC")
	RFC("RFC"),
	@XmlEnumValue("RFCS")
	RFCS("RFCS"),
	@XmlEnumValue("RFF")
	RFF("RFF"),
	@XmlEnumValue("RFFS")
	RFFS("RFFS"),
	@XmlEnumValue("RFP")
	RFP("RFP"),
	@XmlEnumValue("RFPS")
	RFPS("RFPS"),
	@XmlEnumValue("RFS")
	RFS("RFS"),
	@XmlEnumValue("SO")
	SO("SO"),
	@XmlEnumValue("TB")
	TB("TB"),
	@XmlEnumValue("TBS")
	TBS("TBS"),
	@XmlEnumValue("TF")
	TF("TF"),
	@XmlEnumValue("TFS")
	TFS("TFS"),
	@XmlEnumValue("UD")
	UD("UD");
	
	private final String value;

    ActPharmacySupplyType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActPharmacySupplyType fromValue(String v) {
        for (ActPharmacySupplyType c: ActPharmacySupplyType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}