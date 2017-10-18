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
public enum ObjectRole {

    _1("1", "This object is the patient that is the subject of care related to this event.  It is identifiable by patient ID or equivalent.  The patient may be either human or animal.", "Patient"),
    _2("2", "This is a location identified as related to the event.  This is usually the location where the event took place.  Note that for shipping, the usual events are arrival at a location or departure from a location.", "Location"),
    _3("3", "This object is any kind of persistent document created as a result of the event.  This could be a paper report, film, electronic report, DICOM Study, etc.  Issues related to medical records life cycle management are conveyed elsewhere.", "Report"),
    _4("4", "A logical object related to a health record event.  This is any healthcare  specific resource (object) not restricted to FHIR defined Resources.", "Domain Resource"),
    _5("5", "This is any configurable file used to control creation of documents.  Examples include the objects maintained by the HL7 Master File transactions, Value Sets, etc.", "Master file"),
    _6("6", "A human participant not otherwise identified by some other category.", "User"),
    _7("7", "(deprecated)", "List"),
    _8("8", "Typically a licensed person who is providing or performing care related to the event, generally a physician.   The key distinction between doctor and practitioner is with regards to their role, not the licensing.  The doctor is the human who actually performed the work.  The practitioner is the human or organization that is responsible for the work.", "Doctor"),
    _9("9", "A person or system that is being notified as part of the event.  This is relevant in situations where automated systems provide notifications to other parties when an event took place.", "Subscriber"),
    _10("10", "Insurance company, or any other organization who accepts responsibility for paying for the healthcare event.", "Guarantor"),
    _11("11", "A person or active system object involved in the event with a security role.", "Security User Entity"),
    _12("12", "A person or system object involved in the event with the authority to modify security roles of other objects.", "Security User Group"),
    _13("13", "A passive object, such as a role table, that is relevant to the event.", "Security Resource"),
    _14("14", "(deprecated)  Relevant to certain RBAC security methodologies.", "Security Granularity Definition"),
    _15("15", "Any person or organization responsible for providing care.  This encompasses all forms of care, licensed or otherwise, and all sorts of teams and care groups. Note the distinction between practitioner and the doctor that actually provided the care to the patient.", "Practitioner"),
    _16("16", "The source or destination for data transfer, when it does not match some other role.", "Data Destination"),
    _17("17", "A source or destination for data transfer that acts as an archive, database, or similar role.", "Data Repository"),
    _18("18", "An object that holds schedule information.  This could be an appointment book, availability information, etc.", "Schedule"),
    _19("19", "An organization or person that is the recipient of services.  This could be an organization that is buying services for a patient, or a person that is buying services for an animal.", "Customer"),
    _20("20", "An order, task, work item, procedure step, or other description of work to be performed; e.g. a particular instance of an MPPS.", "Job"),
    _21("21", "A list of jobs or a system that provides lists of jobs; e.g. an MWL SCP.", "Job Stream"),
    _22("22", "(Deprecated)", "Table"),
    _23("23", "An object that specifies or controls the routing or delivery of items.  For example, a distribution list is the routing criteria for mail.  The items delivered may be documents, jobs, or other objects.", "Routing Criteria"),
    _24("24", "The contents of a query.  This is used to capture the contents of any kind of query.  For security surveillance purposes knowing the queries being made is very important.", "Query"),
    NULL(null, "?", "?");


    private String code;
    private String definition;
    private String display;

    ObjectRole(String code, String definition, String display) {
        this.code = code;
        this.definition = definition;
        this.display = display;
    }

    public static ObjectRole fromCode(String codeString) throws FHIRException {
        if (codeString == null || "".equals(codeString))
            return null;

        for (ObjectRole objectRole : ObjectRole.values()) {
            if (objectRole.code.equals(codeString)) {
                return objectRole;
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
        return "http://hl7.org/fhir/object-role";
    }


}

