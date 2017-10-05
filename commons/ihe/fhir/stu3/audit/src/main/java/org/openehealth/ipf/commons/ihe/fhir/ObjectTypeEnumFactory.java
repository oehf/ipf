package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.dstu3.model.EnumFactory;
import org.hl7.fhir.exceptions.FHIRException;

/**
 * This has been dropped from STU3 due to the size of the code system, but we only
 * need a few values
 *
 * @author Christian Ohr
 */
public class ObjectTypeEnumFactory implements EnumFactory<ObjectType> {

    public ObjectType fromCode(String codeString) throws IllegalArgumentException {
        try {
            return ObjectType.fromCode(codeString);
        } catch (FHIRException e) {
            throw new IllegalArgumentException("Unknown ObjectType code '" + codeString + "'");
        }
    }

    public String toCode(ObjectType code) {
        return code.toCode();
    }

    public String toSystem(ObjectType code) {
        return code.getSystem();
    }
}
