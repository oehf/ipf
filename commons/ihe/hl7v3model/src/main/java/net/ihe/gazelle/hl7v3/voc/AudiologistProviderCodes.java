/**
 * AudiologistProviderCodes.java
 *
 * File generated from the voc::AudiologistProviderCodes uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration AudiologistProviderCodes.
 *
 */

@XmlType(name = "AudiologistProviderCodes")
@XmlEnum
@XmlRootElement(name = "AudiologistProviderCodes")
public enum AudiologistProviderCodes {
	@XmlEnumValue("231H00000X")
	_231H00000X("231H00000X"),
	@XmlEnumValue("231HA2400X")
	_231HA2400X("231HA2400X"),
	@XmlEnumValue("231HA2500X")
	_231HA2500X("231HA2500X");
	
	private final String value;

    AudiologistProviderCodes(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static AudiologistProviderCodes fromValue(String v) {
        for (AudiologistProviderCodes c: AudiologistProviderCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}