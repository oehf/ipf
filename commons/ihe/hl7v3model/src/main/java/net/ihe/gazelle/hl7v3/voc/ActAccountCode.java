/**
 * ActAccountCode.java
 * <p>
 * File generated from the voc::ActAccountCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActAccountCode.
 *
 */

@XmlType(name = "ActAccountCode")
@XmlEnum
@XmlRootElement(name = "ActAccountCode")
public enum ActAccountCode {
	@XmlEnumValue("ACCTRECEIVABLE")
	ACCTRECEIVABLE("ACCTRECEIVABLE"),
	@XmlEnumValue("AE")
	AE("AE"),
	@XmlEnumValue("CASH")
	CASH("CASH"),
	@XmlEnumValue("DN")
	DN("DN"),
	@XmlEnumValue("DV")
	DV("DV"),
	@XmlEnumValue("MC")
	MC("MC"),
	@XmlEnumValue("PBILLACCT")
	PBILLACCT("PBILLACCT"),
	@XmlEnumValue("V")
	V("V");
	
	private final String value;

    ActAccountCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActAccountCode fromValue(String v) {
        for (ActAccountCode c: ActAccountCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}