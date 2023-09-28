/**
 * EthnicityHispanicCentralAmerican.java
 *
 * File generated from the voc::EthnicityHispanicCentralAmerican uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration EthnicityHispanicCentralAmerican.
 *
 */

@XmlType(name = "EthnicityHispanicCentralAmerican")
@XmlEnum
@XmlRootElement(name = "EthnicityHispanicCentralAmerican")
public enum EthnicityHispanicCentralAmerican {
	@XmlEnumValue("2155-0")
	_21550("2155-0"),
	@XmlEnumValue("2156-8")
	_21568("2156-8"),
	@XmlEnumValue("2157-6")
	_21576("2157-6"),
	@XmlEnumValue("2158-4")
	_21584("2158-4"),
	@XmlEnumValue("2159-2")
	_21592("2159-2"),
	@XmlEnumValue("2160-0")
	_21600("2160-0"),
	@XmlEnumValue("2161-8")
	_21618("2161-8"),
	@XmlEnumValue("2162-6")
	_21626("2162-6"),
	@XmlEnumValue("2163-4")
	_21634("2163-4");
	
	private final String value;

    EthnicityHispanicCentralAmerican(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static EthnicityHispanicCentralAmerican fromValue(String v) {
        for (EthnicityHispanicCentralAmerican c: EthnicityHispanicCentralAmerican.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}