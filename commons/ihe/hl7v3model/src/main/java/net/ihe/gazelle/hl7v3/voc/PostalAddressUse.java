/**
 * PostalAddressUse.java
 * <p>
 * File generated from the voc::PostalAddressUse uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration PostalAddressUse.
 *
 */

@XmlType(name = "PostalAddressUse")
@XmlEnum
@XmlRootElement(name = "PostalAddressUse")
public enum PostalAddressUse {
	@XmlEnumValue("ABC")
	ABC("ABC"),
	@XmlEnumValue("BAD")
	BAD("BAD"),
	@XmlEnumValue("DIR")
	DIR("DIR"),
	@XmlEnumValue("H")
	H("H"),
	@XmlEnumValue("HP")
	HP("HP"),
	@XmlEnumValue("HV")
	HV("HV"),
	@XmlEnumValue("IDE")
	IDE("IDE"),
	@XmlEnumValue("PHYS")
	PHYS("PHYS"),
	@XmlEnumValue("PST")
	PST("PST"),
	@XmlEnumValue("PUB")
	PUB("PUB"),
	@XmlEnumValue("SYL")
	SYL("SYL"),
	@XmlEnumValue("TMP")
	TMP("TMP"),
	@XmlEnumValue("WP")
	WP("WP");
	
	private final String value;

    PostalAddressUse(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static PostalAddressUse fromValue(String v) {
        for (PostalAddressUse c: PostalAddressUse.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}