package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.exceptions.FHIRException;

/**
 * This has been dropped from STU3 due to the size of the code system, but we only
 * need a few values for the factory.
 *
 * @author Christian Ohr
 */
public enum ObjectType {

    _1("1","Person","Person"),
    _2("2","System Object","System Object"),
    _3("3","Organization","Organization"),
    _4("4","Other","Other"),
    NULL(null,"?","?");

    private String code;
    private String definition;
    private String display;

    ObjectType(String code, String definition, String display) {
        this.code = code;
        this.definition = definition;
        this.display = display;
    }

    public static ObjectType fromCode(String codeString) throws FHIRException {
        if (codeString == null || "".equals(codeString))
            return null;

        for (ObjectType objectType : ObjectType.values()) {
            if (objectType.code.equals(codeString)) {
                return objectType;
            }
        }

        throw new FHIRException("Unknown V3NullFlavor code '" + codeString + "'");
    }

    public String toCode() {
        return code;
    }


    public String getDefinition() {
        return definition;

    }

    public String getDisplay() {
        return display;
    }

    public String getSystem() {
        return "http://hl7.org/fhir/object-type";
    }



}
