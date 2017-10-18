package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.exceptions.FHIRException;

/**
 * Replace once https://github.com/jamesagnew/hapi-fhir/issues/761 is released
 *
 * @author Christian Ohr
 */
public enum AuditSourceType {

    _1("1", "End-user display device, diagnostic device.", "User Device"),
    _2("2", "Data acquisition device or instrument.", "Data Interface"),
    _3("3", "Web Server process or thread.", "Web Server"),
    _4("4", "Application Server process or thread.", "Application Server"),
    _5("5", "Database Server process or thread.", "Database Server"),
    _6("6", "Security server, e.g. a domain controller.", "Security Server"),
    _7("7", "ISO level 1-3 network component.", "Network Device"),
    _8("8", "ISO level 4-6 operating software.", "Network Router"),
    _9("9", "other kind of device (defined by DICOM, but some other code/system can be used).", "Other"),
    NULL(null, "?", "?");

    private String code;
    private String definition;
    private String display;

    AuditSourceType(String code, String definition, String display) {
        this.code = code;
        this.definition = definition;
        this.display = display;
    }

    public String getSystem() {
        return "http://hl7.org/fhir/security-source-type";
    }

    public static AuditSourceType fromCode(String codeString) throws FHIRException {
        if (codeString == null || "".equals(codeString))
            return null;

        for (AuditSourceType auditSourceType : AuditSourceType.values()) {
            if (auditSourceType.code.equals(codeString)) {
                return auditSourceType;
            }
        }

        throw new FHIRException("Unknown AuditSourceType code '" + codeString + "'");
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


}

