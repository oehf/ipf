/**
 * DentalProvidersProviderCodes.java
 * <p>
 * File generated from the voc::DentalProvidersProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DentalProvidersProviderCodes.
 *
 */

@XmlType(name = "DentalProvidersProviderCodes")
@XmlEnum
@XmlRootElement(name = "DentalProvidersProviderCodes")
public enum DentalProvidersProviderCodes {
	@XmlEnumValue("120000000X")
	_120000000X("120000000X"),
	@XmlEnumValue("122300000X")
	_122300000X("122300000X"),
	@XmlEnumValue("1223D0001X")
	_1223D0001X("1223D0001X"),
	@XmlEnumValue("1223E0200X")
	_1223E0200X("1223E0200X"),
	@XmlEnumValue("1223G0001X")
	_1223G0001X("1223G0001X"),
	@XmlEnumValue("1223P0106X")
	_1223P0106X("1223P0106X"),
	@XmlEnumValue("1223P0221X")
	_1223P0221X("1223P0221X"),
	@XmlEnumValue("1223P0300X")
	_1223P0300X("1223P0300X"),
	@XmlEnumValue("1223P0700X")
	_1223P0700X("1223P0700X"),
	@XmlEnumValue("1223S0112X")
	_1223S0112X("1223S0112X"),
	@XmlEnumValue("1223X0008X")
	_1223X0008X("1223X0008X"),
	@XmlEnumValue("1223X0400X")
	_1223X0400X("1223X0400X"),
	@XmlEnumValue("122400000X")
	_122400000X("122400000X"),
	@XmlEnumValue("124Q00000X")
	_124Q00000X("124Q00000X"),
	@XmlEnumValue("126800000X")
	_126800000X("126800000X"),
	@XmlEnumValue("126900000X")
	_126900000X("126900000X");
	
	private final String value;

    DentalProvidersProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DentalProvidersProviderCodes fromValue(String v) {
        for (DentalProvidersProviderCodes c: DentalProvidersProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}