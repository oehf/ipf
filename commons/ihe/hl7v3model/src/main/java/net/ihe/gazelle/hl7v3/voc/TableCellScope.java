/**
 * TableCellScope.java
 *
 * File generated from the voc::TableCellScope uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration TableCellScope.
 *
 */

@XmlType(name = "TableCellScope")
@XmlEnum
@XmlRootElement(name = "TableCellScope")
public enum TableCellScope {
	@XmlEnumValue("col")
	COL("col"),
	@XmlEnumValue("colgroup")
	COLGROUP("colgroup"),
	@XmlEnumValue("row")
	ROW("row"),
	@XmlEnumValue("rowgroup")
	ROWGROUP("rowgroup");
	
	private final String value;

    TableCellScope(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static TableCellScope fromValue(String v) {
        for (TableCellScope c: TableCellScope.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}