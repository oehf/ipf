/**
 * ActInvoiceDetailGenericModifierCode.java
 *
 * File generated from the voc::ActInvoiceDetailGenericModifierCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActInvoiceDetailGenericModifierCode.
 *
 */

@XmlType(name = "ActInvoiceDetailGenericModifierCode")
@XmlEnum
@XmlRootElement(name = "ActInvoiceDetailGenericModifierCode")
public enum ActInvoiceDetailGenericModifierCode {
	@XmlEnumValue("AFTHRS")
	AFTHRS("AFTHRS"),
	@XmlEnumValue("ISOL")
	ISOL("ISOL"),
	@XmlEnumValue("OOO")
	OOO("OOO");
	
	private final String value;

    ActInvoiceDetailGenericModifierCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActInvoiceDetailGenericModifierCode fromValue(String v) {
        for (ActInvoiceDetailGenericModifierCode c: ActInvoiceDetailGenericModifierCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}