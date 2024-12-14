/**
 * DataTypeSetOfSequencesOfCharacterStrings.java
 * <p>
 * File generated from the voc::DataTypeSetOfSequencesOfCharacterStrings uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration DataTypeSetOfSequencesOfCharacterStrings.
 *
 */

@XmlType(name = "DataTypeSetOfSequencesOfCharacterStrings")
@XmlEnum
@XmlRootElement(name = "DataTypeSetOfSequencesOfCharacterStrings")
public enum DataTypeSetOfSequencesOfCharacterStrings {
	@XmlEnumValue("HIST<AD>")
	HISTAD("HIST<AD>"),
	@XmlEnumValue("SET<AD>")
	SETAD("SET<AD>"),
	@XmlEnumValue("SET<HXIT<AD>>")
	SETHXITAD("SET<HXIT<AD>>"),
	@XmlEnumValue("SET<LIST<ST>>")
	SETLISTST("SET<LIST<ST>>");
	
	private final String value;

    DataTypeSetOfSequencesOfCharacterStrings(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static DataTypeSetOfSequencesOfCharacterStrings fromValue(String v) {
        for (DataTypeSetOfSequencesOfCharacterStrings c: DataTypeSetOfSequencesOfCharacterStrings.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}