/**
 * DataTypeEventRelatedInterval.java
 *
 * File generated from the voc::DataTypeEventRelatedInterval uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DataTypeEventRelatedInterval.
 *
 */

@XmlType(name = "DataTypeEventRelatedInterval")
@XmlEnum
@XmlRootElement(name = "DataTypeEventRelatedInterval")
public enum DataTypeEventRelatedInterval {
	@XmlEnumValue("EIVL<TS>")
	EIVLTS("EIVL<TS>");
	
	private final String value;

    DataTypeEventRelatedInterval(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DataTypeEventRelatedInterval fromValue(String v) {
        for (DataTypeEventRelatedInterval c: DataTypeEventRelatedInterval.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}