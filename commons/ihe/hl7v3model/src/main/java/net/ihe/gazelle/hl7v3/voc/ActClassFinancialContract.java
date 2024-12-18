/**
 * ActClassFinancialContract.java
 * <p>
 * File generated from the voc::ActClassFinancialContract uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActClassFinancialContract.
 *
 */

@XmlType(name = "ActClassFinancialContract")
@XmlEnum
@XmlRootElement(name = "ActClassFinancialContract")
public enum ActClassFinancialContract {
	@XmlEnumValue("COV")
	COV("COV"),
	@XmlEnumValue("FCNTRCT")
	FCNTRCT("FCNTRCT");
	
	private final String value;

    ActClassFinancialContract(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActClassFinancialContract fromValue(String v) {
        for (ActClassFinancialContract c: ActClassFinancialContract.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}