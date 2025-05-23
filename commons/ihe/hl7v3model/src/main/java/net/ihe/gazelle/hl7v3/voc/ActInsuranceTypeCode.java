/**
 * ActInsuranceTypeCode.java
 * <p>
 * File generated from the voc::ActInsuranceTypeCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActInsuranceTypeCode.
 *
 */

@XmlType(name = "ActInsuranceTypeCode")
@XmlEnum
@XmlRootElement(name = "ActInsuranceTypeCode")
public enum ActInsuranceTypeCode {
	@XmlEnumValue("ANNU")
	ANNU("ANNU"),
	@XmlEnumValue("AUTOPOL")
	AUTOPOL("AUTOPOL"),
	@XmlEnumValue("COL")
	COL("COL"),
	@XmlEnumValue("DENTAL")
	DENTAL("DENTAL"),
	@XmlEnumValue("DIS")
	DIS("DIS"),
	@XmlEnumValue("DISEASE")
	DISEASE("DISEASE"),
	@XmlEnumValue("DRUGPOL")
	DRUGPOL("DRUGPOL"),
	@XmlEnumValue("EHCPOL")
	EHCPOL("EHCPOL"),
	@XmlEnumValue("EWB")
	EWB("EWB"),
	@XmlEnumValue("FLEXP")
	FLEXP("FLEXP"),
	@XmlEnumValue("HIP")
	HIP("HIP"),
	@XmlEnumValue("HMO")
	HMO("HMO"),
	@XmlEnumValue("HSAPOL")
	HSAPOL("HSAPOL"),
	@XmlEnumValue("LIFE")
	LIFE("LIFE"),
	@XmlEnumValue("LTC")
	LTC("LTC"),
	@XmlEnumValue("MCPOL")
	MCPOL("MCPOL"),
	@XmlEnumValue("MENTPOL")
	MENTPOL("MENTPOL"),
	@XmlEnumValue("PNC")
	PNC("PNC"),
	@XmlEnumValue("POS")
	POS("POS"),
	@XmlEnumValue("PPO")
	PPO("PPO"),
	@XmlEnumValue("REI")
	REI("REI"),
	@XmlEnumValue("SUBPOL")
	SUBPOL("SUBPOL"),
	@XmlEnumValue("SURPL")
	SURPL("SURPL"),
	@XmlEnumValue("TLIFE")
	TLIFE("TLIFE"),
	@XmlEnumValue("ULIFE")
	ULIFE("ULIFE"),
	@XmlEnumValue("UMBRL")
	UMBRL("UMBRL"),
	@XmlEnumValue("UNINSMOT")
	UNINSMOT("UNINSMOT"),
	@XmlEnumValue("VISPOL")
	VISPOL("VISPOL");
	
	private final String value;

    ActInsuranceTypeCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActInsuranceTypeCode fromValue(String v) {
        for (ActInsuranceTypeCode c: ActInsuranceTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}