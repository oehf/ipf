/**
 * ConceptPropertyId.java
 * <p>
 * File generated from the voc::ConceptPropertyId uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ConceptPropertyId.
 *
 */

@XmlType(name = "ConceptPropertyId")
@XmlEnum
@XmlRootElement(name = "ConceptPropertyId")
public enum ConceptPropertyId {
	@XmlEnumValue("OID")
	OID("OID"),
	@XmlEnumValue("_ValueSetPropertyId")
	VALUESETPROPERTYID("_ValueSetPropertyId"),
	@XmlEnumValue("appliesTo")
	APPLIESTO("appliesTo"),
	@XmlEnumValue("howApplies")
	HOWAPPLIES("howApplies"),
	@XmlEnumValue("inverseRelationship")
	INVERSERELATIONSHIP("inverseRelationship"),
	@XmlEnumValue("openIssue")
	OPENISSUE("openIssue");
	
	private final String value;

    ConceptPropertyId(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ConceptPropertyId fromValue(String v) {
        for (ConceptPropertyId c: ConceptPropertyId.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}