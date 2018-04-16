package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Dmytro Rud
 */
@AllArgsConstructor
@XmlType(name = "NameQualifier", namespace = "http://swisscom.com/hlt/asd/xuagen")
@XmlEnum(String.class)
public enum NameQualifier {
    PATIENT("urn:e-health-suisse:2015:epr-spid"),
    PROFESSIONAL("urn:gs1:gln"),
    CUSTODIAN_OR_GUARDIAN("urn:e-health-suisse:custodian-id"),
    ;

    @Getter private final String qualifier;

    public static NameQualifier fromCode(String code) {
        for (NameQualifier nameQualifier : values()) {
            if (nameQualifier.qualifier.equals(code)) {
                return nameQualifier;
            }
        }
        return null;
    }
}
