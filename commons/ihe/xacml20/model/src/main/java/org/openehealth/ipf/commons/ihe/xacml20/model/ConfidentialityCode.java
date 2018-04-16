package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants.CodingSystemIds;

/**
 * Swiss EPR confidentiality code.
 *
 * @author Dmytro Rud
 */
@AllArgsConstructor
public enum ConfidentialityCode {
    NORMAL    (new CE("1051000195109", CodingSystemIds.SNOMED_CT, "SNOMED Clinical Terms", "Normal")),
    RESTRICTED(new CE("1131000195104", CodingSystemIds.SNOMED_CT, "SNOMED Clinical Terms", "Restricted")),
    SECRET    (new CE("1141000195107", CodingSystemIds.SNOMED_CT, "SNOMED Clinical Terms", "Secret"));

    @Getter private final CE code;
}
