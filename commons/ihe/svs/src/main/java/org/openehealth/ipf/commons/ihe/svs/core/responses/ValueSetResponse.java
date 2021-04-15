package org.openehealth.ipf.commons.ihe.svs.core.responses;

import lombok.Data;
import lombok.NonNull;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValueSetResponseType", propOrder = { "conceptList" })
@Data
public class ValueSetResponse {

    @XmlElement(name = "ConceptList", required = true)
    private final List<ConceptList> conceptList = new ArrayList<>();

    @XmlAttribute(name = "id", required = true)
    @NonNull
    private String id;

    @XmlAttribute(name = "displayName", required = true)
    @NonNull
    private String displayName;

    @XmlAttribute(name = "version")
    private String version;

    /**
     * Empty constructor for JAXB.
     */
    public ValueSetResponse() {
    }

    public ValueSetResponse(@NonNull final String id,
                            @NonNull final String displayName,
                            final String version) {
        this.id = id;
        this.displayName = displayName;
        this.version = version;
    }
}
