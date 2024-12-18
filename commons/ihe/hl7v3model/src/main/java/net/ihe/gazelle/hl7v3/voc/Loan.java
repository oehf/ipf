/**
 * Loan.java
 * <p>
 * File generated from the voc::Loan uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Loan.
 *
 */

@XmlType(name = "Loan")
@XmlEnum
@XmlRootElement(name = "Loan")
public enum Loan {
	@XmlEnumValue("LOAN")
	LOAN("LOAN"),
	@XmlEnumValue("RENT")
	RENT("RENT");
	
	private final String value;

    Loan(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Loan fromValue(String v) {
        for (Loan c: Loan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}