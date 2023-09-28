/**
 * DataTypeRealNumber.java
 *
 * File generated from the voc::DataTypeRealNumber uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DataTypeRealNumber.
 *
 */

@XmlType(name = "DataTypeRealNumber")
@XmlEnum
@XmlRootElement(name = "DataTypeRealNumber")
public enum DataTypeRealNumber {
	@XmlEnumValue("PPD<REAL>")
	PPDREAL("PPD<REAL>"),
	@XmlEnumValue("REAL")
	REAL("REAL");
	
	private final String value;

    DataTypeRealNumber(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DataTypeRealNumber fromValue(String v) {
        for (DataTypeRealNumber c: DataTypeRealNumber.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}