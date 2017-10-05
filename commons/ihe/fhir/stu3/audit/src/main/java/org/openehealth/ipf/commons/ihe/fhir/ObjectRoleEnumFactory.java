package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.dstu3.model.EnumFactory;
import org.hl7.fhir.exceptions.FHIRException;

/**
 * This has been dropped from STU3 due to the size of the code system, but we only
 * need a few values
 *
 * @author Christian Ohr
 */
public class ObjectRoleEnumFactory implements EnumFactory<ObjectRole> {

    public ObjectRole fromCode(String codeString) throws IllegalArgumentException {
        try {
            return ObjectRole.fromCode(codeString);
        } catch (FHIRException e) {
            throw new IllegalArgumentException("Unknown V3NullFlavor code '" + codeString + "'");
        }
    }

    public String toCode(ObjectRole code) {
        return code.toCode();
    }

    public String toSystem(ObjectRole code) {
        return code.getSystem();
    }

}

