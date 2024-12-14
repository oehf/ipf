/**
 * QueryRequestLimit.java
 * <p>
 * File generated from the voc::QueryRequestLimit uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration QueryRequestLimit.
 *
 */

@XmlType(name = "QueryRequestLimit")
@XmlEnum
@XmlRootElement(name = "QueryRequestLimit")
public enum QueryRequestLimit {
	@XmlEnumValue("RD")
	RD("RD");
	
	private final String value;

    QueryRequestLimit(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static QueryRequestLimit fromValue(String v) {
        for (QueryRequestLimit c: QueryRequestLimit.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}