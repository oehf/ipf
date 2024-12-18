/**
 * ContextControlAdditive.java
 * <p>
 * File generated from the voc::ContextControlAdditive uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ContextControlAdditive.
 *
 */

@XmlType(name = "ContextControlAdditive")
@XmlEnum
@XmlRootElement(name = "ContextControlAdditive")
public enum ContextControlAdditive {
	@XmlEnumValue("AN")
	AN("AN"),
	@XmlEnumValue("AP")
	AP("AP");
	
	private final String value;

    ContextControlAdditive(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ContextControlAdditive fromValue(String v) {
        for (ContextControlAdditive c: ContextControlAdditive.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}