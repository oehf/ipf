
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for uidOrganisationIdCategorieType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="uidOrganisationIdCategorieType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;maxLength value="3"/>
 *     &lt;minLength value="3"/>
 *     &lt;enumeration value="CHE"/>
 *     &lt;enumeration value="ADM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "uidOrganisationIdCategorieType")
@XmlEnum
public enum UidOrganisationIdCategorieType {

    CHE,
    ADM;

    public String value() {
        return name();
    }

    public static UidOrganisationIdCategorieType fromValue(String v) {
        return valueOf(v);
    }

}
