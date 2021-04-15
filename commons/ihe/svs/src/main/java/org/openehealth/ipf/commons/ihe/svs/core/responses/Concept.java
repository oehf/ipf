package org.openehealth.ipf.commons.ihe.svs.core.responses;

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
@XmlType(name = "CD")
@Data
public class Concept {

    @XmlAttribute(name = "code", required = true)
    @NonNull
    private String code;

    @XmlAttribute(name = "codeSystem", required = true)
    @NonNull
    private String codeSystem;

    @XmlAttribute(name = "displayName", required = true)
    @NonNull
    private String displayName;

    @XmlAttribute(name = "codeSystemVersion")
    private String codeSystemVersion;

    /**
     * Empty constructor for JAXB.
     */
    public Concept() {
    }

    public Concept(@NonNull final String code,
                   @NonNull final String codeSystem,
                   @NonNull final String displayName,
                   final String codeSystemVersion) {
        this.code = code;
        this.codeSystem = codeSystem;
        this.displayName = displayName;
        this.codeSystemVersion = codeSystemVersion;
    }
}
