/**
 * DataTypeTelecommunicationAddress.java
 *
 * File generated from the voc::DataTypeTelecommunicationAddress uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DataTypeTelecommunicationAddress.
 *
 */

@XmlType(name = "DataTypeTelecommunicationAddress")
@XmlEnum
@XmlRootElement(name = "DataTypeTelecommunicationAddress")
public enum DataTypeTelecommunicationAddress {
	@XmlEnumValue("TEL")
	TEL("TEL");
	
	private final String value;

    DataTypeTelecommunicationAddress(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DataTypeTelecommunicationAddress fromValue(String v) {
        for (DataTypeTelecommunicationAddress c: DataTypeTelecommunicationAddress.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}