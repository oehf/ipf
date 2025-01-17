/**
 * DataTypeSetOfRealNumbers.java
 * <p>
 * File generated from the voc::DataTypeSetOfRealNumbers uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DataTypeSetOfRealNumbers.
 *
 */

@XmlType(name = "DataTypeSetOfRealNumbers")
@XmlEnum
@XmlRootElement(name = "DataTypeSetOfRealNumbers")
public enum DataTypeSetOfRealNumbers {
	@XmlEnumValue("IVL<REAL>")
	IVLREAL("IVL<REAL>"),
	@XmlEnumValue("SET<REAL>")
	SETREAL("SET<REAL>");
	
	private final String value;

    DataTypeSetOfRealNumbers(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DataTypeSetOfRealNumbers fromValue(String v) {
        for (DataTypeSetOfRealNumbers c: DataTypeSetOfRealNumbers.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}