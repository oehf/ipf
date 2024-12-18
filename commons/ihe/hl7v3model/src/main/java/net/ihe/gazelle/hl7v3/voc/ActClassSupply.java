/**
 * ActClassSupply.java
 * <p>
 * File generated from the voc::ActClassSupply uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ActClassSupply.
 *
 */

@XmlType(name = "ActClassSupply")
@XmlEnum
@XmlRootElement(name = "ActClassSupply")
public enum ActClassSupply {
	@XmlEnumValue("DIET")
	DIET("DIET"),
	@XmlEnumValue("SPLY")
	SPLY("SPLY");
	
	private final String value;

    ActClassSupply(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ActClassSupply fromValue(String v) {
        for (ActClassSupply c: ActClassSupply.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}