/**
 * MaterialEntityAdditive.java
 *
 * File generated from the voc::MaterialEntityAdditive uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration MaterialEntityAdditive.
 *
 */

@XmlType(name = "MaterialEntityAdditive")
@XmlEnum
@XmlRootElement(name = "MaterialEntityAdditive")
public enum MaterialEntityAdditive {
	@XmlEnumValue("ACDA")
	ACDA("ACDA"),
	@XmlEnumValue("ACDB")
	ACDB("ACDB"),
	@XmlEnumValue("ACET")
	ACET("ACET"),
	@XmlEnumValue("AMIES")
	AMIES("AMIES"),
	@XmlEnumValue("BACTM")
	BACTM("BACTM"),
	@XmlEnumValue("BF10")
	BF10("BF10"),
	@XmlEnumValue("BOR")
	BOR("BOR"),
	@XmlEnumValue("BOUIN")
	BOUIN("BOUIN"),
	@XmlEnumValue("BSKM")
	BSKM("BSKM"),
	@XmlEnumValue("C32")
	C32("C32"),
	@XmlEnumValue("C38")
	C38("C38"),
	@XmlEnumValue("CARS")
	CARS("CARS"),
	@XmlEnumValue("CARY")
	CARY("CARY"),
	@XmlEnumValue("CHLTM")
	CHLTM("CHLTM"),
	@XmlEnumValue("CTAD")
	CTAD("CTAD"),
	@XmlEnumValue("EDTK15")
	EDTK15("EDTK15"),
	@XmlEnumValue("EDTK75")
	EDTK75("EDTK75"),
	@XmlEnumValue("EDTN")
	EDTN("EDTN"),
	@XmlEnumValue("ENT")
	ENT("ENT"),
	@XmlEnumValue("F10")
	F10("F10"),
	@XmlEnumValue("FDP")
	FDP("FDP"),
	@XmlEnumValue("FL10")
	FL10("FL10"),
	@XmlEnumValue("FL100")
	FL100("FL100"),
	@XmlEnumValue("HCL6")
	HCL6("HCL6"),
	@XmlEnumValue("HEPA")
	HEPA("HEPA"),
	@XmlEnumValue("HEPL")
	HEPL("HEPL"),
	@XmlEnumValue("HEPN")
	HEPN("HEPN"),
	@XmlEnumValue("HNO3")
	HNO3("HNO3"),
	@XmlEnumValue("JKM")
	JKM("JKM"),
	@XmlEnumValue("KARN")
	KARN("KARN"),
	@XmlEnumValue("KOX")
	KOX("KOX"),
	@XmlEnumValue("LIA")
	LIA("LIA"),
	@XmlEnumValue("M4")
	M4("M4"),
	@XmlEnumValue("M4RT")
	M4RT("M4RT"),
	@XmlEnumValue("M5")
	M5("M5"),
	@XmlEnumValue("MICHTM")
	MICHTM("MICHTM"),
	@XmlEnumValue("MMDTM")
	MMDTM("MMDTM"),
	@XmlEnumValue("NAF")
	NAF("NAF"),
	@XmlEnumValue("NONE")
	NONE("NONE"),
	@XmlEnumValue("PAGE")
	PAGE("PAGE"),
	@XmlEnumValue("PHENOL")
	PHENOL("PHENOL"),
	@XmlEnumValue("PVA")
	PVA("PVA"),
	@XmlEnumValue("RLM")
	RLM("RLM"),
	@XmlEnumValue("SILICA")
	SILICA("SILICA"),
	@XmlEnumValue("SPS")
	SPS("SPS"),
	@XmlEnumValue("SST")
	SST("SST"),
	@XmlEnumValue("STUTM")
	STUTM("STUTM"),
	@XmlEnumValue("THROM")
	THROM("THROM"),
	@XmlEnumValue("THYMOL")
	THYMOL("THYMOL"),
	@XmlEnumValue("THYO")
	THYO("THYO"),
	@XmlEnumValue("TOLU")
	TOLU("TOLU"),
	@XmlEnumValue("URETM")
	URETM("URETM"),
	@XmlEnumValue("VIRTM")
	VIRTM("VIRTM"),
	@XmlEnumValue("WEST")
	WEST("WEST");
	
	private final String value;

    MaterialEntityAdditive(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static MaterialEntityAdditive fromValue(String v) {
        for (MaterialEntityAdditive c: MaterialEntityAdditive.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}