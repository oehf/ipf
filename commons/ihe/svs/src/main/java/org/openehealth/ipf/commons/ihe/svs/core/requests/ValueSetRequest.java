package org.openehealth.ipf.commons.ihe.svs.core.requests;

import lombok.Data;
import lombok.NonNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValueSetRequestType")
@Data
public class ValueSetRequest {

    @XmlAttribute(name = "id", required = true)
    @NonNull
    private String id;

    @XmlAttribute(name = "lang", namespace = javax.xml.XMLConstants.XML_NS_URI)
    private String lang;

    @XmlAttribute(name = "version")
    private String version;

    /**
     * Empty constructor for JAXB.
     */
    public ValueSetRequest() {
    }

    public ValueSetRequest(@NonNull final String id,
                           final String lang,
                           final String version) {
        this.id = id;
        this.lang = lang;
        this.version = version;
    }
}
