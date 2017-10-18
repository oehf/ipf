package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.exceptions.FHIRException;

/**
 * This has been dropped from STU3 due to the size of the code system, but we only
 * need a few values for the factory.
 *
 * Replace once https://github.com/jamesagnew/hapi-fhir/issues/761 is released
 *
 * @author Christian Ohr
 */
public enum V3NullFlavor {

    UNK("UNK", "A proper value is applicable, but not known.", "unknown"),
    NULL(null, "?", "?");


    private String code;
    private String definition;
    private String display;

    V3NullFlavor(String code, String definition, String display) {
        this.code = code;
        this.definition = definition;
        this.display = display;
    }

    public static V3NullFlavor fromCode(String codeString) throws FHIRException {
        if (codeString == null || "".equals(codeString))
            return null;

        for (V3NullFlavor v3NullFlavor : V3NullFlavor.values()) {
            if (v3NullFlavor.code.equals(codeString)) {
                return v3NullFlavor;
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
        return "http://hl7.org/fhir/v3/NullFlavor";
    }


}

