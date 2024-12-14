/**
 * DataTypeCodedSimpleValue.java
 * <p>
 * File generated from the voc::DataTypeCodedSimpleValue uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DataTypeCodedSimpleValue.
 *
 */

@XmlType(name = "DataTypeCodedSimpleValue")
@XmlEnum
@XmlRootElement(name = "DataTypeCodedSimpleValue")
public enum DataTypeCodedSimpleValue {
	@XmlEnumValue("CS")
	CS("CS");
	
	private final String value;

    DataTypeCodedSimpleValue(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DataTypeCodedSimpleValue fromValue(String v) {
        for (DataTypeCodedSimpleValue c: DataTypeCodedSimpleValue.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}