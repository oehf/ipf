/**
 * DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities.java
 * <p>
 * File generated from the voc::DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities.
 *
 */

@XmlType(name = "DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities")
@XmlEnum
@XmlRootElement(name = "DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities")
public enum DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities {
	@XmlEnumValue("UVP<IVL<PQ>>")
	UVPIVLPQ("UVP<IVL<PQ>>");
	
	private final String value;

    DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities fromValue(String v) {
        for (DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities c: DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}