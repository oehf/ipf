/**
 * XLabSpecimenCollectionProviders.java
 * <p>
 * File generated from the voc::XLabSpecimenCollectionProviders uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration XLabSpecimenCollectionProviders.
 *
 */

@XmlType(name = "XLabSpecimenCollectionProviders")
@XmlEnum
@XmlRootElement(name = "XLabSpecimenCollectionProviders")
public enum XLabSpecimenCollectionProviders {
	@XmlEnumValue("communityLaboratory")
	COMMUNITYLABORATORY("communityLaboratory"),
	@XmlEnumValue("homeHealth")
	HOMEHEALTH("homeHealth"),
	@XmlEnumValue("laboratory")
	LABORATORY("laboratory"),
	@XmlEnumValue("pathologist")
	PATHOLOGIST("pathologist"),
	@XmlEnumValue("phlebotomist")
	PHLEBOTOMIST("phlebotomist"),
	@XmlEnumValue("self")
	SELF("self"),
	@XmlEnumValue("thirdParty")
	THIRDPARTY("thirdParty");
	
	private final String value;

    XLabSpecimenCollectionProviders(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static XLabSpecimenCollectionProviders fromValue(String v) {
        for (XLabSpecimenCollectionProviders c: XLabSpecimenCollectionProviders.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}