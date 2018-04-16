package org.openehealth.ipf.commons.ihe.xacml20.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class is used only to create the XML Schema.
 * @author Dmytro Rud
 */
@XmlType(name = "Dummy", namespace = "dummy", propOrder = {"cx", "nameQualifier", "purposeOfUse", "subjectRole"})
public class Dummy {
    @XmlElement
    CX cx;

    @XmlElement
    NameQualifier nameQualifier;

    @XmlElement
    PurposeOfUse purposeOfUse;

    @XmlElement
    SubjectRole subjectRole;
}
