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
public enum V3MaritalStatus {

    A("A","Marriage contract has been declared null and to not have existed", "Annulled"),
    D("D","Marriage contract has been declared dissolved and inactive", "Divorced"),
    I("I","Subject to an Interlocutory Decree.", "Interlocutory"),
    L("L","Legally Separated", "Legally Separated"),
    M("M","A current marriage contract is active", "Married"),
    P("P","More than 1 current spouse", "Polygamous"),
    S("S","No marriage contract has ever been entered", "Never Married"),
    T("T","Person declares that a domestic partner relationship exists.", "Domestic partner"),
    U("U","Currently not in a marriage contract.", "unmarried"),
    W("W","The spouse has died", "Widowed");


    private String code;
    private String definition;
    private String display;

    V3MaritalStatus(String code, String definition, String display) {
        this.code = code;
        this.definition = definition;
        this.display = display;
    }

    public static V3MaritalStatus fromCode(String codeString) throws FHIRException {
        if (codeString == null || "".equals(codeString))
            return null;

        for (V3MaritalStatus v3MaritalStatus : V3MaritalStatus.values()) {
            if (v3MaritalStatus.code.equals(codeString)) {
                return v3MaritalStatus;
            }
        }

        throw new FHIRException("Unknown V3MaritalStatus code '" + codeString + "'");
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
        return "http://hl7.org/fhir/v3/MaritalStatus";
    }


}

