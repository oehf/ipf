package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.dstu3.model.EnumFactory;
import org.hl7.fhir.exceptions.FHIRException;

/**
 * @author Christian Ohr
 */
public class AuditSourceTypeEnumFactory implements EnumFactory<AuditSourceType> {

    public AuditSourceType fromCode(String codeString) throws IllegalArgumentException {
        try {
            return AuditSourceType.fromCode(codeString);
        } catch (FHIRException e) {
            throw new IllegalArgumentException("Unknown AuditSourceType code '" + codeString + "'");
        }
    }

    public String toCode(AuditSourceType code) {
        return code.toCode();
    }

    public String toSystem(AuditSourceType code) {
        return code.getSystem();
    }

}

