/**
 * GTSAbbreviationHolidaysChristianRoman.java
 * <p>
 * File generated from the voc::GTSAbbreviationHolidaysChristianRoman uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration GTSAbbreviationHolidaysChristianRoman.
 *
 */

@XmlType(name = "GTSAbbreviationHolidaysChristianRoman")
@XmlEnum
@XmlRootElement(name = "GTSAbbreviationHolidaysChristianRoman")
public enum GTSAbbreviationHolidaysChristianRoman {
	@XmlEnumValue("JHCHREAS")
	JHCHREAS("JHCHREAS"),
	@XmlEnumValue("JHCHRGFR")
	JHCHRGFR("JHCHRGFR"),
	@XmlEnumValue("JHCHRNEW")
	JHCHRNEW("JHCHRNEW"),
	@XmlEnumValue("JHCHRPEN")
	JHCHRPEN("JHCHRPEN"),
	@XmlEnumValue("JHCHRXME")
	JHCHRXME("JHCHRXME"),
	@XmlEnumValue("JHCHRXMS")
	JHCHRXMS("JHCHRXMS");
	
	private final String value;

    GTSAbbreviationHolidaysChristianRoman(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static GTSAbbreviationHolidaysChristianRoman fromValue(String v) {
        for (GTSAbbreviationHolidaysChristianRoman c: GTSAbbreviationHolidaysChristianRoman.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}