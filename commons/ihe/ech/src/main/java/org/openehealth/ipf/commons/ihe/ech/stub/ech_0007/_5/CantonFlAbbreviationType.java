
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0007._5;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cantonFlAbbreviationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cantonFlAbbreviationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;maxLength value="2"/>
 *     &lt;enumeration value="ZH"/>
 *     &lt;enumeration value="BE"/>
 *     &lt;enumeration value="LU"/>
 *     &lt;enumeration value="UR"/>
 *     &lt;enumeration value="SZ"/>
 *     &lt;enumeration value="OW"/>
 *     &lt;enumeration value="NW"/>
 *     &lt;enumeration value="GL"/>
 *     &lt;enumeration value="ZG"/>
 *     &lt;enumeration value="FR"/>
 *     &lt;enumeration value="SO"/>
 *     &lt;enumeration value="BS"/>
 *     &lt;enumeration value="BL"/>
 *     &lt;enumeration value="SH"/>
 *     &lt;enumeration value="AR"/>
 *     &lt;enumeration value="AI"/>
 *     &lt;enumeration value="SG"/>
 *     &lt;enumeration value="GR"/>
 *     &lt;enumeration value="AG"/>
 *     &lt;enumeration value="TG"/>
 *     &lt;enumeration value="TI"/>
 *     &lt;enumeration value="VD"/>
 *     &lt;enumeration value="VS"/>
 *     &lt;enumeration value="NE"/>
 *     &lt;enumeration value="GE"/>
 *     &lt;enumeration value="JU"/>
 *     &lt;enumeration value="FL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cantonFlAbbreviationType")
@XmlEnum
public enum CantonFlAbbreviationType {

    ZH,
    BE,
    LU,
    UR,
    SZ,
    OW,
    NW,
    GL,
    ZG,
    FR,
    SO,
    BS,
    BL,
    SH,
    AR,
    AI,
    SG,
    GR,
    AG,
    TG,
    TI,
    VD,
    VS,
    NE,
    GE,
    JU,
    FL;

    public String value() {
        return name();
    }

    public static CantonFlAbbreviationType fromValue(String v) {
        return valueOf(v);
    }

}
