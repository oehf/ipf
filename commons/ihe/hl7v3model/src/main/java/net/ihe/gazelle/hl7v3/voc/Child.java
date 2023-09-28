/**
 * Child.java
 *
 * File generated from the voc::Child uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Child.
 *
 */

@XmlType(name = "Child")
@XmlEnum
@XmlRootElement(name = "Child")
public enum Child {
	@XmlEnumValue("CHILD")
	CHILD("CHILD"),
	@XmlEnumValue("CHLDADOPT")
	CHLDADOPT("CHLDADOPT"),
	@XmlEnumValue("CHLDFOST")
	CHLDFOST("CHLDFOST"),
	@XmlEnumValue("CHLDINLAW")
	CHLDINLAW("CHLDINLAW"),
	@XmlEnumValue("DAU")
	DAU("DAU"),
	@XmlEnumValue("DAUADOPT")
	DAUADOPT("DAUADOPT"),
	@XmlEnumValue("DAUFOST")
	DAUFOST("DAUFOST"),
	@XmlEnumValue("DAUINLAW")
	DAUINLAW("DAUINLAW"),
	@XmlEnumValue("NCHILD")
	NCHILD("NCHILD"),
	@XmlEnumValue("SON")
	SON("SON"),
	@XmlEnumValue("SONADOPT")
	SONADOPT("SONADOPT"),
	@XmlEnumValue("SONFOST")
	SONFOST("SONFOST"),
	@XmlEnumValue("SONINLAW")
	SONINLAW("SONINLAW"),
	@XmlEnumValue("STPCHLD")
	STPCHLD("STPCHLD"),
	@XmlEnumValue("STPDAU")
	STPDAU("STPDAU"),
	@XmlEnumValue("STPSON")
	STPSON("STPSON");
	
	private final String value;

    Child(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Child fromValue(String v) {
        for (Child c: Child.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}