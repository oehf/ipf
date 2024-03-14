/**
 * ActCoverageAuthorizationConfirmationCode.java
 *
 * File generated from the voc::ActCoverageAuthorizationConfirmationCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActCoverageAuthorizationConfirmationCode.
 *
 */

@XmlType(name = "ActCoverageAuthorizationConfirmationCode")
@XmlEnum
@XmlRootElement(name = "ActCoverageAuthorizationConfirmationCode")
public enum ActCoverageAuthorizationConfirmationCode {
	@XmlEnumValue("AUTH")
	AUTH("AUTH"),
	@XmlEnumValue("NAUTH")
	NAUTH("NAUTH");
	
	private final String value;

    ActCoverageAuthorizationConfirmationCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActCoverageAuthorizationConfirmationCode fromValue(String v) {
        for (ActCoverageAuthorizationConfirmationCode c: ActCoverageAuthorizationConfirmationCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}