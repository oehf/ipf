/**
 * ContextControlPropagating.java
 * <p>
 * File generated from the voc::ContextControlPropagating uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ContextControlPropagating.
 *
 */

@XmlType(name = "ContextControlPropagating")
@XmlEnum
@XmlRootElement(name = "ContextControlPropagating")
public enum ContextControlPropagating {
	@XmlEnumValue("AP")
	AP("AP"),
	@XmlEnumValue("OP")
	OP("OP");
	
	private final String value;

    ContextControlPropagating(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ContextControlPropagating fromValue(String v) {
        for (ContextControlPropagating c: ContextControlPropagating.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}