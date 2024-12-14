/**
 * IrrigationSolution.java
 * <p>
 * File generated from the voc::IrrigationSolution uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration IrrigationSolution.
 *
 */

@XmlType(name = "IrrigationSolution")
@XmlEnum
@XmlRootElement(name = "IrrigationSolution")
public enum IrrigationSolution {
	@XmlEnumValue("DOUCHE")
	DOUCHE("DOUCHE"),
	@XmlEnumValue("ENEMA")
	ENEMA("ENEMA"),
	@XmlEnumValue("IRSOL")
	IRSOL("IRSOL"),
	@XmlEnumValue("OPIRSOL")
	OPIRSOL("OPIRSOL");
	
	private final String value;

    IrrigationSolution(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static IrrigationSolution fromValue(String v) {
        for (IrrigationSolution c: IrrigationSolution.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}