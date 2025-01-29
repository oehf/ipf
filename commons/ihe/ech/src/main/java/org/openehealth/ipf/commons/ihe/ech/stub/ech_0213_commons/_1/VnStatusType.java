
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vnStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="vnStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="active"/>
 *     &lt;enumeration value="inactive"/>
 *     &lt;enumeration value="canceled"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "vnStatusType")
@XmlEnum
public enum VnStatusType {

    @XmlEnumValue("active")
    ACTIVE("active"),
    @XmlEnumValue("inactive")
    INACTIVE("inactive"),
    @XmlEnumValue("canceled")
    CANCELED("canceled");
    private final String value;

    VnStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VnStatusType fromValue(String v) {
        for (VnStatusType c: VnStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
