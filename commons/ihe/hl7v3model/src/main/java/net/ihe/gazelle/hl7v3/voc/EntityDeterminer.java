/**
 * EntityDeterminer.java
 * <p>
 * File generated from the voc::EntityDeterminer uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration EntityDeterminer.
 *
 */

@XmlType(name = "EntityDeterminer")
@XmlEnum
@XmlRootElement(name = "EntityDeterminer")
public enum EntityDeterminer {
	@XmlEnumValue("INSTANCE")
	INSTANCE("INSTANCE"),
	@XmlEnumValue("KIND")
	KIND("KIND"),
	@XmlEnumValue("QUANTIFIED_KIND")
	QUANTIFIEDKIND("QUANTIFIED_KIND");
	
	private final String value;

    EntityDeterminer(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static EntityDeterminer fromValue(String v) {
        for (EntityDeterminer c: EntityDeterminer.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}