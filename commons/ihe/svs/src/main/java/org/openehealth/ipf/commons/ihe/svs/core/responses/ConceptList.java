package org.openehealth.ipf.commons.ihe.svs.core.responses;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConceptListType", propOrder = { "concept" })
@Data
public class ConceptList {

    @XmlElement(name = "Concept", required = true)
    private final List<Concept> concept = new ArrayList<>();

    @XmlAttribute(name = "lang", namespace = javax.xml.XMLConstants.XML_NS_URI)
    private String lang;

    /**
     * Empty constructor for JAXB.
     */
    public ConceptList() {
    }

    public ConceptList(final String lang) {
        this.lang = lang;
    }
}
