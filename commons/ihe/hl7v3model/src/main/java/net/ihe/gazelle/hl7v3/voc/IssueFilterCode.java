/**
 * IssueFilterCode.java
 *
 * File generated from the voc::IssueFilterCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IssueFilterCode.
 *
 */

@XmlType(name = "IssueFilterCode")
@XmlEnum
@XmlRootElement(name = "IssueFilterCode")
public enum IssueFilterCode {
	@XmlEnumValue("ISSFA")
	ISSFA("ISSFA"),
	@XmlEnumValue("ISSFI")
	ISSFI("ISSFI"),
	@XmlEnumValue("ISSFU")
	ISSFU("ISSFU");
	
	private final String value;

    IssueFilterCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IssueFilterCode fromValue(String v) {
        for (IssueFilterCode c: IssueFilterCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}