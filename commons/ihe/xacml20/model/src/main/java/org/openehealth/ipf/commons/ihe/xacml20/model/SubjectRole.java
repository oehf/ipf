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
@XmlType(name = "SubjectRole", namespace = "http://swisscom.com/hlt/asd/xuagen")
@XmlEnum
public enum SubjectRole {
    PATIENT       (new CE("PAT",       CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Patient")),
    PROFESSIONAL  (new CE("HCP",       CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Healthcare Professional")),
    ASSISTANT     (new CE("ASSISTANT", CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Assistant")),
    REPRESENTATIVE(new CE("REP",       CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Representative")),
    ADMINISTRATOR (new CE("ADMIN",     CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Administrator")),
    ;

    @Getter private final CE code;

    public static SubjectRole fromCode(CE code) {
        for (SubjectRole subjectRole : values()) {
            if (subjectRole.code.equals(code)) {
                return subjectRole;
            }
        }
        return null;
    }
}
