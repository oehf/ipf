/**
 * ContractorProviderCodes.java
 * <p>
 * File generated from the voc::ContractorProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ContractorProviderCodes.
 *
 */

@XmlType(name = "ContractorProviderCodes")
@XmlEnum
@XmlRootElement(name = "ContractorProviderCodes")
public enum ContractorProviderCodes {
	@XmlEnumValue("171W00000X")
	_171W00000X("171W00000X"),
	@XmlEnumValue("171WH0202X")
	_171WH0202X("171WH0202X"),
	@XmlEnumValue("171WV0202X")
	_171WV0202X("171WV0202X");
	
	private final String value;

    ContractorProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ContractorProviderCodes fromValue(String v) {
        for (ContractorProviderCodes c: ContractorProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}