package org.openehealth.ipf.commons.ihe.fhir.translation;

import org.hl7.fhir.dstu3.model.EnumFactory;

/**
 * This has been dropped from STU3 due to the size of the code system, but we only
 * need a few values
 *
 * @author Christian Ohr
 */
public class ObjectTypeEnumFactory implements EnumFactory<ObjectType> {

    public ObjectType fromCode(String codeString) throws IllegalArgumentException {
        if (codeString == null || "".equals(codeString))
            return null;
        if ("1".equals(codeString))
            return ObjectType._1;
        if ("2".equals(codeString))
            return ObjectType._2;
        if ("3".equals(codeString))
            return ObjectType._3;
        if ("4".equals(codeString))
            return ObjectType._4;
        throw new IllegalArgumentException("Unknown ObjectType code '" + codeString + "'");
    }

    public String toCode(ObjectType code) {
        if (code == ObjectType._1)
            return "1";
        if (code == ObjectType._2)
            return "2";
        if (code == ObjectType._3)
            return "3";
        if (code == ObjectType._4)
            return "4";
        return "?";
    }

    public String toSystem(ObjectType code) {
        return code.getSystem();
    }
}
