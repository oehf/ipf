package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import static org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants.CodingSystemIds;

/**
 * @author Dmytro Rud
 */
@AllArgsConstructor
@XmlType(name = "PurposeOfUse", namespace = "http://swisscom.com/hlt/asd/xuagen")
@XmlEnum
public enum PurposeOfUse {
    NORMAL   (new CE("NORM", CodingSystemIds.SWISS_PURPOSE_OF_USE, "eHealth Suisse Verwendungszweck", "Normal")),
    EMERGENCY(new CE("EMER", CodingSystemIds.SWISS_PURPOSE_OF_USE, "eHealth Suisse Verwendungszweck", "Emergency"));

    @Getter private final CE code;

    public static PurposeOfUse fromCode(CE code) {
        for (PurposeOfUse purposeOfUse : values()) {
            if (purposeOfUse.code.equals(code)) {
                return purposeOfUse;
            }
        }
        return null;
    }
}
