/**
 * ActAdjudicationCode.java
 * <p>
 * File generated from the voc::ActAdjudicationCode uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActAdjudicationCode.
 *
 */

@XmlType(name = "ActAdjudicationCode")
@XmlEnum
@XmlRootElement(name = "ActAdjudicationCode")
public enum ActAdjudicationCode {
	@XmlEnumValue("AA")
	AA("AA"),
	@XmlEnumValue("ANF")
	ANF("ANF"),
	@XmlEnumValue("AR")
	AR("AR"),
	@XmlEnumValue("AS")
	AS("AS");
	
	private final String value;

    ActAdjudicationCode(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActAdjudicationCode fromValue(String v) {
        for (ActAdjudicationCode c: ActAdjudicationCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}