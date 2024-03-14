/**
 * NorthernIroquoian.java
 *
 * File generated from the voc::NorthernIroquoian uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration NorthernIroquoian.
 *
 */

@XmlType(name = "NorthernIroquoian")
@XmlEnum
@XmlRootElement(name = "NorthernIroquoian")
public enum NorthernIroquoian {
	@XmlEnumValue("x-CAY")
	XCAY("x-CAY"),
	@XmlEnumValue("x-MOH")
	XMOH("x-MOH"),
	@XmlEnumValue("x-ONE")
	XONE("x-ONE"),
	@XmlEnumValue("x-ONO")
	XONO("x-ONO"),
	@XmlEnumValue("x-SEE")
	XSEE("x-SEE"),
	@XmlEnumValue("x-TUS")
	XTUS("x-TUS");
	
	private final String value;

    NorthernIroquoian(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static NorthernIroquoian fromValue(String v) {
        for (NorthernIroquoian c: NorthernIroquoian.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}