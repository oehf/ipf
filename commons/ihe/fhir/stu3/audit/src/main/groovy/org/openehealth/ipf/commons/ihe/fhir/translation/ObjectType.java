package org.openehealth.ipf.commons.ihe.fhir.translation;

/**
 * This has been dropped from STU3 due to the size of the code system, but we only
 * need a few values for the factory.
 *
 * @author Christian Ohr
 */
public enum ObjectType {

    /**
     * Person
     */
    _1,
    /**
     * System Object
     */
    _2,
    /**
     * Organization
     */
    _3,
    /**
     * Other
     */
    _4,
    /**
     * added to help the parsers
     */
    NULL;
    public static ObjectType fromCode(String codeString) throws Exception {
        if (codeString == null || "".equals(codeString))
            return null;
        if ("1".equals(codeString))
            return _1;
        if ("2".equals(codeString))
            return _2;
        if ("3".equals(codeString))
            return _3;
        if ("4".equals(codeString))
            return _4;
        throw new Exception("Unknown ObjectType code '"+codeString+"'");
    }
    public String toCode() {
        switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            default: return "?";
        }
    }
    public String getSystem() {
        return "http://hl7.org/fhir/object-type";
    }
    public String getDefinition() {
        switch (this) {
            case _1: return "Person";
            case _2: return "System Object";
            case _3: return "Organization";
            case _4: return "Other";
            default: return "?";
        }
    }
    public String getDisplay() {
        switch (this) {
            case _1: return "Person";
            case _2: return "System Object";
            case _3: return "Organization";
            case _4: return "Other";
            default: return "?";
        }
    }


}
