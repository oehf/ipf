/**
 * RespiteCareProviderCodes.java
 * <p>
 * File generated from the voc::RespiteCareProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RespiteCareProviderCodes.
 *
 */

@XmlType(name = "RespiteCareProviderCodes")
@XmlEnum
@XmlRootElement(name = "RespiteCareProviderCodes")
public enum RespiteCareProviderCodes {
	@XmlEnumValue("385H00000X")
	_385H00000X("385H00000X"),
	@XmlEnumValue("385HR2050X")
	_385HR2050X("385HR2050X"),
	@XmlEnumValue("385HR2055X")
	_385HR2055X("385HR2055X"),
	@XmlEnumValue("385HR2060X")
	_385HR2060X("385HR2060X"),
	@XmlEnumValue("385HR2065X")
	_385HR2065X("385HR2065X");
	
	private final String value;

    RespiteCareProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RespiteCareProviderCodes fromValue(String v) {
        for (RespiteCareProviderCodes c: RespiteCareProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}