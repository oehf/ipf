/**
 * MessageCondition.java
 * <p>
 * File generated from the voc::MessageCondition uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration MessageCondition.
 *
 */

@XmlType(name = "MessageCondition")
@XmlEnum
@XmlRootElement(name = "MessageCondition")
public enum MessageCondition {
	@XmlEnumValue("0")
	_0("0"),
	@XmlEnumValue("100")
	_100("100"),
	@XmlEnumValue("101")
	_101("101"),
	@XmlEnumValue("102")
	_102("102"),
	@XmlEnumValue("103")
	_103("103"),
	@XmlEnumValue("200")
	_200("200"),
	@XmlEnumValue("201")
	_201("201"),
	@XmlEnumValue("202")
	_202("202"),
	@XmlEnumValue("203")
	_203("203"),
	@XmlEnumValue("204")
	_204("204"),
	@XmlEnumValue("205")
	_205("205"),
	@XmlEnumValue("206")
	_206("206"),
	@XmlEnumValue("207")
	_207("207");
	
	private final String value;

    MessageCondition(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static MessageCondition fromValue(String v) {
        for (MessageCondition c: MessageCondition.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}